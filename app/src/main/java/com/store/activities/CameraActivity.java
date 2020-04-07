package com.store.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.store.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

    public static final int REQUEST_CODE = 1;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private SurfaceView surfaceView;

    private String[] neededPermissions = new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        surfaceView = findViewById(R.id.surfaceView);
        if (surfaceView != null) {
            boolean result = checkPermission();
            if (result) {
                setupSurfaceHolder();
            }
        }

    }

    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : neededPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                boolean shouldShowAlert = false;
                for (String permission : permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                }
                if (shouldShowAlert) {
                    showPermissionAlert(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                } else {
                    requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                }
                return false;
            }
        }
        return true;
    }

    private void showPermissionAlert(final String[] permissions) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_required);
        alertBuilder.setMessage(R.string.permission_message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(CameraActivity.this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(CameraActivity.this, R.string.permission_warning, Toast.LENGTH_LONG).show();
                        setViewVisibility(R.id.showPermissionMsg, View.VISIBLE);
                        return;
                    }
                }

                setupSurfaceHolder();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setViewVisibility(int id, int visibility) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void setupSurfaceHolder() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        setCaptureBtnClick();
        setOpenFolderBtnClick();
    }

    private void setCaptureBtnClick() {
        Button startBtn = findViewById(R.id.startBtn);
        if (startBtn != null) {
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });
        }
    }

    private void setOpenFolderBtnClick() {
        Button folderBtn = findViewById(R.id.photosFolderBtn);
        if (folderBtn != null) {
            folderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPhotosFolder();
                }
            });
        }
    }


    private void openPhotosFolder() {
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String path = directory.getPath();
        Uri myImagesdir = Uri.parse("content://" + path);
        intent.setDataAndType(myImagesdir, "*/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Open folder"));

    }

    public void captureImage() {
        if (camera != null) {
            camera.takePicture(null, null, this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startCamera();
    }

    private void startCamera() {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        resetCamera();
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        if (camera != null) {
            camera.stopPreview();
            try {

                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        saveImage(bytes);
        resetCamera();
    }

    private void saveImage(byte[] bytes) {
        FileOutputStream outStream;
        try {
            File dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES);

            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(dir, fileName);
            outStream = new FileOutputStream(file);
            outStream.write(bytes);
            outStream.close();
            Toast.makeText(this, "Picture Saved: " + fileName, Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
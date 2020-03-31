package com.store.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.store.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    List<Sensor> listSensor;
    HashMap<Sensor, TextView> availableSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        LinearLayout linearLayout = findViewById(R.id.sensorList);
        TextView nrOfSensorsView = findViewById(R.id.nrOfSensors);

        availableSensors = new HashMap<>();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        listSensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<String> sensors = new ArrayList<>();

        for (Sensor sensor : listSensor)
            sensors.add(sensor.getName());

        nrOfSensorsView.setText(String.format("Nr. of available sensors: %d", listSensor.size()));
        for (Sensor sensor : listSensor) {
            TextView newTextView = new TextView(this);
            linearLayout.addView(newTextView);
            availableSensors.put(sensor, newTextView);
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor changedSensor = event.sensor;
        float[] currentValues = aproximateValuesFromArray(event.values);
        String sensorName = changedSensor.getName();
        availableSensors.get(changedSensor).setText(String.format("%s \n Values: %s", sensorName, Arrays.toString(currentValues)));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        for (Sensor sensor : availableSensors.keySet()) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    private float[] aproximateValuesFromArray(float[] array) {
        for (int i = 0; i < array.length; i++)
            array[i] = (float) (Math.round(array[i] * 1000.0) / 1000.0);
        return array;
    }
}


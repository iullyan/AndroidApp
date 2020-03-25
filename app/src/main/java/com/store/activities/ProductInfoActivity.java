package com.store.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.store.R;

public class ProductInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        final TextView name = findViewById(R.id.name);
        final TextView price = findViewById(R.id.price);
        final TextView description = findViewById(R.id.description);

        Intent intent = getIntent();

        name.setText(intent.getStringExtra("name"));
        price.setText(intent.getStringExtra("price") + intent.getStringExtra("currency"));
        description.setText(intent.getStringExtra("description"));



    }
}

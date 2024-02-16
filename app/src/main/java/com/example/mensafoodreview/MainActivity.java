package com.example.mensafoodreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewButton = findViewById(R.id.btn_viewReview);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getApplicationContext(), ViewReviewSelectMensa.class);
                Intent i = new Intent(getApplicationContext(), ViewReview.class);
                startActivity(i);
            }
        });
    }

    public void gotoSelection(View view) {
        Intent i = new Intent(getApplicationContext(), selectform.class);
        startActivity(i);
    }
}
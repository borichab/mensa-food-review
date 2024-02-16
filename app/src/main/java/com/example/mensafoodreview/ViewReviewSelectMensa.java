package com.example.mensafoodreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ViewReviewSelectMensa extends AppCompatActivity {

    private Spinner spinner;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review_select_mensa);

        spinner = (Spinner) findViewById(R.id.selectMensaDD);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mensa_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter); // Apply the adapter to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nextBtn = (Button) findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewReview.class);
                //String mensa = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                String mensa = (String) spinner.getSelectedItem();
//                String dish = (String) dishSpinner.getItemAtPosition(dishSpinner.getSelectedItemPosition());
//                i.putExtra("mensa", mensa);
                i.putExtra("mensa", mensa);
                startActivity(i);
            }
        });
    }
}
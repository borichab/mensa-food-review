package com.example.mensafoodreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

public class selectform extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectform);

        nextBtn = (Button) findViewById(R.id.btn_next);

        Spinner mensaSpinner = (Spinner) findViewById(R.id.dd_selectMensa);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.mensa_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mensaSpinner.setAdapter(adapter); // Apply the adapter to the spinner
        mensaSpinner.setOnItemSelectedListener(this);

        Spinner dishSpinner = (Spinner) findViewById(R.id.dd_selectDish);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDish = ArrayAdapter.createFromResource(this,
                R.array.dishe_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDish.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dishSpinner.setAdapter(adapterDish); // Apply the adapter to the spinner
        dishSpinner.setOnItemSelectedListener(this);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), reviewForm.class);
                String mensa = (String) mensaSpinner.getItemAtPosition(mensaSpinner.getSelectedItemPosition());
                String dish = (String) dishSpinner.getItemAtPosition(dishSpinner.getSelectedItemPosition());
                String dish_id = "a" + (int) dishSpinner.getSelectedItemPosition();
                i.putExtra("mensa", mensa);
                i.putExtra("dish", dish);
                i.putExtra("dish_id", dish_id);
                startActivity(i);
            }
        });


    }





    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void writeReview(View view) {

    }
    
    public boolean isEnabled(int position) {
        if (position == 0) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        } else {
            return true;
        }
    }

    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemText = (String) parent.getItemAtPosition(position);
        // If user change the default selection
        // First item is disable and it is used for hint
        if (position > 0) {
            // Notify the selected item text
        }
    }
}
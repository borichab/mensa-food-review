package com.example.mensafoodreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewReview extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ReviewAdapter reviewAdapter;
    private ProgressBar progressBar;
    ArrayList<Review> reviews;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);

        recyclerView = findViewById(R.id.reviewRecyclerview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Reviews");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this,reviews);
        recyclerView.setAdapter(reviewAdapter);

//        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
//        String mensa_name = getIntent().getStringExtra("mensa");


        spinner = (Spinner) findViewById(R.id.selectMensaDD);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mensa_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter); // Apply the adapter to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                progressBar = new ProgressBar(ViewReview.this);
                progressBar.setVisibility(View.VISIBLE);

                String mensa_name = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                reviews.clear();
                Query query = databaseReference.orderByChild("mensa_name").equalTo(mensa_name);
                //Query query = databaseReference.orderByChild("mensa_name").equalTo(mensa_name);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Review review = dataSnapshot.getValue(Review.class);
                            reviews.add(review);
                        }
                        reviewAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewReview.this,"Error "+error,Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    //progressBar.setVisibility(View.INVISIBLE);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Query query = databaseReference.orderByChild("mensa_name");
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
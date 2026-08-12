package com.example.bloodbanksystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateUsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText feedbackEditText;
    private Button submitFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        ratingBar = findViewById(R.id.ratingBar);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String comment = feedbackEditText.getText().toString();

                Toast.makeText(RateUsActivity.this,
                        "Thank you for rating us " + rating + " stars!",
                        Toast.LENGTH_LONG).show();

                // TODO: Optionally save feedback to Firebase or database
                ratingBar.setRating(0);
                feedbackEditText.setText("");
            }
        });
    }
}
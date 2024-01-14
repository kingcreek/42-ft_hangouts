package es.kingcreek.ft_hangouts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import es.kingcreek.ft_hangouts.R;

public class ContactDetails extends AppCompatActivity {

    Toolbar toolbar;
    ImageView profileImage;
    TextView textViewName, textViewEmail, textViewAddress, textViewNumber;
    Button buttonEdit, buttonCall, buttonMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // Views
        toolbar = findViewById(R.id.toolbar);

        profileImage = findViewById(R.id.imageViewProfile);

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewNumber = findViewById(R.id.textViewNumber);

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonCall = findViewById(R.id.buttonCall);
        buttonMessage = findViewById(R.id.buttonMessage);


        Intent intent = getIntent();
        if (intent != null) {
            int contactID = intent.getIntExtra("contact", -1);
            if(contactID != -1)
            {
                populateData(contactID);
            }
        }
    }

    private void populateData(int contactID)
    {

    }
}

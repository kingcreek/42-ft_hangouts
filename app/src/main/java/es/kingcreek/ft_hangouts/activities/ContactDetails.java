package es.kingcreek.ft_hangouts.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.models.ContactModel;

public class ContactDetails extends AppCompatActivity {

    Toolbar toolbar;
    ImageView profileImage;
    TextView textViewName, textViewEmail, textViewAddress, textViewNumber;
    Button buttonEdit, buttonCall, buttonMessage;
    RecyclerView recyclerView;

    CardView cardViewEmail, cardViewAddress;

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

        recyclerView = findViewById(R.id.recyclerViewConversationHistory);

        cardViewEmail = findViewById(R.id.cardViewEmail);
        cardViewAddress = findViewById(R.id.cardViewAddress);

        // Toolbar
        setSupportActionBar(toolbar);
        // Change toolbar Color
        //int toolbarColor = PreferenceHelper.getInstance(getApplicationContext()).getToolbarColor();
        //Helper.changeToolbarColor(toolbar, toolbarColor);

        Intent intent = getIntent();
        if (intent != null) {
            int contactID = intent.getIntExtra("contact", -1);
            if(contactID != -1)
            {
                populateData(contactID);
            }
        }

        buttonEdit.setOnClickListener(v -> {

        });

        buttonCall.setOnClickListener(v -> {

        });

        buttonMessage.setOnClickListener(v -> {

        });
    }

    private void populateData(int contactID)
    {
        // Get Contact
        ContactModel contact = ContactDataSource.getInstance(getApplicationContext()).getContactById(contactID);

        //load image
        if (contact.getImage() != null) {
            profileImage.setImageURI(Uri.parse(contact.getImage()));
        }
        textViewName.setText(new StringBuilder().append(contact.getFirstName()).append(" ").append(contact.getLastName()).toString());
        textViewNumber.setText(contact.getNumber());
        if (contact.getEmail().isEmpty())
            cardViewEmail.setVisibility(View.GONE);
        else
            textViewEmail.setText(contact.getEmail());
        if (contact.getAddress().isEmpty())
            cardViewAddress.setVisibility(View.GONE);
        else
            textViewAddress.setText(contact.getAddress());
    }
}

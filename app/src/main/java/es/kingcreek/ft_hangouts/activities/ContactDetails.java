package es.kingcreek.ft_hangouts.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.adapters.ContactAdapter;
import es.kingcreek.ft_hangouts.adapters.SMSAdapter;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.database.SMSDataSource;
import es.kingcreek.ft_hangouts.helper.CommunicationUtils;
import es.kingcreek.ft_hangouts.helper.Constants;
import es.kingcreek.ft_hangouts.models.ContactModel;
import es.kingcreek.ft_hangouts.models.SMSModel;

public class ContactDetails extends AppCompatActivity {

    private BroadcastReceiver smsReceiver;
    private List<SMSModel> smsList;
    private Toolbar toolbar;
    private ImageView profileImage;
    private TextView textViewName, textViewEmail, textViewAddress, textViewNumber;
    private Button buttonEdit, buttonCall, buttonMessage;
    private RecyclerView recyclerView;
    private SMSAdapter smsAdapter;
    private CardView cardViewEmail, cardViewAddress;

    Intent resultIntent = new Intent();
    int finalContactID;

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
        int contactID = -1;
        if (intent != null) {
            contactID = intent.getIntExtra("contact", -1);
            if(contactID == -1)
            {
                finish();
            }
        }
        populateData(contactID);
        finalContactID = contactID;
        buttonEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, AddContactActivity.class);
            i.putExtra("contactID", finalContactID);
            startActivityForResult(i, Constants.EDIT_CONTACT_REQUEST_CODE);
        });

        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Constants.SMS_RECEIVED_DETAILS)) {
                    int contactID = intent.getIntExtra("contactID", -1);
                    String phoneNumber = intent.getStringExtra("phoneNumber");
                    String message = intent.getStringExtra("message");
                    String time = intent.getStringExtra("time");
                    int inOut = intent.getIntExtra("inOut", 1);

                    if(contactID != -1 && contactID == finalContactID) {
                        smsList.add(new SMSModel(contactID, phoneNumber, message, time, inOut));
                        smsAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Constants.SMS_RECEIVED_DETAILS);
        registerReceiver(smsReceiver, intentFilter);
    }

    private void populateData(int contactID)
    {
        // Get Contact
        ContactModel contact = ContactDataSource.getInstance(getApplicationContext()).getContactById(contactID);
        if (contact == null)
            return;
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

        populateReciclerView(recyclerView, contactID);

        buttonCall.setOnClickListener(v -> {
            CommunicationUtils.makePhoneCall(this, contact.getNumber());
        });

        buttonMessage.setOnClickListener(v -> {
            CommunicationUtils.sendSMS(this, contact);
        });
    }

    private void populateReciclerView(RecyclerView recyclerView, int contactID)
    {
        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get SMS list from db, create adapter and set into RecyclerView
        smsList = SMSDataSource.getInstance(this).getMessagesByContactId(contactID);
        smsAdapter = new SMSAdapter(this, smsList);
        smsAdapter.attachSwipeToDelete(recyclerView);
        recyclerView.setAdapter(smsAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.EDIT_CONTACT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int newContactId = data.getIntExtra("newContact", -1);
                if (newContactId != -1) {
                    populateData(newContactId);
                    resultIntent.putExtra("newContact", (int)newContactId);
                    setResult(RESULT_OK, resultIntent);
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

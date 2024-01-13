package es.kingcreek.ft_hangouts.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDBHelper;
import es.kingcreek.ft_hangouts.database.ContactDataSource;

public class AddContactActivity extends AppCompatActivity {

    private TextInputLayout tilNumber, tilFirstName, tilLastName, tilAddress, tilEmail;
    private TextInputEditText etNumber, etFirstName, etLastName, etAddress, etEmail;
    private Button btnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Views Titles
        tilNumber = findViewById(R.id.tilNumber);
        tilFirstName = findViewById(R.id.tilFirstName);
        tilLastName = findViewById(R.id.tilLastName);
        tilAddress = findViewById(R.id.tilAddress);
        tilEmail = findViewById(R.id.tilEmail);

        // Views Input
        etNumber = findViewById(R.id.etNumber);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        // Button
        btnAddContact = findViewById(R.id.btnAddContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = etNumber.getText().toString();
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String address = etAddress.getText().toString();
                String email = etEmail.getText().toString();

                ContactDataSource dataSource = ContactDataSource.getInstance(getApplicationContext());
                long result = dataSource.insertContact(number, firstName, lastName, address, email);
                dataSource.close();
                if (result != -1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.insert_success), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.insert_fail), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
package es.kingcreek.ft_hangouts.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.helper.BitmapHelper;
import es.kingcreek.ft_hangouts.helper.Constants;
import es.kingcreek.ft_hangouts.helper.PreferenceHelper;
import es.kingcreek.ft_hangouts.models.ContactModel;

public class AddContactActivity extends AppCompatActivity {

    private TextInputEditText etNumber, etFirstName, etLastName, etAddress, etEmail;
    private Button btnAddContact, btnSelectImage, btnRemoveImage;
    private ImageView imagePreview;
    private String imageUri = "";

    int contactID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Views Input
        etNumber = findViewById(R.id.etNumber);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        // Button
        btnAddContact = findViewById(R.id.btnAddContact);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnRemoveImage = findViewById(R.id.btnRemoveImage);
        btnRemoveImage.setVisibility(View.GONE);
        // ImageView
        imagePreview = findViewById(R.id.imagePreview);
        imagePreview.setVisibility(View.GONE);

        // Get intent to check if it's edit contact
        Intent intent = getIntent();
        if (intent != null) {
            contactID = intent.getIntExtra("contactID", -1);
            if(contactID != -1)
            {
                ContactModel contact = ContactDataSource.getInstance(getApplicationContext()).getContactById(contactID);
                etNumber.setText(contact.getNumber());
                etFirstName.setText(contact.getFirstName());
                etLastName.setText(contact.getLastName());
                etAddress.setText(contact.getAddress());
                etEmail.setText(contact.getEmail());
                if(!contact.getImage().isEmpty())
                {
                    imageUri = contact.getImage();
                    imagePreview.setVisibility(View.VISIBLE);
                    if (!imageUri.isEmpty()) {
                        BitmapHelper.LoadImageTask loadImageTask = new BitmapHelper.LoadImageTask(imagePreview);
                        loadImageTask.execute(imageUri);
                    }
                    //imagePreview.setImageURI(Uri.parse(imageUri));
                    btnRemoveImage.setVisibility(View.VISIBLE);
                }
            }
        }

        btnSelectImage.setOnClickListener(v -> pickImageFromGallery());

        btnAddContact.setOnClickListener(view -> {

            String number = etNumber.getText().toString();
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String address = etAddress.getText().toString();
            String email = etEmail.getText().toString();
            ContactModel newContact = new ContactModel(number, firstName, lastName, address, email, BitmapHelper.stringToBase64(getApplicationContext(), imageUri));

            if(number.isEmpty())
            {
                Toast.makeText(getApplicationContext(), getString(R.string.need_number), Toast.LENGTH_LONG).show();
                return;
            }
            if(firstName.isEmpty())
            {
                Toast.makeText(getApplicationContext(), getString(R.string.need_name), Toast.LENGTH_LONG).show();
                return;
            }

            ContactDataSource dataSource = ContactDataSource.getInstance(getApplicationContext());
            if(contactID != -1) {
                newContact.setId(contactID);
                dataSource.updateContact(newContact);
            }
            else
                contactID = (int)dataSource.insertContact(newContact);

            if (contactID != -1) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_success), Toast.LENGTH_LONG).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newContact", contactID);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_fail), Toast.LENGTH_LONG).show();
            }
        });

        btnRemoveImage.setOnClickListener(v -> {
            imageUri = "";
            imagePreview.setImageDrawable(null);
            imagePreview.setVisibility(View.GONE);
            btnRemoveImage.setVisibility(View.GONE);
        });
    }

    private void pickImageFromGallery() {
        if (!PreferenceHelper.getInstance(this).isBonusActive())
        {
            Toast.makeText(this, getString(R.string.only_bonus), Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            ContentResolver contentResolver = getContentResolver();
            contentResolver.takePersistableUriPermission(selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = selectedImageUri.toString();
            imagePreview.setVisibility(View.VISIBLE);
            imagePreview.setImageURI(selectedImageUri);
            btnRemoveImage.setVisibility(View.VISIBLE);
        }
    }

}
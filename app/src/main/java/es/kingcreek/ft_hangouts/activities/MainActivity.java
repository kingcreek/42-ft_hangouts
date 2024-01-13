package es.kingcreek.ft_hangouts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.adapters.ContactAdapter;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.helper.Constants;
import es.kingcreek.ft_hangouts.models.ContactModel;

import android.database.Cursor;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ContactDataSource dataSource;
    private ContactAdapter contactAdapter;
    List<ContactModel> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = ContactDataSource.getInstance(this);
        dataSource.open();

        // Set RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewContacts);
        populateReciclerView(recyclerView);

        FloatingActionButton fabAddContact = findViewById(R.id.fab_add_contact);
        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivityForResult(intent, Constants.ADD_CONTACT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dataSource.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "request: " + requestCode + "  result: " + resultCode, Toast.LENGTH_LONG).show();
        if (requestCode == Constants.ADD_CONTACT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ContactModel newContact = data.getParcelableExtra("newContact");
                if (newContact != null) {
                    contactList.add(newContact);
                    contactAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void populateReciclerView(RecyclerView recyclerView)
    {
        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get cursor from db, create adapter and set into RecyclerView
        contactList = dataSource.getContacts();
        contactAdapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(contactAdapter);
    }

    private void refreshRecyclerView()
    {
        /*
        Cursor newCursor = dataSource.getContactsCursor();

        // Actualizar el cursor en el adaptador
        if (contactAdapter != null) {
            contactAdapter.changeCursor(newCursor);
            contactAdapter.notifyDataSetChanged();
        } else {
            // Si el adaptador aún no está inicializado, inicialízalo
            populateReciclerView((RecyclerView) findViewById(R.id.recyclerViewContacts));
        }

         */
    }
}
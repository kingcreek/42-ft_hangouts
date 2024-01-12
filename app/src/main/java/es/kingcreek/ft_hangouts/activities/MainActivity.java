package es.kingcreek.ft_hangouts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.adapters.ContactAdapter;
import es.kingcreek.ft_hangouts.database.ContactDataSource;

import android.database.Cursor;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private ContactDataSource dataSource;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new ContactDataSource(this);
        dataSource.open();

        // Set RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewContacts);
        populateReciclerView(recyclerView);

        FloatingActionButton fabAddContact = findViewById(R.id.fab_add_contact);
        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    private void populateReciclerView(RecyclerView recyclerView)
    {
        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get cursor from db, create adapter and set into RecyclerView
        Cursor cursor = dataSource.getContactsCursor();
        contactAdapter = new ContactAdapter(this, cursor);
        recyclerView.setAdapter(contactAdapter);
    }
}
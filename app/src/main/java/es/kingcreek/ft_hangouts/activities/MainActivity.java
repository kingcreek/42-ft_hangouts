package es.kingcreek.ft_hangouts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ContactDataSource dataSource;
    private ContactAdapter contactAdapter;
    List<ContactModel> contactList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set DB
        dataSource = ContactDataSource.getInstance(this);
        dataSource.open();

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Configura el SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Ajustes", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Override function to reload RecyclerView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    // Function to populate RecyclerView first time
    private void populateReciclerView(RecyclerView recyclerView)
    {
        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get cursor from db, create adapter and set into RecyclerView
        contactList = dataSource.getContacts();
        contactAdapter = new ContactAdapter(this, contactList);
        contactAdapter.attachSwipeToDelete(recyclerView);
        recyclerView.setAdapter(contactAdapter);
    }
}
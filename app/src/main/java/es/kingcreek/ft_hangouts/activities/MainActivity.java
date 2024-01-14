package es.kingcreek.ft_hangouts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.adapters.ContactAdapter;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.fragment.SettingsDialogFragment;
import es.kingcreek.ft_hangouts.helper.Constants;
import es.kingcreek.ft_hangouts.helper.PreferenceHelper;
import es.kingcreek.ft_hangouts.interfaces.OnDialogDismissListener;
import es.kingcreek.ft_hangouts.models.ContactModel;

import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnDialogDismissListener {

    private ContactDataSource dataSource;
    private ContactAdapter contactAdapter;
    List<ContactModel> filteredContacts;
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

        // Change toolbar Color
        int toolbarColor = PreferenceHelper.getInstance(getApplicationContext()).getToolbarColor();
        changeToolbarColor(toolbarColor);

        // Change dark/idiot mode
        boolean isDarkMode = PreferenceHelper.getInstance(getApplicationContext()).isDarkMode();
        setAppTheme(isDarkMode);

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

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return true;}

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
            SettingsDialogFragment settingsDialog = new SettingsDialogFragment();
            settingsDialog.show(getSupportFragmentManager(), "settingsDialog");
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
                int newContactId = data.getIntExtra("newContact", -1);
                if (newContactId != -1) {
                    ContactModel contact = dataSource.getContactById(newContactId);
                    contactList.add(contact);
                    filteredContacts.add(contact);
                    if (contactAdapter.isFiltering()) {
                        contactAdapter.filter(contactAdapter.getFilter());
                    }
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
        // Get Contact list from db, create adapter and set into RecyclerView
        contactList = dataSource.getContacts();
        filteredContacts = dataSource.getContacts();
        contactAdapter = new ContactAdapter(this, contactList, filteredContacts);
        contactAdapter.attachSwipeToDelete(recyclerView);
        recyclerView.setAdapter(contactAdapter);
    }

    // Change toolbar color
    private void changeToolbarColor(int selectedColor) {
        toolbar.setBackgroundColor(selectedColor);
    }

    // Change theme
    private void setAppTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onDialogDismissed() {
        int toolbarColor = PreferenceHelper.getInstance(getApplicationContext()).getToolbarColor();
        changeToolbarColor(toolbarColor);
    }
}
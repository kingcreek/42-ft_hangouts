package es.kingcreek.ft_hangouts.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import es.kingcreek.ft_hangouts.R;
import es.kingcreek.ft_hangouts.adapters.ContactAdapter;
import es.kingcreek.ft_hangouts.database.ContactDataSource;
import es.kingcreek.ft_hangouts.fragment.SettingsDialogFragment;
import es.kingcreek.ft_hangouts.helper.Constants;
import es.kingcreek.ft_hangouts.helper.Helper;
import es.kingcreek.ft_hangouts.helper.PreferenceHelper;
import es.kingcreek.ft_hangouts.interfaces.OnDialogDismissListener;
import es.kingcreek.ft_hangouts.models.ContactModel;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogDismissListener {

    private final String TAG = "MainActivity";
    private BroadcastReceiver smsReceiver;
    private ContactAdapter contactAdapter;
    List<ContactModel> filteredContacts;
    List<ContactModel> contactList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check permissons
        permissions();

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change dark/idiot mode
        boolean isDarkMode = PreferenceHelper.getInstance(getApplicationContext()).isDarkMode();
        setAppTheme(isDarkMode);

        // Change toolbar Color
        int toolbarColor = PreferenceHelper.getInstance(getApplicationContext()).getToolbarColor();
        Helper.changeToolbarColor(toolbar, toolbarColor);
        Helper.changeAppThemeColor(getApplicationContext(), toolbarColor);

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

        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Constants.SMS_RECEIVED_MAIN)) {
                    int contactID = intent.getIntExtra("contactID", -1);
                    if(contactID != -1)
                        addContactUpdate(contactID);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Constants.SMS_RECEIVED_MAIN);
        registerReceiver(smsReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dataSource.close();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
        PreferenceHelper.getInstance(this).saveLastTime(Helper.getCurrentDateTimeString());
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
                    addContactUpdate(newContactId);
                }
            }
        } else if (requestCode == Constants.VIEW_CONTACT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int newContactId = data.getIntExtra("newContact", -1);
                if (newContactId != -1) {
                    ContactModel contact = ContactDataSource.getInstance(this).getContactById(newContactId);
                    findAndReplace(contactList, contact);
                    findAndReplace(filteredContacts, contact);
                    if (contactAdapter.isFiltering()) {
                        contactAdapter.filter(contactAdapter.getFilter());
                    }
                }
            }
        }
    }

    private void findAndReplace(List<ContactModel> lst, ContactModel contact) {
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getId() == contact.getId()) {
                lst.set(i, contact);
                contactAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    private void addContactUpdate(int newContact)
    {
        ContactModel contact = ContactDataSource.getInstance(this).getContactById(newContact);
        contactList.add(contact);
        filteredContacts.add(contact);
        if (contactAdapter.isFiltering()) {
            contactAdapter.filter(contactAdapter.getFilter());
        }
        contactAdapter.notifyDataSetChanged();
    }

    // Function to populate RecyclerView first time
    private void populateReciclerView(RecyclerView recyclerView)
    {
        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get Contact list from db, create adapter and set into RecyclerView
        contactList = ContactDataSource.getInstance(this).getContacts();
        filteredContacts = ContactDataSource.getInstance(this).getContacts();
        contactAdapter = new ContactAdapter(this, contactList, filteredContacts);
        contactAdapter.attachSwipeToDelete(recyclerView);
        recyclerView.setAdapter(contactAdapter);
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
        // Change dark/white mode
        boolean isDarkMode = PreferenceHelper.getInstance(getApplicationContext()).isDarkMode();
        setAppTheme(isDarkMode);

        // Change toolbar Color
        int toolbarColor = PreferenceHelper.getInstance(getApplicationContext()).getToolbarColor();
        Helper.changeToolbarColor(toolbar, toolbarColor);
        Helper.changeAppThemeColor(getApplicationContext(), toolbarColor);
    }

    private void permissions() {
        // Required permissions
        String[] requiredPermissions = {
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE/*,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE*/
        };

        // List of permissions that have not yet been granted
        List<String> permissionsToRequest = new ArrayList<>();

        // Check each permission individually
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, add to list of permissions to request
                permissionsToRequest.add(permission);
            }
        }

        // Convert the permission list to an array and request permissions
        if (!permissionsToRequest.isEmpty()) {
            String[] permissionsArray = permissionsToRequest.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissionsArray, Constants.MI_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.MI_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showPermissionExplanationDialog(permissions[i], requestCode);
                }
            }
        }
    }

    private void showPermissionExplanationDialog(String permission, final int requestCode) {
        String message = "";

        switch (permission) {
            case Manifest.permission.READ_MEDIA_IMAGES:
                message = getString(R.string.permisson_storage);
                break;
            case Manifest.permission.RECEIVE_SMS:
                message = getString(R.string.permisson_sms);
                break;
            case Manifest.permission.SEND_SMS:
                message = getString(R.string.permisson_sms_send);
                break;
            case Manifest.permission.CALL_PHONE:
                message = getString(R.string.permisson_call);
                break;
            case Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE:
                message = getString(R.string.permisson_notification);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissionAgain(requestCode);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }

    private void requestPermissionAgain(int requestCode) {
        String[] requiredPermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
        };
        ActivityCompat.requestPermissions(this, requiredPermissions, requestCode);
    }
}
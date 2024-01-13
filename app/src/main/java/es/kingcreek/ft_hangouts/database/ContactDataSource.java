package es.kingcreek.ft_hangouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactDataSource {

    private static ContactDataSource instance;
    private SQLiteDatabase database;
    private static ContactDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public static synchronized ContactDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new ContactDataSource(context.getApplicationContext());
        }
        return instance;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContact(String number, String firstName, String lastName, String address, String email) {
        ContentValues values = new ContentValues();
        values.put(ContactDBHelper.COLUMN_NUMBER, number);
        values.put(ContactDBHelper.COLUMN_FIRST_NAME, firstName);
        values.put(ContactDBHelper.COLUMN_LAST_NAME, lastName);
        values.put(ContactDBHelper.COLUMN_ADDRESS, address);
        values.put(ContactDBHelper.COLUMN_EMAIL, email);

        return database.insert(ContactDBHelper.TABLE_CONTACTS, null, values);
    }

    public Cursor getContactsCursor() {
        String[] allColumns = {
                ContactDBHelper.COLUMN_ID,
                ContactDBHelper.COLUMN_NUMBER,
                ContactDBHelper.COLUMN_FIRST_NAME,
                ContactDBHelper.COLUMN_LAST_NAME,
                ContactDBHelper.COLUMN_ADDRESS,
                ContactDBHelper.COLUMN_EMAIL
        };

        return database.query(ContactDBHelper.TABLE_CONTACTS, allColumns, null, null, null, null, null);
    }
}

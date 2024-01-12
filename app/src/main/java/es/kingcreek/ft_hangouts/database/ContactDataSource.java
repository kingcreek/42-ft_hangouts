package es.kingcreek.ft_hangouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactDataSource {

    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContact(String number, String firstName, String lastName, String nick, String email) {
        ContentValues values = new ContentValues();
        values.put(ContactDBHelper.COLUMN_NUMBER, number);
        values.put(ContactDBHelper.COLUMN_FIRST_NAME, firstName);
        values.put(ContactDBHelper.COLUMN_LAST_NAME, lastName);
        values.put(ContactDBHelper.COLUMN_NICK, nick);
        values.put(ContactDBHelper.COLUMN_EMAIL, email);

        return database.insert(ContactDBHelper.TABLE_CONTACTS, null, values);
    }

    public Cursor getContactsCursor() {
        String[] allColumns = {
                ContactDBHelper.COLUMN_ID,
                ContactDBHelper.COLUMN_NUMBER,
                ContactDBHelper.COLUMN_FIRST_NAME,
                ContactDBHelper.COLUMN_LAST_NAME,
                ContactDBHelper.COLUMN_NICK,
                ContactDBHelper.COLUMN_EMAIL
        };

        return database.query(ContactDBHelper.TABLE_CONTACTS, allColumns, null, null, null, null, null);
    }
}

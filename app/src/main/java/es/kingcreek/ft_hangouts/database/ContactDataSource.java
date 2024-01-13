package es.kingcreek.ft_hangouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import es.kingcreek.ft_hangouts.helper.BitmapHelper;
import es.kingcreek.ft_hangouts.models.ContactModel;

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

    public long insertContact(ContactModel newContact) {
        ContentValues values = new ContentValues();
        values.put(ContactDBHelper.COLUMN_NUMBER, newContact.getNumber());
        values.put(ContactDBHelper.COLUMN_FIRST_NAME, newContact.getFirstName());
        values.put(ContactDBHelper.COLUMN_LAST_NAME, newContact.getLastName());
        values.put(ContactDBHelper.COLUMN_ADDRESS, newContact.getAddress());
        values.put(ContactDBHelper.COLUMN_EMAIL, newContact.getEmail());

        return database.insert(ContactDBHelper.TABLE_CONTACTS, null, values);
    }

    public List<ContactModel> getContacts() {
        List<ContactModel> contactList = new ArrayList<>();
        Cursor cursor = database.query(ContactDBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_ID));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_NUMBER));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_LAST_NAME));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_ADDRESS));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_EMAIL));

                ContactModel contact = new ContactModel(id, number, firstName, lastName, address, email);
                contactList.add(contact);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return contactList;
    }

    public long insertContactWithImage(String number, String firstName, String lastName, String email, Bitmap imageBitmap) {
        ContentValues values = new ContentValues();
        values.put(ContactDBHelper.COLUMN_NUMBER, number);
        values.put(ContactDBHelper.COLUMN_FIRST_NAME, firstName);
        values.put(ContactDBHelper.COLUMN_LAST_NAME, lastName);
        values.put(ContactDBHelper.COLUMN_EMAIL, email);

        // Convierte la imagen en un array de bytes (blob)
        byte[] imageBytes = BitmapHelper.getBytesFromBitmap(imageBitmap);
        values.put(ContactDBHelper.COLUMN_IMAGE, imageBytes);

        return database.insert(ContactDBHelper.TABLE_CONTACTS, null, values);
    }

    public Bitmap getImageForContact(long contactId) {
        Cursor cursor = database.query(ContactDBHelper.TABLE_CONTACTS, new String[]{ContactDBHelper.COLUMN_IMAGE},
                ContactDBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(contactId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_IMAGE));
            cursor.close();

            // Convierte el array de bytes de vuelta a un bitmap
            return BitmapHelper.getBitmapFromBytes(imageBytes);
        }

        return null;
    }
}

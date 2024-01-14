package es.kingcreek.ft_hangouts.database;

import static es.kingcreek.ft_hangouts.database.ContactDBHelper.COLUMN_ADDRESS;
import static es.kingcreek.ft_hangouts.database.ContactDBHelper.COLUMN_EMAIL;
import static es.kingcreek.ft_hangouts.database.ContactDBHelper.COLUMN_FIRST_NAME;
import static es.kingcreek.ft_hangouts.database.ContactDBHelper.COLUMN_ID;
import static es.kingcreek.ft_hangouts.database.ContactDBHelper.COLUMN_IMAGE;
import static es.kingcreek.ft_hangouts.database.ContactDBHelper.COLUMN_LAST_NAME;
import static es.kingcreek.ft_hangouts.database.ContactDBHelper.COLUMN_NUMBER;
import static es.kingcreek.ft_hangouts.database.ContactDBHelper.TABLE_CONTACTS;

import android.annotation.SuppressLint;
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

    private boolean contactExists(String phoneNumber) {
        Cursor cursor = database.query(
                TABLE_CONTACTS,
                null,
                COLUMN_NUMBER + " = ? OR " + COLUMN_FIRST_NAME + " = ?",
                new String[]{phoneNumber, phoneNumber},
                null,
                null,
                null
        );

        boolean exists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContact(ContactModel newContact) {

        if (contactExists(newContact.getNumber())) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, newContact.getNumber() == null ? "" : newContact.getNumber());
        values.put(COLUMN_FIRST_NAME, newContact.getFirstName() == null ? "" : newContact.getFirstName());
        values.put(COLUMN_LAST_NAME, newContact.getLastName() == null ? "" : newContact.getLastName());
        values.put(COLUMN_ADDRESS, newContact.getAddress() == null ? "" : newContact.getAddress());
        values.put(COLUMN_EMAIL, newContact.getEmail() == null ? "" : newContact.getEmail());
        values.put(COLUMN_IMAGE, newContact.getImage() == null ? "" : newContact.getImage());

        return database.insert(TABLE_CONTACTS, null, values);
    }

    public ContactModel getContactById(int id) {
        ContactModel contact = null;
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = database.query(TABLE_CONTACTS, null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            contact = new ContactModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            );
            cursor.close();
        }
        return contact;
    }

    public List<ContactModel> getContacts() {
        List<ContactModel> contactList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMBER));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ContactDBHelper.COLUMN_ADDRESS));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

                ContactModel contact = new ContactModel(id, number, firstName, lastName, address, email, image);
                contactList.add(contact);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return contactList;
    }

    public void removeContactById(int id)
    {
        database.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

}

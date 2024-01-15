package es.kingcreek.ft_hangouts.database;


import static es.kingcreek.ft_hangouts.database.DatabaseTables.COLUMN_CONTACT_ID;
import static es.kingcreek.ft_hangouts.database.DatabaseTables.COLUMN_MESSAGE_DIR;
import static es.kingcreek.ft_hangouts.database.DatabaseTables.COLUMN_MESSAGE_ID;
import static es.kingcreek.ft_hangouts.database.DatabaseTables.COLUMN_MESSAGE_TEXT;
import static es.kingcreek.ft_hangouts.database.DatabaseTables.COLUMN_PHONE_NUMBER;
import static es.kingcreek.ft_hangouts.database.DatabaseTables.COLUMN_TIMESTAMP;
import static es.kingcreek.ft_hangouts.database.DatabaseTables.TABLE_MESSAGES;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.kingcreek.ft_hangouts.helper.Helper;
import es.kingcreek.ft_hangouts.models.ContactModel;
import es.kingcreek.ft_hangouts.models.SMSModel;

public class SMSDataSource {

    private static SMSDataSource instance;
    private SQLiteDatabase database;
    private static DatabaseTables databaseTables;

    public SMSDataSource(Context context) {
        databaseTables = new DatabaseTables(context);
    }

    public static synchronized SMSDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new SMSDataSource(context.getApplicationContext());
        }
        if (instance.database == null)
            instance.open();
        return instance;
    }

    public void open() throws SQLException {
        database = databaseTables.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public long insertSMS(SMSModel newSMS) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, newSMS.getContactId() == -1 ? 0 : newSMS.getContactId());
        values.put(COLUMN_PHONE_NUMBER, newSMS.getPhoneNumber() == null ? "" : newSMS.getPhoneNumber());
        values.put(COLUMN_MESSAGE_TEXT, newSMS.getMessage() == null ? "" : newSMS.getMessage());
        values.put(COLUMN_TIMESTAMP, newSMS.getTime() == null ? "" : newSMS.getTime());
        values.put(COLUMN_MESSAGE_DIR, newSMS.getInOut());

        return database.insert(TABLE_MESSAGES, null, values);
    }

    public List<SMSModel> getMessagesByContactId(long contactId) {
        List<SMSModel> messagesList = new ArrayList<>();

        String[] columns = {COLUMN_MESSAGE_ID, COLUMN_CONTACT_ID, COLUMN_PHONE_NUMBER, COLUMN_MESSAGE_TEXT, COLUMN_TIMESTAMP, COLUMN_MESSAGE_DIR};
        String selection = COLUMN_CONTACT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(contactId)};

        Cursor cursor = database.query(TABLE_MESSAGES, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                SMSModel message = new SMSModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_TEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE_DIR))
                );
                messagesList.add(message);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return messagesList;
    }

    public void removeSMSById(int id) {
        database.delete(TABLE_MESSAGES, COLUMN_MESSAGE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void removeMessagesFromContact(int contactID) {
        String whereClause = DatabaseTables.COLUMN_CONTACT_ID + " = ?";
        String[] whereArgs = {String.valueOf(contactID)};
        database.delete(DatabaseTables.TABLE_MESSAGES, whereClause, whereArgs);
    }

}

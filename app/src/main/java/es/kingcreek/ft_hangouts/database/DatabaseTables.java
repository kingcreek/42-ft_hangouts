package es.kingcreek.ft_hangouts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseTables extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "contacts.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    //////////////
    // CONTACTS //
    //////////////
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_EMAIL = "email";

    //////////////
    // MESSAGES //
    //////////////
    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_MESSAGE_ID = "_id";
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_TIMESTAMP = "message_timestamp";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";

    private static final String DATABASE_CONTACTS = "create table "
            + TABLE_CONTACTS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NUMBER + " text not null, "
            + COLUMN_FIRST_NAME + " text not null, "
            + COLUMN_LAST_NAME + " text, "
            + COLUMN_ADDRESS + " text, "
            + COLUMN_IMAGE + " text, "
            + COLUMN_EMAIL + " text);";

    private static final String CREATE_TABLE_MESSAGES =
            "CREATE TABLE " + TABLE_MESSAGES + " (" +
                    COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CONTACT_ID + " INTEGER, " +
                    COLUMN_TIMESTAMP + " TEXT, " +
                    COLUMN_PHONE_NUMBER + " TEXT, " +
                    COLUMN_MESSAGE_TEXT + " TEXT);";

    public DatabaseTables(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CONTACTS);
        database.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }
}

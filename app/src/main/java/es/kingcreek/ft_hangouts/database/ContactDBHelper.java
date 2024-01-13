package es.kingcreek.ft_hangouts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDBHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "contacts.db";

    // Versión de la base de datos. Cambia si modificas la estructura de la base de datos.
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y los nombres de las columnas
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_ADDRESS = "address";

    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_EMAIL = "email";

    // Sentencia SQL para la creación de la tabla
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CONTACTS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NUMBER + " text not null, "
            + COLUMN_FIRST_NAME + " text not null, "
            + COLUMN_LAST_NAME + " text not null, "
            + COLUMN_ADDRESS + " text not null, "
            + COLUMN_IMAGE + " text, "
            + COLUMN_EMAIL + " text not null);";

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }
}

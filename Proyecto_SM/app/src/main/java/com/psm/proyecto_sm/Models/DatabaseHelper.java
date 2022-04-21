package com.psm.proyecto_sm.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bd_buzztalk";
    private static final int DB_VERSION = 1;

    public static final String TABLE_USER = "user";
    public static final String KEY_ID_USER = "Id_User";
    public static final String KEY_NAME_USER = "Name";
    public static final String KEY_EMAIL_USER = "Email";
    public static final String KEY_PASS_USER = "Password";
    public static final String KEY_PHONE_USER = "Phone";
    public static final String KEY_ADDR_USER = "Address";
    public static final String KEY_PIC_USER = "Profile_Picture";
    public static final String KEY_DATE_USER = "Register_Date";

    public static final String TABLE_POST = "post";
    public static final String KEY_ID_POST = "Id_Post";
    public static final String KEY_TITLE_POST = "Title";
    public static final String KEY_CONT_POST = "Content";
    public static final String KEY_FAVS_POST = "Favorites";
    public static final String KEY_DRAFT_POST = "Is_Draft";
    public static final String KEY_DATE_POST = "Posted_Date";

    public static final String TABLE_REPLY = "reply";
    public static final String KEY_ID_REPLY = "Id_Reply";
    public static final String KEY_CONT_REPLY = "Content";
    public static final String KEY_DATE_REPLY = "Replied_Date";

    public static final String TABLE_FAV = "favorites";
    public static final String KEY_ID_FAV = "Id_Fav";

    public static final String TABLE_POST_IMG = "post_images";
    public static final String KEY_ID_PIMG = "Id_P_Img";
    public static final String KEY_IMG_PIMG = "Image";

    public static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME_USER + " VARCHAR, "
            + KEY_EMAIL_USER + " VARCHAR, "
            + KEY_PASS_USER + " VARCHAR, "
            + KEY_PHONE_USER + " BIGINT, "
            + KEY_ADDR_USER + " VARCHAR, "
            + KEY_PIC_USER + " LONGBLOB, "
            + KEY_DATE_USER + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    public static final String CREATE_TABLE_POST = "CREATE TABLE " + TABLE_POST + "("
            + KEY_ID_POST + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE_POST + " VARCHAR, "
            + KEY_CONT_POST + " VARCHAR, "
            + KEY_FAVS_POST + " SMALLINT, "
            + KEY_DRAFT_POST + " BOOLEAN DEFAULT 0, "
            + KEY_DATE_POST + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + KEY_ID_USER + " SMALLINT, "
            + "FOREIGN KEY (" + KEY_ID_USER + ") REFERENCES " + TABLE_USER + "(" + KEY_ID_USER + "));";

    public static final String CREATE_TABLE_REPLY = "CREATE TABLE " + TABLE_REPLY + "("
            + KEY_ID_REPLY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CONT_REPLY + " VARCHAR, "
            + KEY_DATE_REPLY + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + KEY_ID_USER + " SMALLINT, "
            + KEY_ID_POST + " SMALLINT, "
            + "FOREIGN KEY (" + KEY_ID_USER + ") REFERENCES " + TABLE_USER + "(" + KEY_ID_USER + "), "
            + "FOREIGN KEY (" + KEY_ID_POST + ") REFERENCES " + TABLE_POST + "(" + KEY_ID_POST + "));";

    public static final String CREATE_TABLE_FAV = "CREATE TABLE " + TABLE_FAV + "("
            + KEY_ID_FAV + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ID_USER + " SMALLINT, "
            + KEY_ID_POST + " SMALLINT, "
            + "FOREIGN KEY (" + KEY_ID_USER + ") REFERENCES " + TABLE_USER + "(" + KEY_ID_USER + "), "
            + "FOREIGN KEY (" + KEY_ID_POST + ") REFERENCES " + TABLE_POST + "(" + KEY_ID_POST + "));";

    public static final String CREATE_TABLE_PIMG = "CREATE TABLE " + TABLE_POST_IMG + "("
            + KEY_ID_PIMG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_IMG_PIMG + " LONGBLOB, "
            + KEY_ID_POST + " SMALLINT, "
            + "FOREIGN KEY (" + KEY_ID_POST + ") REFERENCES " + TABLE_POST + "(" + KEY_ID_POST + "));";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_POST);
        db.execSQL(CREATE_TABLE_REPLY);
        db.execSQL(CREATE_TABLE_FAV);
        db.execSQL(CREATE_TABLE_PIMG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPLY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST_IMG);

        onCreate(db);
    }

    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_USER, user.getName());
        values.put(KEY_EMAIL_USER, user.getEmail());
        values.put(KEY_PASS_USER, user.getPassword());
        values.put(KEY_PHONE_USER, user.getPhone());
        values.put(KEY_ADDR_USER, user.getAddress());
        values.put(KEY_PIC_USER, user.getProfile_picture());

        long user_id = db.insert(TABLE_USER, null, values);

        return user_id;
    }

    public User readUser(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ID_USER + " = " + user_id;

        Cursor c = db.rawQuery(query, null);

        if (c != null)
            c.moveToFirst();

        User user = new User();
        user.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));
        user.setName(c.getString(c.getColumnIndexOrThrow(KEY_NAME_USER)));
        user.setEmail(c.getString(c.getColumnIndexOrThrow(KEY_EMAIL_USER)));
        user.setPassword(c.getString(c.getColumnIndexOrThrow(KEY_PASS_USER)));
        user.setPhone(c.getInt(c.getColumnIndexOrThrow(KEY_PHONE_USER)));
        user.setAddress(c.getString(c.getColumnIndexOrThrow(KEY_ADDR_USER)));
        user.setProfile_picture(c.getBlob(c.getColumnIndexOrThrow(KEY_PIC_USER)));
        user.setRegister_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_USER)));

        return user;
    }

}

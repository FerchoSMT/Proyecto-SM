package com.psm.proyecto_sm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.psm.proyecto_sm.models.Favorites;
import com.psm.proyecto_sm.models.Post;
import com.psm.proyecto_sm.models.Reply;
import com.psm.proyecto_sm.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bd_buzztalk";
    private static final int DB_VERSION = 8;

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
    public static final String KEY_DELETED_POST = "Is_Deleted";
    public static final String KEY_IMG1_POST = "Image1";
    public static final String KEY_IMG2_POST = "Image2";

    public static final String TABLE_REPLY = "reply";
    public static final String KEY_ID_REPLY = "Id_Reply";
    public static final String KEY_CONT_REPLY = "Content";
    public static final String KEY_DATE_REPLY = "Replied_Date";
    public static final String KEY_DELETED_REPLY = "Is_Deleted";

    public static final String TABLE_FAV = "favorites";
    public static final String KEY_ID_FAV = "Id_Fav";
    public static final String KEY_FAV_FAV = "Favorite";

    public static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME_USER + " VARCHAR, "
            + KEY_EMAIL_USER + " VARCHAR, "
            + KEY_PASS_USER + " VARCHAR, "
            + KEY_PHONE_USER + " VARCHAR, "
            + KEY_ADDR_USER + " VARCHAR, "
            + KEY_PIC_USER + " LONGBLOB, "
            + KEY_DATE_USER + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    public static final String CREATE_TABLE_POST = "CREATE TABLE " + TABLE_POST + "("
            + KEY_ID_POST + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE_POST + " VARCHAR, "
            + KEY_CONT_POST + " VARCHAR, "
            + KEY_FAVS_POST + " INTEGER DEFAULT 0, "
            + KEY_DRAFT_POST + " BOOLEAN DEFAULT 0, "
            + KEY_DATE_POST + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + KEY_DELETED_POST + " BOOLEAN DEFAULT 0, "
            + KEY_IMG1_POST + " LONGBLOB, "
            + KEY_IMG2_POST + " LONGBLOB, "
            + KEY_ID_USER + " INTEGER, "
            + "FOREIGN KEY (" + KEY_ID_USER + ") REFERENCES " + TABLE_USER + "(" + KEY_ID_USER + "));";

    public static final String CREATE_TABLE_REPLY = "CREATE TABLE " + TABLE_REPLY + "("
            + KEY_ID_REPLY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CONT_REPLY + " VARCHAR, "
            + KEY_DATE_REPLY + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + KEY_DELETED_REPLY + " BOOLEAN DEFAULT 0, "
            + KEY_ID_USER + " INTEGER, "
            + KEY_ID_POST + " INTEGER, "
            + "FOREIGN KEY (" + KEY_ID_USER + ") REFERENCES " + TABLE_USER + "(" + KEY_ID_USER + "), "
            + "FOREIGN KEY (" + KEY_ID_POST + ") REFERENCES " + TABLE_POST + "(" + KEY_ID_POST + "));";

    public static final String CREATE_TABLE_FAV = "CREATE TABLE " + TABLE_FAV + "("
            + KEY_ID_FAV + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_FAV_FAV + " BOOLEAN DEFAULT 0, "
            + KEY_ID_USER + " INTEGER, "
            + KEY_ID_POST + " INTEGER, "
            + "FOREIGN KEY (" + KEY_ID_USER + ") REFERENCES " + TABLE_USER + "(" + KEY_ID_USER + "), "
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPLY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);

        onCreate(db);
    }

    //guardar users al hacer login
    public void getUsersFromDbHost(List<User> users) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean userExists;

        for (int i = 0; i < users.size(); i++) {

            userExists = false;

            ContentValues values = new ContentValues();
            values.put(KEY_ID_USER, users.get(i).getId_user());
            values.put(KEY_NAME_USER, users.get(i).getName());
            values.put(KEY_EMAIL_USER, users.get(i).getEmail());
            values.put(KEY_PASS_USER, users.get(i).getPassword());
            values.put(KEY_PHONE_USER, users.get(i).getPhone());
            values.put(KEY_ADDR_USER, users.get(i).getAddress());
            values.put(KEY_PIC_USER, users.get(i).getProfile_picture());
            values.put(KEY_DATE_USER, users.get(i).getRegister_date());

            String query = "SELECT " + KEY_ID_USER + " FROM " + TABLE_USER + " WHERE " + KEY_ID_USER + " = " + users.get(i).getId_user();
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                userExists = true;
            }

            if (!userExists) {
                db.insert(TABLE_USER, null, values);
            }
            else {
                String[] args = new String[]{users.get(i).getId_user().toString()};
                db.update(TABLE_USER, values, KEY_ID_USER + "=?", args);
            }

            c.close();

        }
    }

    //guardar posts al entrar a main y perfil
    public void getPostsFromDbHost(List<Post> posts) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean postExists;

        for (int i = 0; i < posts.size(); i++) {

            postExists = false;

            ContentValues values = new ContentValues();
            values.put(KEY_ID_POST, posts.get(i).getId_post());
            values.put(KEY_TITLE_POST, posts.get(i).getTitle());
            values.put(KEY_CONT_POST, posts.get(i).getContent());
            values.put(KEY_FAVS_POST, posts.get(i).getFavorites());
            values.put(KEY_DRAFT_POST, posts.get(i).is_draft());
            values.put(KEY_DATE_POST, posts.get(i).getPosted_date());
            values.put(KEY_DELETED_POST, posts.get(i).is_deleted());
            values.put(KEY_IMG1_POST, posts.get(i).getImageA());
            values.put(KEY_IMG2_POST, posts.get(i).getImageB());
            values.put(KEY_ID_USER, posts.get(i).getId_user());

            String query = "SELECT " + KEY_ID_POST + " FROM " + TABLE_POST + " WHERE " + KEY_ID_POST + " = " + posts.get(i).getId_post();
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                postExists = true;
            }

            if (!postExists) {
                db.insert(TABLE_POST, null, values);
            }
            else {
                String[] args = new String[]{posts.get(i).getId_post().toString()};
                db.update(TABLE_POST, values, KEY_ID_POST + "=?", args);
            }

            c.close();

        }
    }

    //guardar replies al entrar a perfil
    public void getRepliesFromDbHost(List<Reply> replies) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean replyExists;

        for (int i = 0; i < replies.size(); i++) {

            replyExists = false;

            ContentValues values = new ContentValues();
            values.put(KEY_ID_REPLY, replies.get(i).getId_reply());
            values.put(KEY_CONT_REPLY, replies.get(i).getContent());
            values.put(KEY_DATE_REPLY, replies.get(i).getReplied_date());
            values.put(KEY_DELETED_REPLY, replies.get(i).is_deleted());
            values.put(KEY_ID_USER, replies.get(i).getId_user());
            values.put(KEY_ID_POST, replies.get(i).getId_post());

            String query = "SELECT " + KEY_ID_REPLY + " FROM " + TABLE_REPLY + " WHERE " + KEY_ID_REPLY + " = " + replies.get(i).getId_reply();
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                replyExists = true;
            }

            if (!replyExists) {
                db.insert(TABLE_REPLY, null, values);
            }
            else {
                String[] args = new String[]{replies.get(i).getId_reply().toString()};
                db.update(TABLE_REPLY, values, KEY_ID_REPLY + "=?", args);
            }

            c.close();

        }
    }

    //guardar favs al entrar a perfil
    public void getFavoritesFromDbHost(List<Favorites> favorites) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean favExists;

        for (int i = 0; i < favorites.size(); i++) {

            favExists = false;

            ContentValues values = new ContentValues();
            values.put(KEY_ID_FAV, favorites.get(i).getId_fav());
            values.put(KEY_FAV_FAV, favorites.get(i).getFavorite());
            values.put(KEY_ID_USER, favorites.get(i).getId_user());
            values.put(KEY_ID_POST, favorites.get(i).getId_post());

            String query = "SELECT " + KEY_ID_FAV + " FROM " + TABLE_FAV + " WHERE " + KEY_ID_FAV + " = " + favorites.get(i).getId_fav();
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                favExists = true;
            }

            if (!favExists) {
                db.insert(TABLE_FAV, null, values);
            }
            else {
                String[] args = new String[]{favorites.get(i).getId_fav().toString()};
                db.update(TABLE_FAV, values, KEY_ID_FAV + "=?", args);
            }

            c.close();

        }
    }

    //obtener informacion del usuario loggeado y mostrarla en perfil
    public User readUser(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ID_USER + " = " + user_id;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        User userAux = new User();
        userAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));
        userAux.setName(c.getString(c.getColumnIndexOrThrow(KEY_NAME_USER)));
        userAux.setEmail(c.getString(c.getColumnIndexOrThrow(KEY_EMAIL_USER)));
        userAux.setPassword(c.getString(c.getColumnIndexOrThrow(KEY_PASS_USER)));
        userAux.setPhone(c.getString(c.getColumnIndexOrThrow(KEY_PHONE_USER)));
        userAux.setAddress(c.getString(c.getColumnIndexOrThrow(KEY_ADDR_USER)));
        userAux.setProfile_picture(c.getBlob(c.getColumnIndexOrThrow(KEY_PIC_USER)));
        userAux.setRegister_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_USER)));

        c.close();
        return userAux;
    }

    //hacer login con los usuarios previamente guardados
    public User loginUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        User userAux = new User();

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " +
                KEY_EMAIL_USER + " = '" + user.getEmail() + "' AND " +
                KEY_PASS_USER + " = '" + user.getPassword() + "'";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            userAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));
            userAux.setProfile_picture(c.getBlob(c.getColumnIndexOrThrow(KEY_PIC_USER)));
        }

        c.close();
        return userAux;
    }

    //obtener todos los posts previamente guardados
    public List<Post> readAllPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_DRAFT_POST + " = 0 AND " + KEY_DELETED_POST + " = 0 " +
                "ORDER BY " + KEY_DATE_POST + " DESC";

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Post postAux = new Post();
            postAux.setId_post(c.getLong(c.getColumnIndexOrThrow(KEY_ID_POST)));
            postAux.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_TITLE_POST)));
            postAux.setContent(c.getString(c.getColumnIndexOrThrow(KEY_CONT_POST)));
            postAux.setFavorites(c.getInt(c.getColumnIndexOrThrow(KEY_FAVS_POST)));
            postAux.set_draft(c.getInt(c.getColumnIndexOrThrow(KEY_DRAFT_POST)) != 0);
            postAux.setPosted_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_POST)));
            postAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));

            postAux.setImageA(c.getBlob(c.getColumnIndexOrThrow(KEY_IMG1_POST)));
            postAux.setImageB(c.getBlob(c.getColumnIndexOrThrow(KEY_IMG2_POST)));

            String tQuery = "SELECT " + KEY_PIC_USER + " FROM " + TABLE_USER +
                    " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
            Cursor tc = db.rawQuery(tQuery, null);
            tc.moveToFirst();
            postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

            tc.close();
            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

    //obtener los posts del usuario previamente guardados
    public List<Post> readUserPosts(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_DRAFT_POST + " = 0 AND " + KEY_DELETED_POST + " = 0 AND " +
                KEY_ID_USER + " = " + user_id + " ORDER BY " + KEY_DATE_POST + " DESC";

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Post postAux = new Post();
            postAux.setId_post(c.getLong(c.getColumnIndexOrThrow(KEY_ID_POST)));
            postAux.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_TITLE_POST)));
            postAux.setContent(c.getString(c.getColumnIndexOrThrow(KEY_CONT_POST)));
            postAux.setFavorites(c.getInt(c.getColumnIndexOrThrow(KEY_FAVS_POST)));
            postAux.set_draft(c.getInt(c.getColumnIndexOrThrow(KEY_DRAFT_POST)) != 0);
            postAux.setPosted_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_POST)));
            postAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));

            postAux.setImageA(c.getBlob(c.getColumnIndexOrThrow(KEY_IMG1_POST)));
            postAux.setImageB(c.getBlob(c.getColumnIndexOrThrow(KEY_IMG2_POST)));

            String tQuery = "SELECT " + KEY_PIC_USER + " FROM " + TABLE_USER +
                    " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
            Cursor tc = db.rawQuery(tQuery, null);
            tc.moveToFirst();
            postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

            tc.close();
            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

    //obtener los posts likeados por el usuario previamente guardados
    public List<Post> readUserFavorites(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " p INNER JOIN " + TABLE_FAV + " f ON f." +
                KEY_ID_POST + " = p." + KEY_ID_POST + " AND f." + KEY_FAV_FAV + " = 1" +
                " WHERE p." + KEY_DRAFT_POST + " = 0 AND p." + KEY_DELETED_POST + " = 0 AND f." +
                KEY_ID_USER + " = " + user_id + " ORDER BY p." + KEY_DATE_POST + " DESC";

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Post postAux = new Post();
            postAux.setId_post(c.getLong(c.getColumnIndexOrThrow(KEY_ID_POST)));
            postAux.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_TITLE_POST)));
            postAux.setContent(c.getString(c.getColumnIndexOrThrow(KEY_CONT_POST)));
            postAux.setFavorites(c.getInt(c.getColumnIndexOrThrow(KEY_FAVS_POST)));
            postAux.set_draft(c.getInt(c.getColumnIndexOrThrow(KEY_DRAFT_POST)) != 0);
            postAux.setPosted_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_POST)));
            postAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));

            postAux.setImageA(c.getBlob(c.getColumnIndexOrThrow(KEY_IMG1_POST)));
            postAux.setImageB(c.getBlob(c.getColumnIndexOrThrow(KEY_IMG2_POST)));

            String tQuery = "SELECT " + KEY_PIC_USER + " FROM " + TABLE_USER +
                    " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
            Cursor tc = db.rawQuery(tQuery, null);
            tc.moveToFirst();
            postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

            tc.close();
            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

    //obtener los drafts del usuario previamente guardados
    public List<Post> readUserDraftPosts(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_DRAFT_POST + " = 1 AND " +
                KEY_ID_USER + " = " + user_id + " ORDER BY " + KEY_DATE_POST + " DESC";

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Post postAux = new Post();
            postAux.setId_post(c.getLong(c.getColumnIndexOrThrow(KEY_ID_POST)));
            postAux.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_TITLE_POST)));
            postAux.setContent(c.getString(c.getColumnIndexOrThrow(KEY_CONT_POST)));
            postAux.setFavorites(c.getInt(c.getColumnIndexOrThrow(KEY_FAVS_POST)));
            postAux.set_draft(c.getInt(c.getColumnIndexOrThrow(KEY_DRAFT_POST)) != 0);
            postAux.setPosted_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_POST)));
            postAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));

            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

    //obtener los replies del usuario previamente guardados
    public List<Reply> readUserReplies(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Reply> listReplies = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_REPLY + " WHERE " + KEY_DELETED_REPLY + " = 0 AND " +
                KEY_ID_USER + " = " + user_id + " ORDER BY " + KEY_DATE_REPLY + " DESC";

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Reply replyAux = new Reply();
            replyAux.setId_reply(c.getLong(c.getColumnIndexOrThrow(KEY_ID_REPLY)));
            replyAux.setContent(c.getString(c.getColumnIndexOrThrow(KEY_CONT_REPLY)));
            replyAux.setReplied_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_REPLY)));
            replyAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));
            replyAux.setId_post(c.getLong(c.getColumnIndexOrThrow(KEY_ID_POST)));

            String sQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ID_USER + " = " + replyAux.getId_user();
            Cursor sc = db.rawQuery(sQuery, null);
            sc.moveToFirst();
            replyAux.setName_user(sc.getString(sc.getColumnIndexOrThrow(KEY_NAME_USER)));
            replyAux.setImg_user(sc.getBlob(sc.getColumnIndexOrThrow(KEY_PIC_USER)));

            sc.close();
            listReplies.add(replyAux);
        }

        c.close();
        return listReplies;
    }
}
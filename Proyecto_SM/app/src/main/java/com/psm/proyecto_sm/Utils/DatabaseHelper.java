package com.psm.proyecto_sm.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.psm.proyecto_sm.models.Post;
import com.psm.proyecto_sm.models.Reply;
import com.psm.proyecto_sm.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bd_buzztalk";
    private static final int DB_VERSION = 4;

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
    public static final String KEY_FAV_FAV = "Favorite";

    public static final String TABLE_POST_IMG = "post_images";
    public static final String KEY_ID_PIMG = "Id_P_Img";
    public static final String KEY_IMG_PIMG = "Image";

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
            + KEY_ID_USER + " INTEGER, "
            + "FOREIGN KEY (" + KEY_ID_USER + ") REFERENCES " + TABLE_USER + "(" + KEY_ID_USER + "));";

    public static final String CREATE_TABLE_REPLY = "CREATE TABLE " + TABLE_REPLY + "("
            + KEY_ID_REPLY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CONT_REPLY + " VARCHAR, "
            + KEY_DATE_REPLY + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
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

    public static final String CREATE_TABLE_PIMG = "CREATE TABLE " + TABLE_POST_IMG + "("
            + KEY_ID_PIMG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_IMG_PIMG + " LONGBLOB, "
            + KEY_ID_POST + " INTEGER, "
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
        c.moveToFirst();

        User user = new User();
        user.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));
        user.setName(c.getString(c.getColumnIndexOrThrow(KEY_NAME_USER)));
        user.setEmail(c.getString(c.getColumnIndexOrThrow(KEY_EMAIL_USER)));
        user.setPassword(c.getString(c.getColumnIndexOrThrow(KEY_PASS_USER)));
        user.setPhone(c.getString(c.getColumnIndexOrThrow(KEY_PHONE_USER)));
        user.setAddress(c.getString(c.getColumnIndexOrThrow(KEY_ADDR_USER)));
        user.setProfile_picture(c.getBlob(c.getColumnIndexOrThrow(KEY_PIC_USER)));
        user.setRegister_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_USER)));

        c.close();
        return user;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_USER, user.getName());
        values.put(KEY_EMAIL_USER, user.getEmail());
        values.put(KEY_PASS_USER, user.getPassword());
        values.put(KEY_PHONE_USER, user.getPhone());
        values.put(KEY_ADDR_USER, user.getAddress());
        values.put(KEY_PIC_USER, user.getProfile_picture());

        String[] args = new String[]{user.getId_user().toString()};
        db.update(TABLE_USER, values, KEY_ID_USER + "=?", args);
    }

    public boolean validEmailUser(String email, long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean valid = true;

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_EMAIL_USER + " = '" + email + "'";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            long qId = c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER));
            if (id != qId) { valid = false; }
        }

        c.close();
        return valid;
    }

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

    public long createPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_POST, post.getTitle());
        values.put(KEY_CONT_POST, post.getContent());
        values.put(KEY_ID_USER, post.getId_user());

        long post_id = db.insert(TABLE_POST, null, values);

        List<byte[]> images = post.getImages();

        for (int i = 0; i < images.size(); i++) {
            ContentValues val = new ContentValues();
            val.put(KEY_IMG_PIMG, images.get(i));
            val.put(KEY_ID_POST, post_id);

            db.insert(TABLE_POST_IMG, null, val);
        }

        return post_id;
    }

    public void createDraftPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_POST, post.getTitle());
        values.put(KEY_CONT_POST, post.getContent());
        values.put(KEY_DRAFT_POST, 1);
        values.put(KEY_ID_USER, post.getId_user());

        long post_id = db.insert(TABLE_POST, null, values);

        List<byte[]> images = post.getImages();

        for (int i = 0; i < images.size(); i++) {
            ContentValues val = new ContentValues();
            val.put(KEY_IMG_PIMG, images.get(i));
            val.put(KEY_ID_POST, post_id);

            db.insert(TABLE_POST_IMG, null, val);
        }
    }

    public Post readPost(long post_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_ID_POST + " = " + post_id;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Post postAux = new Post();
        postAux.setId_post(c.getLong(c.getColumnIndexOrThrow(KEY_ID_POST)));
        postAux.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_TITLE_POST)));
        postAux.setContent(c.getString(c.getColumnIndexOrThrow(KEY_CONT_POST)));
        postAux.setFavorites(c.getInt(c.getColumnIndexOrThrow(KEY_FAVS_POST)));
        postAux.set_draft(c.getInt(c.getColumnIndexOrThrow(KEY_DRAFT_POST)) != 0);
        postAux.setPosted_date(c.getString(c.getColumnIndexOrThrow(KEY_DATE_POST)));
        postAux.setId_user(c.getLong(c.getColumnIndexOrThrow(KEY_ID_USER)));

        List<byte[]> imgs = new ArrayList<>();
        String sQuery = "SELECT " + KEY_IMG_PIMG + " FROM " + TABLE_POST_IMG +
                " WHERE " + KEY_ID_POST + " = " + postAux.getId_post();
        Cursor sc = db.rawQuery(sQuery, null);
        while (sc.moveToNext()) {
            byte[] imgAux = sc.getBlob(sc.getColumnIndexOrThrow(KEY_IMG_PIMG));
            imgs.add(imgAux);
        }
        postAux.setImages(imgs);

        String tQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
        Cursor tc = db.rawQuery(tQuery, null);
        tc.moveToFirst();
        postAux.setName_user(tc.getString(tc.getColumnIndexOrThrow(KEY_NAME_USER)));
        postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

        c.close();
        sc.close();
        tc.close();
        return postAux;
    }

    public List<Post> readAllPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_DRAFT_POST + " = 0 " +
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

            List<byte[]> imgs = new ArrayList<>();
            String sQuery = "SELECT " + KEY_IMG_PIMG + " FROM " + TABLE_POST_IMG +
                    " WHERE " + KEY_ID_POST + " = " + postAux.getId_post();
            Cursor sc = db.rawQuery(sQuery, null);
            while (sc.moveToNext()) {
                byte[] imgAux = sc.getBlob(sc.getColumnIndexOrThrow(KEY_IMG_PIMG));
                imgs.add(imgAux);
            }
            postAux.setImages(imgs);

            String tQuery = "SELECT " + KEY_PIC_USER + " FROM " + TABLE_USER +
                    " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
            Cursor tc = db.rawQuery(tQuery, null);
            tc.moveToFirst();
            postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

            sc.close();
            tc.close();
            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

    public List<Post> readUserPosts(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_DRAFT_POST + " = 0 AND " +
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

            List<byte[]> imgs = new ArrayList<>();
            String sQuery = "SELECT " + KEY_IMG_PIMG + " FROM " + TABLE_POST_IMG +
                    " WHERE " + KEY_ID_POST + " = " + postAux.getId_post();
            Cursor sc = db.rawQuery(sQuery, null);
            while (sc.moveToNext()) {
                byte[] imgAux = sc.getBlob(sc.getColumnIndexOrThrow(KEY_IMG_PIMG));
                imgs.add(imgAux);
            }
            postAux.setImages(imgs);

            String tQuery = "SELECT " + KEY_PIC_USER + " FROM " + TABLE_USER +
                    " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
            Cursor tc = db.rawQuery(tQuery, null);
            tc.moveToFirst();
            postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

            sc.close();
            tc.close();
            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

    public List<Post> readUserFavorites(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " p INNER JOIN " + TABLE_FAV + " f ON f." +
                KEY_ID_POST + " = p." + KEY_ID_POST + " AND f." + KEY_FAV_FAV + " = 1" +
                " WHERE p." + KEY_DRAFT_POST + " = 0 AND f." + KEY_ID_USER + " = " + user_id +
                " ORDER BY p." + KEY_DATE_POST + " DESC";

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

            List<byte[]> imgs = new ArrayList<>();
            String sQuery = "SELECT " + KEY_IMG_PIMG + " FROM " + TABLE_POST_IMG +
                    " WHERE " + KEY_ID_POST + " = " + postAux.getId_post();
            Cursor sc = db.rawQuery(sQuery, null);
            while (sc.moveToNext()) {
                byte[] imgAux = sc.getBlob(sc.getColumnIndexOrThrow(KEY_IMG_PIMG));
                imgs.add(imgAux);
            }
            postAux.setImages(imgs);

            String tQuery = "SELECT " + KEY_PIC_USER + " FROM " + TABLE_USER +
                    " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
            Cursor tc = db.rawQuery(tQuery, null);
            tc.moveToFirst();
            postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

            sc.close();
            tc.close();
            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

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

    public void updatePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_POST, post.getTitle());
        values.put(KEY_CONT_POST, post.getContent());

        String[] args = new String[]{post.getId_post().toString()};
        db.update(TABLE_POST, values, KEY_ID_POST + "=?", args);

        db.delete(TABLE_POST_IMG, KEY_ID_POST + "=?", args);

        List<byte[]> images = post.getImages();

        for (int i = 0; i < images.size(); i++) {
            ContentValues val = new ContentValues();
            val.put(KEY_IMG_PIMG, images.get(i));
            val.put(KEY_ID_POST, post.getId_post());

            db.insert(TABLE_POST_IMG, null, val);
        }
    }

    public void updateDraftPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_POST, post.getTitle());
        values.put(KEY_CONT_POST, post.getContent());
        values.put(KEY_DRAFT_POST, 0);

        String[] args = new String[]{post.getId_post().toString()};
        db.update(TABLE_POST, values, KEY_ID_POST + "=?", args);

        db.delete(TABLE_POST_IMG, KEY_ID_POST + "=?", args);

        List<byte[]> images = post.getImages();

        for (int i = 0; i < images.size(); i++) {
            ContentValues val = new ContentValues();
            val.put(KEY_IMG_PIMG, images.get(i));
            val.put(KEY_ID_POST, post.getId_post());

            db.insert(TABLE_POST_IMG, null, val);
        }
    }

    public void deletePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] args = new String[]{post.getId_post().toString()};
        db.delete(TABLE_POST_IMG, KEY_ID_POST + "=?", args);
        db.delete(TABLE_FAV, KEY_ID_POST + "=?", args);
        db.delete(TABLE_REPLY, KEY_ID_POST + "=?", args);
        db.delete(TABLE_POST, KEY_ID_POST + "=?", args);
    }

    public List<Post> searchPosts(String search) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Post> listPosts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_DRAFT_POST + " = 0 AND " +
                KEY_TITLE_POST + " LIKE '%" + search + "%' OR " +
                KEY_CONT_POST + " LIKE '%" + search + "%' " +
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

            List<byte[]> imgs = new ArrayList<>();
            String sQuery = "SELECT " + KEY_IMG_PIMG + " FROM " + TABLE_POST_IMG +
                    " WHERE " + KEY_ID_POST + " = " + postAux.getId_post();
            Cursor sc = db.rawQuery(sQuery, null);
            while (sc.moveToNext()) {
                byte[] imgAux = sc.getBlob(sc.getColumnIndexOrThrow(KEY_IMG_PIMG));
                imgs.add(imgAux);
            }
            postAux.setImages(imgs);

            String tQuery = "SELECT " + KEY_PIC_USER + " FROM " + TABLE_USER +
                    " WHERE " + KEY_ID_USER + " = " + postAux.getId_user();
            Cursor tc = db.rawQuery(tQuery, null);
            tc.moveToFirst();
            postAux.setImg_user(tc.getBlob(tc.getColumnIndexOrThrow(KEY_PIC_USER)));

            sc.close();
            tc.close();
            listPosts.add(postAux);
        }

        c.close();
        return listPosts;
    }

    public boolean favePost(long user_id, long post_id) {
        SQLiteDatabase dbR = this.getReadableDatabase();
        SQLiteDatabase dbW = this.getWritableDatabase();
        boolean faved = false;

        String query = "SELECT * FROM " + TABLE_FAV + " WHERE " + KEY_ID_USER + " = " +
                user_id + " AND " + KEY_ID_POST + " = " + post_id;

        Cursor c = dbR.rawQuery(query, null);
        if (c.moveToFirst()) {
            ContentValues values = new ContentValues();
            long id_fav = c.getLong(c.getColumnIndexOrThrow(KEY_ID_FAV));
            int fav = c.getInt(c.getColumnIndexOrThrow(KEY_FAV_FAV));

            if (fav == 0) {
                values.put(KEY_FAV_FAV, 1);

                String[] args = new String[]{String.valueOf(id_fav)};
                dbW.update(TABLE_FAV, values, KEY_ID_FAV + "=?", args);

                faved = true;
            }
            else {
                values.put(KEY_FAV_FAV, 0);

                String[] args = new String[]{String.valueOf(id_fav)};
                dbW.update(TABLE_FAV, values, KEY_ID_FAV + "=?", args);

                faved = false;
            }
        }
        else {
            ContentValues values = new ContentValues();
            values.put(KEY_FAV_FAV, 1);
            values.put(KEY_ID_USER, user_id);
            values.put(KEY_ID_POST, post_id);

            dbW.insert(TABLE_FAV, null, values);
            faved = true;
        }

        String sQuery = "SELECT * FROM " + TABLE_POST + " WHERE " + KEY_ID_POST + " = " + post_id;
        Cursor sc = dbR.rawQuery(sQuery, null);
        sc.moveToFirst();
        int favs = sc.getInt(sc.getColumnIndexOrThrow(KEY_FAVS_POST));
        if (faved) {
            ContentValues values = new ContentValues();
            values.put(KEY_FAVS_POST, favs + 1);

            String[] args = new String[]{String.valueOf(post_id)};
            dbW.update(TABLE_POST, values, KEY_ID_POST + "=?", args);
        }
        else {
            ContentValues values = new ContentValues();
            values.put(KEY_FAVS_POST, favs - 1);

            String[] args = new String[]{String.valueOf(post_id)};
            dbW.update(TABLE_POST, values, KEY_ID_POST + "=?", args);
        }

        c.close();
        sc.close();
        return faved;
    }

    public long createReply(Reply reply) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONT_REPLY, reply.getContent());
        values.put(KEY_ID_USER, reply.getId_user());
        values.put(KEY_ID_POST, reply.getId_post());

        long reply_id = db.insert(TABLE_REPLY, null, values);

        return reply_id;
    }

    public List<Reply> readPostReplies(long post_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Reply> listReplies = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_REPLY + " WHERE " + KEY_ID_POST + " = " + post_id +
                " ORDER BY " + KEY_DATE_REPLY + " ASC";

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

    public List<Reply> readUserReplies(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Reply> listReplies = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_REPLY + " WHERE " + KEY_ID_USER + " = " + user_id +
                " ORDER BY " + KEY_DATE_REPLY + " DESC";

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

    public void updateReply(Reply reply) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONT_REPLY, reply.getContent());

        String[] args = new String[]{reply.getId_reply().toString()};
        db.update(TABLE_REPLY, values, KEY_ID_REPLY + "=?", args);
    }

    public void deleteReply(Reply reply) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] args = new String[]{reply.getId_reply().toString()};
        db.delete(TABLE_REPLY, KEY_ID_REPLY + "=?", args);
    }
}
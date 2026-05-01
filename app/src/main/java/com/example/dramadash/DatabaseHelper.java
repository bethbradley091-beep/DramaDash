package com.example.dramadash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DramaDash.db";
    private static final int DATABASE_VERSION = 2;

    // USERS TABLE
    private static final String TABLE_USERS = "users";

    private static final String COL_ID = "id";
    private static final String COL_STUDENT_NAME = "student_name";
    private static final String COL_PARENT_NAME = "parent_name";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";
    private static final String COL_GROUP_NAME = "group_name";

    // ANNOUNCEMENTS TABLE
    private static final String TABLE_ANNOUNCEMENTS = "announcements";

    private static final String COL_ANNOUNCEMENT_ID = "announcement_id";
    private static final String COL_ANNOUNCEMENT_GROUP = "group_name";
    private static final String COL_TITLE = "title";
    private static final String COL_MESSAGE = "message";
    private static final String COL_DATE = "date_posted";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_STUDENT_NAME + " TEXT, "
                + COL_PARENT_NAME + " TEXT, "
                + COL_USERNAME + " TEXT UNIQUE, "
                + COL_PASSWORD + " TEXT, "
                + COL_ROLE + " TEXT, "
                + COL_GROUP_NAME + " TEXT)";

        String createAnnouncementsTable = "CREATE TABLE " + TABLE_ANNOUNCEMENTS + " ("
                + COL_ANNOUNCEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ANNOUNCEMENT_GROUP + " TEXT, "
                + COL_TITLE + " TEXT, "
                + COL_MESSAGE + " TEXT, "
                + COL_DATE + " TEXT)";

        db.execSQL(createUsersTable);
        db.execSQL(createAnnouncementsTable);

        // Default admin account
        ContentValues adminValues = new ContentValues();
        adminValues.put(COL_STUDENT_NAME, "Admin");
        adminValues.put(COL_PARENT_NAME, "Admin");
        adminValues.put(COL_USERNAME, "admin");
        adminValues.put(COL_PASSWORD, hashPassword("admin123"));
        adminValues.put(COL_ROLE, "admin");
        adminValues.put(COL_GROUP_NAME, "All Groups");
        db.insert(TABLE_USERS, null, adminValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANNOUNCEMENTS);
        onCreate(db);
    }

    // USER METHODS
    public boolean insertUser(String studentName, String parentName, String username, String password, String role, String groupName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_STUDENT_NAME, studentName);
        values.put(COL_PARENT_NAME, parentName);
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);
        values.put(COL_GROUP_NAME, groupName);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ?",
                new String[]{username}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{username, password}
        );

        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public String getUserRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_ROLE + " FROM " + TABLE_USERS
                        + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{username, password}
        );

        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }
        cursor.close();
        return role;
    }

    public String getUserGroup(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_GROUP_NAME + " FROM " + TABLE_USERS
                        + " WHERE " + COL_USERNAME + " = ?",
                new String[]{username}
        );

        String groupName = null;
        if (cursor.moveToFirst()) {
            groupName = cursor.getString(0);
        }
        cursor.close();
        return groupName;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " ORDER BY " + COL_ID + " DESC", null);
    }

    // ANNOUNCEMENT METHODS
    public boolean insertAnnouncement(String groupName, String title, String message, String datePosted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ANNOUNCEMENT_GROUP, groupName);
        values.put(COL_TITLE, title);
        values.put(COL_MESSAGE, message);
        values.put(COL_DATE, datePosted);

        long result = db.insert(TABLE_ANNOUNCEMENTS, null, values);
        return result != -1;
    }

    public Cursor getAnnouncementsByGroup(String groupName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_ANNOUNCEMENTS
                        + " WHERE " + COL_ANNOUNCEMENT_GROUP + " = ?"
                        + " ORDER BY " + COL_ANNOUNCEMENT_ID + " DESC",
                new String[]{groupName}
        );
    }

    public Cursor getAllAnnouncements() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_ANNOUNCEMENTS
                        + " ORDER BY " + COL_ANNOUNCEMENT_ID + " DESC",
                null
        );
    }

    public boolean updateAnnouncement(int announcementId, String groupName, String title, String message, String datePosted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ANNOUNCEMENT_GROUP, groupName);
        values.put(COL_TITLE, title);
        values.put(COL_MESSAGE, message);
        values.put(COL_DATE, datePosted);

        int result = db.update(
                TABLE_ANNOUNCEMENTS,
                values,
                COL_ANNOUNCEMENT_ID + " = ?",
                new String[]{String.valueOf(announcementId)}
        );

        return result > 0;
    }

    public boolean deleteAnnouncement(int announcementId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(
                TABLE_ANNOUNCEMENTS,
                COL_ANNOUNCEMENT_ID + " = ?",
                new String[]{String.valueOf(announcementId)}
        );

        return result > 0;
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
package se.miun.mova1701.dt031g.dialer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String CALLS_DATABASE_NAME = "Calls1.db";
    private static final String CALLS_TABLE_NAME = "calls";
    private static final String CALLS_COLUMN_ID = "id";
    private static final String CALLS_COLUMN_PHONE_NUMBER = "phone_number";
    private static final String CALLS_COLUMN_DATE= "date";
    private static final String CALLS_COLUMN_POSITION = "position";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CALLS_TABLE_NAME + " (" +
                    CALLS_COLUMN_ID + " INTEGER PRIMARY KEY," +
                    CALLS_COLUMN_PHONE_NUMBER + " TEXT," +
                    CALLS_COLUMN_DATE + " TEXT," +
                    CALLS_COLUMN_POSITION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CALLS_TABLE_NAME;

    private SQLiteDatabase dbHelper;


    public boolean insertCall(String phoneNumber, String date, String position) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone_number", phoneNumber);
        contentValues.put("date", date);
        contentValues.put("position", position);
        dbHelper.insert(CALLS_TABLE_NAME, null, contentValues);
        close();
        return true;
    }

    public int numberOfRows(){
        open();
        int numberOfQueries = 0;
        try {
             numberOfQueries = (int) DatabaseUtils.queryNumEntries(dbHelper, CALLS_COLUMN_ID);
        }
        catch(Exception e) {
            return 0;
        }
        close();
        return numberOfQueries;
    }

    public ArrayList<HashMap<String, String>> getCalls() {
        open();
        ArrayList<HashMap<String, String>> callsList = new ArrayList<>();
        String query = "SELECT phone_number, date, position FROM " + CALLS_TABLE_NAME + " ORDER BY date DESC";
        Cursor cursor = dbHelper.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("phone_number", cursor.getString(cursor.getColumnIndex(CALLS_COLUMN_PHONE_NUMBER)));
            user.put("date", cursor.getString(cursor.getColumnIndex(CALLS_COLUMN_DATE)));
            user.put("position", cursor.getString(cursor.getColumnIndex(CALLS_COLUMN_POSITION)));
            callsList.add(user);
        }
        cursor.close();
        close();
        return callsList;
    }

    public DatabaseHandler(Context context) {
        super(context, CALLS_DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void open() {
        dbHelper = this.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

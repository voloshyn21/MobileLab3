package ua.lpnu.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLFeatureNotSupportedException;
import java.util.Vector;
import java.lang.String;

public class LocalDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "apple_db";
    private static final String ITEM_TABLE = "apple";
    private static final String ITEM_NAME_COL = "name";
    private static final String ITEMS_TABLE_CREATE_SQL = "CREATE TABLE " + ITEM_TABLE + " ( " + ITEM_NAME_COL + " TEXT )";

    LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ITEMS_TABLE_CREATE_SQL);

        RandomGenerationService r = new RandomGenerationService();

        for (int i = 0; i < 10; ++i) {
            addItem(db, r.next());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: implement when several database versions are available
    }

    public long addItem(SQLiteDatabase db, String item_name) {
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME_COL, item_name);
        return db.insert(ITEM_TABLE, null, values);
    }

    public long addItem(String item_name) {
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME_COL, item_name);
        return getWritableDatabase().insert(ITEM_TABLE, null, values);
    }

    public Vector<String> getItems() {
        Vector<String> ret = new Vector<String>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + ITEM_TABLE, null);

        try {
            for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()) {
                ret.add(new String(cursor.getString(0)));
            }
        } finally {
            cursor.close();
        }
        return ret;
    }

    public void deleteItem(String item_name) {
        getWritableDatabase().delete(ITEM_TABLE, ITEM_NAME_COL + " = ?", new String[]{item_name});
    }
}


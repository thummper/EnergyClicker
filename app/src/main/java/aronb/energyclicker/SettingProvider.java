package aronb.energyclicker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Aron on 04/05/2018.
 */

public class SettingProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher;
    private static final int SETTINGS_TYPE_LIST = 1;
    private static final int SETTINGS_TYPE_ONE = 2;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(SettingsDBData.AUTHORITY, "settings", SETTINGS_TYPE_LIST);
        sUriMatcher.addURI(SettingsDBData.AUTHORITY, "settings/#", SETTINGS_TYPE_ONE);
    }
    private static HashMap<String, String> SettingsProjMap;
    static{
        SettingsProjMap = new HashMap<String, String>();
        SettingsProjMap.put(SettingsDBData.SettingsTable.ID, SettingsDBData.SettingsTable.ID);
        SettingsProjMap.put(SettingsDBData.SettingsTable.SETTING, SettingsDBData.SettingsTable.SETTING);
        SettingsProjMap.put(SettingsDBData.SettingsTable.VALUE, SettingsDBData.SettingsTable.VALUE);

    }




    private static class SettingsDBHelper extends SQLiteOpenHelper{

        public SettingsDBHelper(Context c){
            super(c, SettingsDBData.DATABASE_NAME, null, SettingsDBData.DATABASE_VERSION);
        }

        private static final String SQL_CREATE = "CREATE TABLE " + SettingsDBData.SettingsTable.TABLE_NAME + " (" +
            SettingsDBData.SettingsTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SettingsDBData.SettingsTable.SETTING + " TEXT NOT NULL, " +
            SettingsDBData.SettingsTable.VALUE + " TEXT NOT NULL);";

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE);

            //After database made input default settings?
            ContentValues vals = new ContentValues();
            vals.put(SettingsDBData.SettingsTable.SETTING, "Notification");
            vals.put(SettingsDBData.SettingsTable.VALUE, "On");
            db.insert(SettingsDBData.SettingsTable.TABLE_NAME, null, vals);





        }

        private static final String SQL_DROP = "DROP TABLE IF EXISTS " + SettingsDBData.SettingsTable.TABLE_NAME + ";";

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
            db.execSQL(SQL_DROP);
            onCreate(db);
        }


    }
    private SettingsDBHelper helper;

    @Override
    public boolean onCreate(){
        helper = new SettingsDBHelper(getContext());
        return false;
    }


    @Override
    public int delete(Uri uri, String where, String[] whereArgs){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = 0;

        switch(sUriMatcher.match(uri)){
            case SETTINGS_TYPE_LIST:
                count = db.delete(SettingsDBData.SettingsTable.TABLE_NAME, where, whereArgs);
                break;

            case SETTINGS_TYPE_ONE:
                String rowID = uri.getPathSegments().get(1);
                //No idea what this does lol
                count = db.delete(SettingsDBData.SettingsTable.TABLE_NAME, SettingsDBData.SettingsTable.ID + "=" + rowID + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri){
        switch (sUriMatcher.match(uri)){
            case SETTINGS_TYPE_LIST:
                return SettingsDBData.CONTENT_TYPE_ARTICLES_LIST;
            case SETTINGS_TYPE_ONE:
                return SettingsDBData.CONTENT_TYPE_ARTICLE_ONE;

                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        if(sUriMatcher.match(uri) != SETTINGS_TYPE_LIST){
            throw new IllegalArgumentException("[Insert](01)Unkown URI: " + uri);
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        long rowID = db.insert(SettingsDBData.SettingsTable.TABLE_NAME, null, values);
        if(rowID > 0){
            Uri settingsUri = ContentUris.withAppendedId(SettingsDBData.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(settingsUri, null);
            return settingsUri;
        }
        throw new IllegalArgumentException("[INSERT](02)Unknown URI: " + uri);
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)){
            case SETTINGS_TYPE_LIST:
                builder.setTables(SettingsDBData.SettingsTable.TABLE_NAME);
                builder.setProjectionMap(SettingsProjMap);
                break;

            case SETTINGS_TYPE_ONE:
                builder.setTables(SettingsDBData.SettingsTable.TABLE_NAME);
                builder.setProjectionMap(SettingsProjMap);
                builder.appendWhere(SettingsDBData.SettingsTable.ID + " = " + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor queryCursor = builder.query(db, projection, selection, selectionArgs, null,null,null);
        queryCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return queryCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs){

        SQLiteDatabase db = helper.getWritableDatabase();
        int count = 0;
        switch(sUriMatcher.match(uri)){
            case SETTINGS_TYPE_LIST:
                count = db.update(SettingsDBData.SettingsTable.TABLE_NAME, values, where, whereArgs);
                break;

            case SETTINGS_TYPE_ONE:
                String rowID = uri.getPathSegments().get(1);
                count = db.update(SettingsDBData.SettingsTable.TABLE_NAME, values, SettingsDBData.SettingsTable.ID + " = " + rowID +
                        (!TextUtils.isEmpty(where) ? " AND (" + ")" : ""), whereArgs);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}

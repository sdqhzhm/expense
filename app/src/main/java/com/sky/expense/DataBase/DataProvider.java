package com.sky.expense.DataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by sky on 2014/5/8.
 */
public class DataProvider extends ContentProvider {

    private static final UriMatcher matcher;
    private DBHelper helper;
    private SQLiteDatabase db;

    private static final String AUTHORITY = "com.sky.expense.DataProvider";
    public static final int TRANSACTION_ALL = 0;
    public static final int TRANSACTION_ONE = 1;

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.sky.transaction";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.sky.transaction";

    //数据改变后立即重新查询
    private static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/transactions");

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/transactions");

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, "transactions", TRANSACTION_ALL);   //匹配记录集合
        matcher.addURI(AUTHORITY, "transactions/#", TRANSACTION_ONE); //匹配单条记录
    }

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int match = matcher.match(uri);
        switch (match) {
            case TRANSACTION_ALL:
                return CONTENT_TYPE;
            case TRANSACTION_ONE:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = helper.getReadableDatabase();
        int match = matcher.match(uri);
        switch (match) {
            case TRANSACTION_ALL:
                //doesn't need any code in my provider.
                break;
            case TRANSACTION_ONE:
                long _id = ContentUris.parseId(uri);
                selection = "_id = ?";
                selectionArgs = new String[]{String.valueOf(_id)};
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return db.query("transactions", projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = matcher.match(uri);
        db = helper.getWritableDatabase();
        if (values == null) {
            values = new ContentValues();
            //TODO 在没有默认数据的情况下怎么办？
            return null;
        }
        long rowId = db.insert("transactions", null, values);
        if (rowId > 0) {
            notifyDataChanged();
            return ContentUris.withAppendedId(uri, rowId);
        }
        return null;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    //通知指定URI数据已改变
    private void notifyDataChanged() {
        getContext().getContentResolver().notifyChange(NOTIFY_URI, null);
    }
}

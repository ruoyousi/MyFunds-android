package com.ruoyousi.common.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSession {

	protected Context mContext = null;
	protected SQLiteDatabase mDb = null;
	protected SQLiteOpenHelper mHelper = null;

	public DatabaseSession(Context _context, final String dbName,
			final int dbVersion, final String[] dropTables,
			final String[] ddlSQL) {
		mContext = _context;

		mHelper = new SQLiteOpenHelper(mContext, dbName, null, dbVersion) {

			@Override
			public void onCreate(SQLiteDatabase db) {
				for (String createTable : ddlSQL) {
					db.execSQL(createTable);
				}
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				for (String tableName : dropTables) {
					db.execSQL("DROP TABLE " + tableName);
				}
				onCreate(db);
			}

		};

		mDb = mHelper.getWritableDatabase();

	}

	public String getDBPath() {
		return mDb.getPath();
	}

	public void close() {
		mHelper.close();
	}

	public int queryScalar(String sql, String[] selectionArgs, int defaultValue) {
		int intResult = defaultValue;
		Cursor cur = mDb.rawQuery(sql, selectionArgs);
		cur.moveToFirst();
		if (!cur.isAfterLast()) {
			intResult = cur.getInt(0);
		}
		cur.close();
		return intResult;
	}

	public List<String> queryRowsToList(String sql, String[] selectionArgs) {
		Cursor cur = mDb.rawQuery(sql, selectionArgs);

		List<String> listResult = new ArrayList<String>();

		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			listResult.add(cur.getString(0));
			cur.moveToNext();
		}
		cur.close();

		return listResult;
	}

	public Map<String, String> queryRowToMap(String sql, String[] selectionArgs) {
		Cursor cur = mDb.rawQuery(sql, selectionArgs);

		Map<String, String> mapResult = new HashMap<String, String>();

		cur.moveToFirst();
		if (!cur.isAfterLast()) {
			for (int i = 0, j = cur.getColumnCount(); i < j; i++) {
				mapResult.put(cur.getColumnName(i), cur.getString(0));
			}
		}
		cur.close();

		return mapResult;
	}

	public long insert(String table, ContentValues values) {
		return mDb.insert(table, null, values);
	}

	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		return mDb.update(table, values, whereClause, whereArgs);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		return mDb.delete(table, whereClause, whereArgs);
	}
	
	public void queryCursor (String table, String[] columns, String selection, String[] selectionArgs, 
			String groupBy, String having, String orderBy, RowHandler handler) {
		Cursor cur = mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			handler.handle(new Row(cur));
			cur.moveToNext();
		}
		cur.close();
	}

}

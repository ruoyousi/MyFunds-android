package com.ruoyousi.common.db;

import android.database.Cursor;

public class Row {

	private Cursor cur;
	
	public Row(Cursor cur) {
	  this.cur = cur;	
	}
	
	public String getString (int columnIndex) {
		return cur.getString(columnIndex);
	}
	
	public double getDouble (int columnIndex) {
		return cur.getDouble(columnIndex);
	}

}

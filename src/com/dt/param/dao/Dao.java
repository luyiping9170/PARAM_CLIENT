package com.dt.param.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Dao {
	
	/// 数据库名称
	private static final String DB_NAME = "PARAM_DB.db";

	/// 消息表
	private static final String MSG_TABLE_NAME = "MESSAGES";
	private static final String MSG_TABLE_STUC = "CREATE TABLE IF NOT EXISTS "+ MSG_TABLE_NAME +"("+
												 " _id INTEGER NOT NULL," +
												 " _from INTEGER NOT NULL," +
												 " _to varchar(20) NOT NULL," +
												 " _time DATE NOT NULL," +
												 " _content TEXT NOT NULL," +
												 " PRIMARY KEY(_id)" +
												 " );";
	
	/// 联系人表
	private static final String CONTACTS_TABLE_NAME  = "CONTACTS";
	private static final String CONTACTS_TABLE_STRUC = "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE_NAME + "(" +
													   " _id INTEGER NOT NULL," +
													   " _number varchar(20) NOT NULL," +
													   " _name varchar(20) NOT NULL," +
													   " _icon BLOB," +
													   " _msg INTEGER NOT NULL DEFAULT 0," +
													   " _call INTEGER NOT NULL DEFAULT 0," +
													   " PRIMARY KEY(_id)" +
													   " );";
	private static SQLiteDatabase db;
	
	public Dao(Context context){
		db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null);
		initDB();
	}
	
	public void initDB(){
		
		db.execSQL(MSG_TABLE_STUC);
		db.execSQL(CONTACTS_TABLE_STRUC);
		
	}
	
}

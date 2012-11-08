package com.dt.param.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Dao {
	
	/// database name
	private static final String DB_NAME = "PARAM_DB.db";

	/// message table
	private static final String MSG_TABLE_NAME = "MESSAGES";
	private static final String MSG_TABLE_STUC = "CREATE TABLE IF NOT EXISTS "+ MSG_TABLE_NAME +"("+
												 " _id INTEGER NOT NULL," +
												 " _account varchar(20) NOT NULL," +
												 " _from INTEGER NOT NULL," +
												 " _to varchar(20) NOT NULL," +
												 " _time DATE NOT NULL," +
												 " _content TEXT NOT NULL," +
												 " PRIMARY KEY(_id)" +
												 " );";
	
	/// 
	private static final String CONTACTS_TABLE_NAME  = "CONTACTS";
	private static final String CONTACTS_TABLE_STRUC = "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE_NAME + "(" +
													   " _id INTEGER NOT NULL," +
													   " _account varchar(20) NOT NULL," +
													   " _number varchar(20) NOT NULL," +
													   " _name varchar(20) NOT NULL," +
													   " _icon BLOB," +
													   " _msg INTEGER NOT NULL DEFAULT 0," +
													   " _call INTEGER NOT NULL DEFAULT 0," +
													   " PRIMARY KEY(_id)" +
													   " );";
	
	private static final String ACCOUNTS_TABLE_NAME  = "ACCOUNTS";
	private static final String ACCOUNTS_TABLE_STRUC = "CREATE TABLE IF NOT EXISTS " + ACCOUNTS_TABLE_NAME +"(" +
													   " _id INTEGER NOT NULL, " +
													   " _account varchar(20) NOT NULL," +
													   " _password varchar(20) NOT NULL," +
													   " PRIMARY KEY(_id)"+
													   " );"; 
	
	private static SQLiteDatabase db;
	private static Dao singledao;
	
	private Dao(Context context){
		db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null);
		initDB();
	}
	
	public static Dao instance(Context context){
		if(singledao == null)
			singledao = new Dao(context);
		return singledao;
	}
	
	public void initDB(){
		db.execSQL(MSG_TABLE_STUC);
		db.execSQL(CONTACTS_TABLE_STRUC);
		db.execSQL(ACCOUNTS_TABLE_STRUC);
	}
	
	public void finish(){
		db.close();
	}
	
	/**
	 * Insert an account into the account database
	 * @param username
	 * @param passwords
	 */
	public void addAccounts(String username,String password){
//		ContentValues cv = new ContentValues();
//		cv.put("_account", username);
//		cv.put("_password", password);
//		db.insertOrThrow(ACCOUNTS_TABLE_NAME, null, cv);
	}
	
	public void clearAccountPassword(String username){
		
	}
	
	
	
}

package net.shoppier.library;

import java.util.ArrayList;
import java.util.HashMap;

import net.shoppier.Lists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ShoppierDB";

	// Table Names
	private static final String TABLE_LOGIN = "user";
	private static final String TABLE_LIST = "list";

	// user Table Columns names
	private static final String KEY_ID = "user_id";
	private static final String KEY_NAME = "user_name";
	private static final String KEY_EMAIL = "user_email";
	private static final String KEY_UID = "user_password_hash";
	
	
	// list Table Columns names
	private static final String KEY_LIST_ID = "list_id";
	private static final String KEY_LIST_TEXT = "list_text";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
		
		String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LIST + "("
				+ KEY_LIST_ID + " INTEGER PRIMARY KEY," + KEY_LIST_TEXT + " TEXT" + ")";
		db.execSQL(CREATE_LIST_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
		
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, uid); // UId

		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}
	
	public void addItemToListDB(Lists list){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(KEY_LIST_ID, list.getListsItemID()); 
		values.put(KEY_LIST_TEXT, list.getListsItem());

		//insert row 
		db.insert(TABLE_LIST, null, values);
	}
	
	public int removeItemFromList(String listID, String listText){
		SQLiteDatabase db = this.getWritableDatabase();
		int num_rows_Deleted = db.delete(TABLE_LIST, KEY_LIST_ID + " =? AND "+ KEY_LIST_TEXT + " =?", 
				new String[]{String.valueOf(listID),String.valueOf(listText)});
		Log.e("# of rows deleted - ", Integer.toString(num_rows_Deleted));
		return num_rows_Deleted;
	}
	
	public ArrayList<Lists> getList(){
		ArrayList<Lists> alllist = new ArrayList<Lists>();
		
		String selectQuery = "SELECT  * FROM " + TABLE_LIST;
		Log.e("getList", selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	    if (c.moveToFirst()) {
	        do {
	            Lists li = new Lists();
	            li.setListsItemID(c.getString(c.getColumnIndex(KEY_LIST_ID))); 
	            li.setListItem(c.getString(c.getColumnIndex(KEY_LIST_TEXT))); 
	 
	            // adding to final list
	            alllist.add(li);
	        } while (c.moveToNext());
	    }
	 
	    return alllist;
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
		}
		cursor.close();
		db.close();
		// return user
		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount(String tableName) {
		String countQuery = "SELECT  * FROM " + tableName;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void resetTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.delete(TABLE_LIST, null, null);
		db.close();
	}


}
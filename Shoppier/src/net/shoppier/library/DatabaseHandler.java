package net.shoppier.library;

import java.util.ArrayList;
import java.util.HashMap;

import net.shoppier.CompleteList;
import net.shoppier.ListsItem;
import net.shoppier.SearchableItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ShoppierDB";

	// Table Names
	private static final String TABLE_LOGIN = "user";
	private static final String TABLE_LIST_ITEMS = "list";
	private static final String TABLE_ITEM = "item"; 
	private static final String TABLE_LIST_IDS = "listId";
	//private static final String TABLE_

	// user Table Columns names
	private static final String KEY_ID = "user_id";
	private static final String KEY_NAME = "user_name";
	private static final String KEY_EMAIL = "user_email";
	private static final String KEY_UID = "user_password_hash";
	
	
	// list_items Table Columns names
	private static final String KEY_LIST_ID_PK = "list_item_id";
	private static final String KEY_LISTITEM_NAME = "list_item_name";
	private static final String KEY_LIST_SEARCH_ITEM_FK = "list_search_item_fk";
	private static final String KEY_LIST_ITEM_LISTFK = "list_item_listfk";
	private static final String KEY_LIST_ITEM_BRAND = "list_item_brand";
	
	//item Table Column names 
	private static final String KEY_ITEM_PK = "item_pk";
	private static final String KEY_ITEM_NAME = "item_name";
	private static final String KEY_ITEM_BRAND = "item_brand";
	private static final String KEY_ITEM_CAT = "item_cat";
	
	//List ID Column Names
	private static final String KEY_LIST_NAME = "List_Name";
	private static final String KEY_LIST_ID = "List_ID";
	
	
	//Query to create login table
	String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
			+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT" + ")";
	
	//Query to create list_items table
	String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LIST_ITEMS + "("
			+ KEY_LIST_ID_PK + " INTEGER PRIMARY KEY, " + KEY_LISTITEM_NAME + " TEXT, " + KEY_LIST_SEARCH_ITEM_FK +
			" TEXT, " + KEY_LIST_ITEM_LISTFK + " TEXT, " + KEY_LIST_ITEM_BRAND + " TEXT " + ")";
	
	//Query to create item table
	String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
			+ KEY_ITEM_PK + " INTEGER PRIMARY KEY," 
			+ KEY_ITEM_NAME + " TEXT," + KEY_ITEM_CAT + " TEXT," + KEY_ITEM_BRAND + " TEXT" + ")";
	
	//Query to create Table of List ID's
		String CREATE_LISTIDS_TABLE = "CREATE TABLE " + TABLE_LIST_IDS + "("
				+ KEY_LIST_ID + " INTEGER PRIMARY KEY," 
				+ KEY_LIST_NAME + " TEXT " + ")";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_LOGIN_TABLE);

		db.execSQL(CREATE_LIST_TABLE);
		
		db.execSQL(CREATE_ITEMS_TABLE);
		
		db.execSQL(CREATE_LISTIDS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
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
	 * Getting table count  
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
	 * Adding a List item to the database
	 * */
	public int  addItemToListDB(ListsItem list){
		int result; 
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(KEY_LIST_SEARCH_ITEM_FK, list.getSearchItemId()); 
		values.put(KEY_LISTITEM_NAME, list.getListsItemName());
		values.put(KEY_LIST_ITEM_LISTFK, list.getListFK());
		values.put(KEY_LIST_ITEM_BRAND, list.getListItemBrand());

		//insert row 
		result = (int) db.insert(TABLE_LIST_ITEMS, null, values);
		list.setListsItemID(result);
		db.close(); 
		return result; 
	}
	
	/**
	 * Removing a List item from the database
	 * */
	public int removeItemFromList(int listID){
		//TODO Fix deleteing newly added items
		SQLiteDatabase db = this.getWritableDatabase();
		int num_rows_Deleted = db.delete(TABLE_LIST_ITEMS, KEY_LIST_ID_PK + " =?", 
				new String[]{String.valueOf(listID)});
		Log.e("# of rows deleted - ", Integer.toString(num_rows_Deleted));
		db.close();
		return num_rows_Deleted;
	}
	
	/**
	 * Fetch the whole grocery list stored in the database. 
	 * */
	public ArrayList<ListsItem> getList(String listID){
		ArrayList<ListsItem> alllist = new ArrayList<ListsItem>();
		
		String selectQuery = "SELECT  * FROM " + TABLE_LIST_ITEMS + " WHERE " + KEY_LIST_ITEM_LISTFK + " = " + listID;
		//Log.e("getList", selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	    if (c.moveToFirst()) {
	        do {
	            ListsItem li = new ListsItem();
	            String tempListFk = c.getString(c.getColumnIndex(KEY_LIST_ITEM_LISTFK));
	            String tempSearchItemID = c.getString(c.getColumnIndex(KEY_LIST_SEARCH_ITEM_FK));
	            String tempListItemID = c.getString(c.getColumnIndex(KEY_LIST_ID_PK));
	            
	            li.setListFK(Integer.parseInt(tempListFk));
	            li.setListsItemName(c.getString(c.getColumnIndex(KEY_LISTITEM_NAME)));
	            li.setListsItemID(Integer.parseInt(tempListItemID));
	            li.setSearchItemId(Integer.parseInt(tempSearchItemID));
	            li.setListItemBrand(c.getString(c.getColumnIndex(KEY_LIST_ITEM_BRAND)));
	            // adding to final list
	            alllist.add(li);
	        } while (c.moveToNext());
	    }
	    db.close();
	    return alllist;
	}
	
	
	
	/**
	 * Edit a List item in the grocery list.  
	 * */
	public int editListItem(ListsItem listItem){
		ContentValues value = new ContentValues();
		value.put(KEY_LISTITEM_NAME, listItem.getListsItemID());
				
		SQLiteDatabase db = this.getWritableDatabase();
		
		int toReturn = db.update(TABLE_LIST_ITEMS, value, KEY_LIST_ID_PK + " = ?",
				new String[]{String.valueOf(listItem.getListsItemID())});
		 db.close();
		 return toReturn;
		
	}
	
	/**
	 * Fill CrowdSoured item database table.  
	 * */
	
	//TODO Fill CrowdSoured item database table.
	
	/**
	 * Get all CrowdSourced items from the database.  
	 * */
	
	public ArrayList<SearchableItem> getAllSearchableItems(){
		SQLiteDatabase db = this.getReadableDatabase(); 
		ArrayList<SearchableItem> items = new ArrayList<SearchableItem>();
		
		String selectQuery = "SELECT * FROM " + TABLE_ITEM; 
		Log.e("getSearchItems", selectQuery);
		
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	    if (c.moveToFirst()) {
	        do {
	        	SearchableItem li = new SearchableItem();

	            li.setItemBrand(c.getString(c.getColumnIndex(KEY_ITEM_BRAND)));
	            li.setItemCat(c.getString(c.getColumnIndex(KEY_ITEM_CAT)));
	            li.setItemID(Integer.parseInt(c.getString(c.getColumnIndex(KEY_ITEM_PK))));
	            li.setItemName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
	            // adding to final list
	            items.add(li);
	        } while (c.moveToNext());
	    }
	    db.close();
	    return items;
		
	}
	
	public Cursor getSearhableItemMatches(String query, String[] columns){
		
		 String selection =  KEY_ITEM_NAME + " MATCH ?";
		    String[] selectionArgs = new String[] {query+"*"};

		    return query(selection, selectionArgs, columns);
		
	}
	
	private Cursor query(String selection, String[] selectionArgs, String[] columns){
		 SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		 SQLiteDatabase db = this.getReadableDatabase();
		    builder.setTables(TABLE_ITEM);

		    Cursor cursor = builder.query(db,
		            columns, selection, selectionArgs, null, null, null);

		    if (cursor == null) {
		        return null;
		    } else if (!cursor.moveToFirst()) {
		        cursor.close();
		        return null;
		    }
		    db.close();
		    return cursor;
	}
	
	/**
	 * Add a CrowdSoured item in the database.  
	 * */
	public void addSearchableItem(SearchableItem item){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_ITEM_BRAND, item.getItemBrand());
		values.put(KEY_ITEM_CAT, item.getItemCat());
		values.put(KEY_ITEM_NAME, item.getItemName());
		values.put(KEY_ITEM_PK, item.getItemID());

		//insert row 
		db.insert(TABLE_ITEM, null, values);
		 db.close();
	}

	/**
	 * Edit a CrowdSoured item in the database.  
	 * */
	
	//TODO Edit a CrowdSoured item in the database.

	/**
	 * Add List to ListID Table  
	 * */
	public void addListID(CompleteList list){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_LIST_NAME, list.getListName());
		values.put(KEY_LIST_ID, list.getListPK());

		//insert row 
		db.insert(TABLE_LIST_IDS, null, values);
		db.close();
	}
	
	public ArrayList<CompleteList> getList(){
		 SQLiteDatabase db = this.getReadableDatabase(); 
			ArrayList<CompleteList> list = new ArrayList<CompleteList>();
			
			String selectQuery = "SELECT * FROM " + TABLE_LIST_IDS; 
			Log.e("getSearchItems", selectQuery);
			
		    Cursor c = db.rawQuery(selectQuery, null);
		    
		    if (c.moveToFirst()) {
		        do {
		        	CompleteList li = new CompleteList();
		            li.setListName(c.getString(c.getColumnIndex(KEY_LIST_NAME)));
		            li.setListPK(Integer.parseInt(c.getString(c.getColumnIndex(KEY_LIST_ID))));
		            // adding to final list
		            list.add(li);
		        } while (c.moveToNext());
		    }
		    db.close();
		    return list;
		
	}
	
	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void resetTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Tables
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_IDS);
		// Create tables again
		onCreate(db);
		db.close();
	}
	
	public void clearItemTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
		db.execSQL(CREATE_ITEMS_TABLE);
		 db.close();
	}
	
	public void DBclose(){
		 SQLiteDatabase db = this.getReadableDatabase();
	        if (db != null && db.isOpen()){
	            db.close();
	        }
	}


}

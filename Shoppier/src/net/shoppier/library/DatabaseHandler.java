package net.shoppier.library;

import java.util.ArrayList;
import java.util.HashMap;

import net.shoppier.AisleObject;
import net.shoppier.CategoryObject;
import net.shoppier.CompleteList;
import net.shoppier.ListsItem;
import net.shoppier.SearchableItem;
import net.shoppier.StoreObject;

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
	private static final String TABLE_STORE = "store";
	private static final String TABLE_AISLE = "aisle";
	private static final String TABLE_CAT = "category";
	//private static final String TABLE_
	
	//aisle Table Column Names 
	private static final String KEY_AISLE_PK = "aisle_pk";
	private static final String KEY_AISLE_NAME = "aisle_name";
	private static final String KEY_AISLE_STRFK = "aisle_STRFK";
	
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
	private static final String KEY_LIST_ITEM_qty = "list_item_qty";
	private static final String KEY_LIST_ITEM_Price = "list_item_price";
	private static final String KEY_LIST_ITEM_XCOR = "list_item_x";
	private static final String KEY_LIST_ITEM_YCOR = "list_item_y";
	private static final String KEY_LIST_CATFK= "list_item_catfk";
	
	
	//item Table Column names 
	private static final String KEY_ITEM_PK = "item_pk";
	private static final String KEY_ITEM_NAME = "item_name";
	private static final String KEY_ITEM_BRAND = "item_brand";
	private static final String KEY_ITEM_CAT = "item_cat";
	
	//List ID Column Names
	private static final String KEY_LIST_NAME = "List_Name";
	private static final String KEY_LIST_ID = "List_ID";
	private static final String KEY_LIST_STOREFK = "list_storefk";
	
	//Store Column Names
	private static final String KEY_storePK       = "storePK";
	private static final String KEY_storeName     = "storeName";
	private static final String KEY_storeAddress  = "storeAddress";
	private static final String KEY_storeCity     = "storeCity";
	private static final String KEY_storeZipCode  = "storeZipCode";
	private static final String KEY_storeType     = "storeType";
	private static final String KEY_storeImage    = "storeImage";
	
	//Category Column Names
	private static final String KEYcat_pk     = "cat_pk";
	private static final String KEYcat_locfk  = "cat_locfk";
	private static final String KEYcat_name   = "cat_name";
	private static final String KEYcat_value  = "cat_value"; 
	private static final String KEYcat_x      = "cat_x";
	private static final String KEYcat_y      = "cat_y"; 
	private static final String Key_CAT_LOCAL_PK = "cat_PK_LOCAL";
	
	
	//Query to create cat table
			String CREATE_CAT_TABLE = "CREATE TABLE " + TABLE_CAT + "(" + Key_CAT_LOCAL_PK + " INTEGER PRIMARY KEY, "
					+ KEYcat_pk + " INTEGER, " + KEYcat_name + " TEXT, "
					+ KEYcat_value + " INTEGER, " + KEYcat_x + " TEXT, "  
					+ KEYcat_y + " TEXT, "  + KEYcat_locfk + " INTEGER )";
			
	//Query to create aisle table
		String CREATE_AISLE_TABLE = "CREATE TABLE " + TABLE_AISLE + "("
				+ KEY_AISLE_PK + " INTEGER PRIMARY KEY," + KEY_AISLE_NAME + " TEXT,"
				+ KEY_AISLE_STRFK + " INTEGER " + ")";

	//Query to create Store Table 
	String CREATE_STORE_TABLE = "CREATE TABLE " + TABLE_STORE + "("
					+ KEY_storePK + " INTEGER PRIMARY KEY," 
					+ KEY_storeName + " TEXT, " + KEY_storeAddress + " TEXT, " 
					+ KEY_storeCity + " TEXT, " + KEY_storeImage + " TEXT, " 
					+ KEY_storeZipCode + " INTEGER, " + KEY_storeType + " TEXT" + ")";
	
	
	//Query to create login table
	String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
			+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT" + ")";
	
	//Query to create list_items table
	String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LIST_ITEMS + "("
			+ KEY_LIST_ID_PK + " INTEGER PRIMARY KEY, " + KEY_LISTITEM_NAME + " TEXT, " + KEY_LIST_SEARCH_ITEM_FK +
			" TEXT, " + KEY_LIST_ITEM_LISTFK + " TEXT, " + KEY_LIST_ITEM_BRAND + " TEXT, " 
			+ KEY_LIST_ITEM_qty + " TEXT, " + KEY_LIST_ITEM_YCOR + " NUMERIC, " +
			KEY_LIST_ITEM_XCOR + " NUMERIC, " + KEY_LIST_CATFK + " NUMERIC, " + 
			KEY_LIST_ITEM_Price + " NUMERIC " + ")";
	
	//Query to create item table
	String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
			+ KEY_ITEM_PK + " INTEGER PRIMARY KEY," 
			+ KEY_ITEM_NAME + " TEXT," + KEY_ITEM_CAT + " TEXT," + KEY_ITEM_BRAND + " TEXT" + ")";
	
	//Query to create Table of List ID's
		String CREATE_LISTIDS_TABLE = "CREATE TABLE " + TABLE_LIST_IDS + "("
				+ KEY_LIST_ID + " INTEGER PRIMARY KEY, "  + KEY_LIST_STOREFK + " INTEGER, "
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
		
		db.execSQL(CREATE_STORE_TABLE);
		
		db.execSQL(CREATE_AISLE_TABLE);
		
		db.execSQL(CREATE_CAT_TABLE);
		
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
		values.put(KEY_LIST_ITEM_Price, list.getItemPrice());
		values.put(KEY_LIST_ITEM_qty, list.getItemQTY());
		values.put(KEY_LIST_ITEM_XCOR, list.getxCord());
		values.put(KEY_LIST_CATFK, list.getCatFK());
		values.put(KEY_LIST_ITEM_YCOR, list.getyCord());
		//insert row 
		result = (int) db.insert(TABLE_LIST_ITEMS, null, values);
		list.setListsItemID(result);
		db.close(); 
		return result; 
	}
	
	public ListsItem getListItem(String listItemPK){
		String selectQuery = "SELECT  * FROM " + TABLE_LIST_ITEMS + " WHERE " + KEY_LIST_ID_PK + " = " + listItemPK;
		//Log.e("getList", selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	    ListsItem li = new ListsItem();
	    if (c.moveToFirst()) {
	        do {
	           
	            String tempListFk = c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_LISTFK));
	            String tempSearchItemID = c.getString(c.getColumnIndexOrThrow(KEY_LIST_SEARCH_ITEM_FK));
	            String tempListItemID = c.getString(c.getColumnIndexOrThrow(KEY_LIST_ID_PK));
	            String tempXCor = c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_XCOR));
	            String tempYCor =c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_YCOR));
	            String catFK =c.getString(c.getColumnIndexOrThrow(KEY_LIST_CATFK));
	            
	            li.setListFK(Integer.parseInt(tempListFk));
	            li.setListsItemName(c.getString(c.getColumnIndexOrThrow(KEY_LISTITEM_NAME)));
	            li.setListsItemID(Integer.parseInt(tempListItemID));
	            li.setSearchItemId(Integer.parseInt(tempSearchItemID));
	            li.setListItemBrand(c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_BRAND)));
	            li.setItemQTY(c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_qty)));
	            li.setItemPrice(c.getDouble(c.getColumnIndexOrThrow(KEY_LIST_ITEM_Price)));
	            li.setxCord(Integer.parseInt(tempXCor));
	            li.setyCord(Integer.parseInt(tempYCor));
	            li.setCatFK(Integer.parseInt(catFK));
	            // adding to final list
	        } while (c.moveToNext());
	    }
	    db.close();
	    c.close();
	    
	    return li;
	}
	
	/**
	 * Removing a List item from the database
	 * */
	public int removeItemFromList(int listID){
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
		
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	    c.moveToFirst();
		   if(c.getCount() != 0){
	        do {
	            ListsItem li = new ListsItem();
	            String tempListFk = c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_LISTFK));
	            String tempSearchItemID = c.getString(c.getColumnIndexOrThrow(KEY_LIST_SEARCH_ITEM_FK));
	            String tempListItemID = c.getString(c.getColumnIndexOrThrow(KEY_LIST_ID_PK));
	            
	            li.setListFK(Integer.parseInt(tempListFk));
	            li.setListsItemName(c.getString(c.getColumnIndexOrThrow(KEY_LISTITEM_NAME)));
	            li.setListsItemID(Integer.parseInt(tempListItemID));
	            li.setSearchItemId(Integer.parseInt(tempSearchItemID));
	            li.setListItemBrand(c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_BRAND)));
	            li.setItemQTY(c.getString(c.getColumnIndexOrThrow(KEY_LIST_ITEM_qty)));
	            li.setItemPrice(c.getDouble((c.getColumnIndexOrThrow(KEY_LIST_ITEM_Price))));
	            // adding to final list
	            alllist.add(li);
	        } while (c.moveToNext());
		   }
	    db.close();
	    c.close();
	    return alllist;
	}
	
	
	
	/**
	 * Edit a List item in the grocery list.  
	 * */
	public int editListItem(ListsItem listItem){
		ContentValues value = new ContentValues();
		value.put(KEY_LISTITEM_NAME, listItem.getListsItemName());
		value.put(KEY_LIST_ITEM_BRAND, listItem.getListItemBrand());
		value.put(KEY_LIST_ITEM_qty, listItem.getItemQTY());
		value.put(KEY_LIST_ITEM_Price, listItem.getItemPrice());
				
		SQLiteDatabase db = this.getWritableDatabase();
		
		int toReturn = db.update(TABLE_LIST_ITEMS, value, KEY_LIST_ID_PK + " = ?",
				new String[]{String.valueOf(listItem.getListsItemID())});
		 db.close();
		 return toReturn;
		
	}
	
	
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

	            li.setItemBrand(c.getString(c.getColumnIndexOrThrow(KEY_ITEM_BRAND)));
	            li.setItemCat(c.getString(c.getColumnIndexOrThrow(KEY_ITEM_CAT)));
	            li.setItemID(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_ITEM_PK))));
	            li.setItemName(c.getString(c.getColumnIndexOrThrow(KEY_ITEM_NAME)));
	            // adding to final list
	            items.add(li);
	        } while (c.moveToNext());
	    }
	    db.close();
	    c.close();
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
	public int addListID(CompleteList list){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_LIST_NAME, list.getListName());
		values.put(KEY_LIST_STOREFK, list.getStore());
		if(list.getListPK() > 0){
			values.put(KEY_LIST_ID, list.getListPK());
		}
		

		//insert row 
		int newID = (int) db.insert(TABLE_LIST_IDS, null, values);
		db.close();
		return newID; 
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
		            li.setListName(c.getString(c.getColumnIndexOrThrow(KEY_LIST_NAME)));
		            li.setListPK(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_LIST_ID))));
		            li.setStore(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_LIST_STOREFK))));
		            // adding to final list
		            list.add(li);
		        } while (c.moveToNext());
		    }
		    db.close();
		    c.close();
		    return list;
		
	}
	
	public int removeList(int listID){
		SQLiteDatabase db = this.getWritableDatabase();
		int num_rows_Deleted = db.delete(TABLE_LIST_IDS, KEY_LIST_ID + " =?", 
				new String[]{String.valueOf(listID)});
		Log.e("# of LISTS deleted - ", Integer.toString(num_rows_Deleted));
		db.close();
		return num_rows_Deleted;
		
	}
	
	/**
	 * Adding a Store  to the database
	 * */
	public int  addStoreDB(StoreObject store){
		int result; 
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
				
		values.put(KEY_storePK, store.getStorePK());    
		values.put(KEY_storeName, store.getStoreName()); 
		values.put(KEY_storeAddress, store.getStoreAddress()); 
		values.put(KEY_storeCity, store.getStoreCity()); 
		values.put(KEY_storeZipCode, store.getStoreZipCode()); 
		values.put(KEY_storeType, store.getStoreType()); 
		values.put(KEY_storeImage, store.getStoreImage()); 

		//insert row 
		result = (int) db.insert(TABLE_STORE, null, values);
		db.close(); 
		return result; 
	}
	
	public StoreObject getStoreObject(int storeFK){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_STORE +  " WHERE " + KEY_storePK + " = " + storeFK; ; 
		
	    Cursor c = db.rawQuery(selectQuery, null);
	    StoreObject store = null;
	    
	   c.moveToFirst();
	   if(c.getCount() != 0){
	        do {
	        	
	        	 store = new StoreObject();
	        	
	        	store.setStoreAddress(c.getString(c.getColumnIndexOrThrow(KEY_storeAddress)));
	        	store.setStoreCity(c.getString(c.getColumnIndexOrThrow(KEY_storeCity)));
	        	store.setStoreImage(c.getString(c.getColumnIndexOrThrow(KEY_storeImage)));
	        	store.setStoreName(c.getString(c.getColumnIndexOrThrow(KEY_storeName)));
	        	store.setStorePK(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_storePK))));
	        	store.setStoreType(c.getString(c.getColumnIndexOrThrow(KEY_storeType)));
	        	store.setStoreZipCode(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_storeZipCode))));
	       
	        	
	        } while (c.moveToNext());
	   }
	    db.close();
	    c.close();
	    return store;
		
	}
	
	public ArrayList<StoreObject> getStores(){
		SQLiteDatabase db = this.getReadableDatabase(); 
		ArrayList<StoreObject> list = new ArrayList<StoreObject>();
		
		String selectQuery = "SELECT * FROM " + TABLE_STORE; 
		Log.e("getTableItems", selectQuery);
		
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	   c.moveToFirst();
	   if(c.getCount() != 0){
	        do {
	        	
	        	StoreObject store = new StoreObject();
	        	
	        	store.setStoreAddress(c.getString(c.getColumnIndexOrThrow(KEY_storeAddress)));
	        	store.setStoreCity(c.getString(c.getColumnIndexOrThrow(KEY_storeCity)));
	        	store.setStoreImage(c.getString(c.getColumnIndexOrThrow(KEY_storeImage)));
	        	store.setStoreName(c.getString(c.getColumnIndexOrThrow(KEY_storeName)));
	        	store.setStorePK(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_storePK))));
	        	store.setStoreType(c.getString(c.getColumnIndexOrThrow(KEY_storeType)));
	        	store.setStoreZipCode(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_storeZipCode))));
	            
	            // adding to final list
	            list.add(store);
	        	
	        } while (c.moveToNext());
	   }
	    db.close();
	    c.close();
	    return list;
	}
	
	public int addAisle(AisleObject a){
		int result; 
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_AISLE_NAME, a.getAisleName());
		values.put(KEY_AISLE_PK, a.getAislePK());
		values.put(KEY_AISLE_STRFK, a.getAisleStoreFK());
				
		//insert row 
		result = (int) db.insert(TABLE_AISLE, null, values);
		db.close(); 
		return result; 
	}
	
	public ArrayList<AisleObject> getAisle(int storeFK){
		SQLiteDatabase db = this.getReadableDatabase(); 
		ArrayList<AisleObject> list = new ArrayList<AisleObject>();
		
		String selectQuery = "SELECT * FROM " + TABLE_AISLE + " WHERE " + KEY_AISLE_STRFK + " = " + storeFK; 
		Log.e("getAsile", selectQuery);
		
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	   c.moveToFirst();
	   if(c.getCount() != 0){
	        do {
	        	
	        	AisleObject aisle = new AisleObject();
	        	
	        	aisle.setAisleName(c.getString(c.getColumnIndexOrThrow(KEY_AISLE_NAME)));
	        	aisle.setAislePK(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_AISLE_PK))));
	        	aisle.setAisleStoreFK(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEY_AISLE_STRFK))));
	            
	            // adding to final list
	            list.add(aisle);
	        	
	        } while (c.moveToNext());
	   }
	    db.close();
	    c.close();
	    return list;
	}
	
	
	public int addCat(CategoryObject c){
		int result; 
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEYcat_pk, c.getCat_pk());
		values.put(KEYcat_locfk, c.getCat_locfk());
		values.put(KEYcat_name, c.getCat_name());
		values.put(KEYcat_value, c.getCat_value());
		values.put(KEYcat_x, c.getCat_x());
		values.put(KEYcat_y, c.getCat_y());

		// insert row
		result = (int) db.insert(TABLE_CAT, null, values);
		db.close(); 
		return result; 
	}
	
	public ArrayList<CategoryObject> getCatFromAisle(int aisleFK){
		SQLiteDatabase db = this.getReadableDatabase(); 
		ArrayList<CategoryObject> list = new ArrayList<CategoryObject>();
		
		String selectQuery = "SELECT * FROM " + TABLE_CAT + " WHERE " + KEYcat_locfk + " = " + aisleFK; 
		Log.e("getAsile", selectQuery);
		
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	   c.moveToFirst();
	   if(c.getCount() != 0){
	        do {
	        	
	        	CategoryObject cat = new CategoryObject();
	        	cat.setCat_locfk(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEYcat_locfk))));
	        	cat.setCat_name(c.getString(c.getColumnIndexOrThrow(KEYcat_name)));
	        	cat.setCat_pk(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEYcat_pk))));
	        	cat.setCat_value(Integer.parseInt(c.getString(c.getColumnIndexOrThrow(KEYcat_value))));
	        	cat.setCat_x(c.getString(c.getColumnIndexOrThrow(KEYcat_x)));
	        	cat.setCat_y(c.getString(c.getColumnIndexOrThrow(KEYcat_y)));
	        	
	            // adding to final list
	            list.add(cat);
	        	
	        } while (c.moveToNext());
	   }
	    db.close();
	    c.close();
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE); 
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AISLE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);

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
	
	public void clearListItemTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ITEMS);
		db.execSQL(CREATE_LIST_TABLE);
		 db.close();
	}
	
	public void clearListTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_IDS);
		db.execSQL(CREATE_LISTIDS_TABLE);
		 db.close();
	}
	
	public void DBclose(){
		 SQLiteDatabase db = this.getReadableDatabase();
	        if (db != null && db.isOpen()){
	            db.close();
	        }
	}


}

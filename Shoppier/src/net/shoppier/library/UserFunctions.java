package net.shoppier.library;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import net.shoppier.Lists;
import net.shoppier.SearchableItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserFunctions {

	private JSONParser jsonParser;

	private static String loginURL = "http://jonnyklemmer.com/shoppier/Android_DB_API/";
	// private static String registerURL =
	// "http://jonnyklemmer.com/shoppier/Android_DB_API/";

	private static String login_tag = "login";
	private static String register_tag = "register";
	
	private static final String Array_List = "list";
	private static final String Tag_ListID = "list_id";
	private static final String Tag_LIST_ITEM = "list_text";
	private static final String Array_Search_List = "items";
	
	private static final String Tag_Item_ID = "item_pk";
	private static final String Tag_Item_Name = "item_name";
	private static final String Tag_Item_Cat = "item_cat";
	private static final String Tag_Item_Brand = "item_brand";
	
	JSONArray userList = null;
	JSONArray searchList = null;
	private DatabaseHandler db;

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
		
	}

	// constructor for login
	public UserFunctions(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		jsonParser = new JSONParser(params);
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		try {
			JSONObject json = jsonParser.execute(loginURL).get();
			return json;
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		// Log.e("JSON", json.toString());
		return null;
	}
	
	//TODO figure out sending data back to database once the list table it full fixed
	public void Sync(Context context){
		this.db = new DatabaseHandler(context);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "storeUsersList"));
		params.add(new BasicNameValuePair("userID", db.getUserDetails().get("uid"))); 
		params.add(new BasicNameValuePair("userList", db.getList().toString()));
		jsonParser = new JSONParser(params);
		
		try {
			jsonParser.execute(loginURL).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	public ArrayList<SearchableItem> getSearchableItems(Context context) throws JSONException{
		this.db = new DatabaseHandler(context);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getSearchableItems"));
		
		ArrayList<SearchableItem> searchableList = new ArrayList<SearchableItem>();
		jsonParser = new JSONParser(params);
		
		try {
			JSONObject json = jsonParser.execute(loginURL).get();
			searchList = json.getJSONArray(Array_Search_List);
			for (int i = 0; i <= searchList.length() - 1; i++) {
				SearchableItem li = new SearchableItem();
				JSONObject l = searchList.getJSONObject(i);
				li.setItemBrand(l.getString(Tag_Item_Brand));
				li.setItemCat(l.getString(Tag_Item_Cat));
				li.setItemID(Integer.parseInt(l.getString(Tag_Item_ID)));
				li.setItemName(l.getString(Tag_Item_Name));
				searchableList.add(li);
				db.addSearchableItem(li);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return searchableList;
		
	}
	public ArrayList<Lists> getUserGrocList(Context context) {
		this.db = new DatabaseHandler(context);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getUsersList"));
		params.add(new BasicNameValuePair("userID", db.getUserDetails().get("uid"))); 

		jsonParser = new JSONParser(params);

		ArrayList<Lists> userGrocList = new ArrayList<Lists>();

		try {
			JSONObject json = jsonParser.execute(loginURL).get();
			try {
				userList = json.getJSONArray(Array_List);
				for (int i = 0; i <= userList.length() - 1; i++) {
					Lists li = new Lists();
					JSONObject l = userList.getJSONObject(i);
					li.setListsItemID(l.getString(Tag_ListID));
					li.setListsItem(l.getString(Tag_LIST_ITEM));
					userGrocList.add(li);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return userGrocList;
	}

	/**
	 * function Register User
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		// getting JSON Object
		// JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return null;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount("user");
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		SharedPreferences settings = context.getSharedPreferences("PreFile", 0);
		Editor edit = settings.edit();
		edit.clear();
		edit.commit();
		return true;
	}

}
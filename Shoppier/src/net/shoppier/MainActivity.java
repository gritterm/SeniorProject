package net.shoppier;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;

public class MainActivity extends Activity {
	Button login;
	TextView skip;
	 Button btnLogin;
	    EditText inputUserName;
	    EditText inputPassword;
	    TextView loginErrorMsg;

	    
		 // JSON Response node names
	    private static String KEY_SUCCESS = "success";
	    private static String KEY_UID = "uid";
	    private static String KEY_NAME = "name";
	    private static String KEY_EMAIL = "email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SharedPreferences settings = getPreferences(MODE_PRIVATE);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String savedUserName = settings.getString("username", null);
		String savedPW = settings.getString("password", null);
		if(savedUserName != null && savedPW != null){
			startActivity(new Intent(MainActivity.this, DrawerActivity.class));
		}
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.btnLogout);
		inputUserName = (EditText) findViewById(R.id.item_name);
        inputPassword = (EditText) findViewById(R.id.editText2);
		login.setOnClickListener(handler);
		loginErrorMsg = (TextView) findViewById(R.id.textView2);
        loginErrorMsg.setVisibility(View.INVISIBLE);
		skip = (TextView) findViewById(R.id.textView1);
		skip.setOnClickListener(handler);
		
		  
	}
	
	@Override
	public void onBackPressed() {
		
	}


	private OnClickListener handler = new OnClickListener() {

		public void onClick(View v) {
			if (v == login) {
				
				String username;
				String password;
				

					  username = inputUserName.getText().toString();
		              password = inputPassword.getText().toString();

				 
	                
	                UserFunctions userFunction = new UserFunctions(username, password);
	                JSONObject json = userFunction.loginUser(username, password);
	 
	                
	                // check for login response
	                try {
	                	 // user successfully logged in
	                    if (json.getString(KEY_SUCCESS) != null) {
	                        loginErrorMsg.setText("");
	                        String res = json.getString(KEY_SUCCESS); 
	                        if(Integer.parseInt(res) == 1){
	                             
	                            // Clear all previous data in sql lite database
	                            userFunction.logoutUser(getApplicationContext());
	                                     
	                             
	                            //save username and password for future login
	                            SavePreferences(username, password);
	                        	
	                            // Store user details in SQLite Database
	                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	                            JSONObject json_user = json.getJSONObject("user");
	                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID)); 
	                            
	                            //Retrieve user's list from SQL Server Database
	                            userFunction.getListIDS(getApplicationContext());
	                            
	                            //get users groclist items
	        	                ArrayList<ListsItem> grocList = userFunction.getUserGrocList(getApplicationContext());
	        	                
	        	                //add groclist items to list sql lite database table 
	        	                for(ListsItem l : grocList){
	        	                	if(!l.equals(null)){
	        	                		db.addItemToListDB(l);
	        	                	}
	        	                }
	                            
	                            // Launch ListSelection Screen
	        	                //TODO: Change start up screen after login
	                            startActivity(new Intent(MainActivity.this, DrawerActivity.class));
	        	                
	                        }else{
	                            // Error in login
	                        	loginErrorMsg.setVisibility(View.VISIBLE);
	                            loginErrorMsg.setText("Incorrect username/password");
	                            inputPassword.setText("");
	                        }
	                    }
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	           
			} else if (v == skip) {
				UserFunctions  userFunction = new UserFunctions();
				userFunction.logoutUser(getApplicationContext());
				startActivity(new Intent(MainActivity.this, DrawerActivity.class));
			}
		}
	};
	
	private void SavePreferences(String username, String password ){
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.putString("username", username).putString("password", password);
		
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

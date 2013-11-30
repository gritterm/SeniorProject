package net.shoppier;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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

	String username;
	String password;
	Context context;

	DatabaseHandler db ;
	Handler updateBarHandler;

	UserFunctions userFuncation = new UserFunctions(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// SharedPreferences settings = getPreferences(MODE_PRIVATE);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String savedUserName = settings.getString("username", null);
		userFuncation = new UserFunctions(); 
		String savedPW = settings.getString("password", null);
		if (savedUserName != null && savedPW != null) {
			fillUserGrcoList();
						startActivity(new Intent(MainActivity.this, DrawerActivity.class));
		}
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.btnLogout);
		inputUserName = (EditText) findViewById(R.id.add_item_Name);
		inputPassword = (EditText) findViewById(R.id.add_item_brand);
		inputPassword.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                    login.performClick();
		                    return true;
		                default:
		                    break;
		            }
		        }return false;
			}
			
		}
		);
		login.setOnClickListener(handler);
		skip = (TextView) findViewById(R.id.textView1);
		skip.setOnClickListener(handler);
		updateBarHandler = new Handler();


	}



	private void setDropDowns() {

		UserFunctions userFuncation = new UserFunctions();
		try {
			userFuncation.getDropDownInfo(getApplicationContext());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private OnClickListener handler = new OnClickListener() {

		public void onClick(View v) {

			

			if (v == login) {

				username = inputUserName.getText().toString();
				password = inputPassword.getText().toString();

			

							UserFunctions userFunction = new UserFunctions(
									username, password);
							JSONObject json = userFunction.loginUser(username,
									password);
							// check for login response
							try {
								// user successfully logged in
								if (json.getString(KEY_SUCCESS) != null) {
									String res = json.getString(KEY_SUCCESS);
									if (Integer.parseInt(res) == 1) {

										// Clear all previous data in sql lite
										// database
										userFunction.logoutUser(getApplicationContext());
										
										setDropDowns();
										
										// save username and password for future login
										SavePreferences(username, password);

										// Store user details in SQLite Database

										JSONObject json_user = json
												.getJSONObject("user");
										db.addUser(
												json_user.getString(KEY_NAME),
												json_user.getString(KEY_EMAIL),
												json.getString(KEY_UID));

										// Retrieve user's list from SQL Server
										// Database
										userFunction
												.getListIDS(getApplicationContext());

										fillUserGrcoList();
										

										// Launch ListSelection Screen
										startActivity(new Intent(
												MainActivity.this,
												DrawerActivity.class));

									} else {
										Context context = getApplicationContext();
										final Toast toast = Toast
												.makeText(
														context,
														"Either the username was not found or"
																+ " password was incorrect. Please check"
																+ " your spelling and try again.",
														Toast.LENGTH_LONG);
										toast.setGravity(Gravity.TOP
												| Gravity.CENTER, 0, 380);
										LinearLayout linearLayout = (LinearLayout) toast
												.getView();
										TextView messageTextView = (TextView) linearLayout
												.getChildAt(0);
										messageTextView.setTextSize(18);
										toast.show();
										new CountDownTimer(5000, 1000) {

											public void onTick(
													long millisUntilFinished) {
												toast.show();
											}

											public void onFinish() {
												toast.show();
											}

										}.start();
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}


			} else if (v == skip) {
				try {
					UserFunctions userFunction = new UserFunctions();
					userFunction.logoutUser(getApplicationContext());
					// get dropdown information from SQL Server
					setDropDowns();
					startActivity(new Intent(MainActivity.this,
							DrawerActivity.class));
				} catch (Exception e) {
					Log.e("tag", "error", e);
				}
			}
		}
	};
	

	private void SavePreferences(String username, String password) {

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
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
	
	public void fillUserGrcoList(){
		// get users groclist items
		ArrayList<ListsItem> grocList = userFuncation
				.getUserGrocList(getApplicationContext());
		DatabaseHandler dbhandler = new DatabaseHandler(getApplicationContext());
		dbhandler.clearListItemTable();
		// add groclist items to list sql lite
		// database
		// table
		for (ListsItem l : grocList) {
			if (!l.equals(null)) {
				dbhandler.addItemToListDB(l);
			}
		}
	}

//	class ProgressTask extends AsyncTask<Void, Void, Boolean> {
//		ProgressDialog ringProgressDialog = new ProgressDialog(context);
//
//		protected void onPreExecute() {
//
//			ringProgressDialog.setMessage("Doing awesome things ...");
//			ringProgressDialog.setIndeterminate(true);
//			ringProgressDialog.show();
//		}
//
//		protected void onPostExecute(final Boolean success) {
//
//			if (ringProgressDialog.isShowing()) {
//				ringProgressDialog.dismiss();
//			}
//
//		}
//
//		@Override
//		protected Boolean doInBackground(Void... params) {
//			try {
//				UserFunctions userFunction = new UserFunctions();
//				userFunction.logoutUser(getApplicationContext());
//				// get dropdown information from SQL Server
//				setDropDowns();
//				startActivity(new Intent(MainActivity.this,
//						DrawerActivity.class));
//				return true;
//			} catch (Exception e) {
//				Log.e("tag", "error", e);
//			}
//			return false;
//		}
//
//	}
}


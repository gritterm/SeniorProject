package net.shoppier;

import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button login;
	TextView skip;
	Button btnLogin;
	EditText inputUserName;
	EditText inputPassword;
	TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.button1);
		inputUserName = (EditText) findViewById(R.id.item_name);
		inputPassword = (EditText) findViewById(R.id.editText2);
		login.setOnClickListener(handler);
		skip = (TextView) findViewById(R.id.textView1);
		skip.setOnClickListener(handler);
	}

	private OnClickListener handler = new OnClickListener() {

		public void onClick(View v) {
			if (v == login) {

				String username = inputUserName.getText().toString();
				String password = inputPassword.getText().toString();
				UserFunctions userFunction = new UserFunctions(username,
						password);
				JSONObject json = userFunction.loginUser(username, password);

				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							// user successfully logged in
							// Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(
									getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");

							// Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getString(KEY_NAME),
									json_user.getString(KEY_EMAIL),
									json.getString(KEY_UID));

							// Launch ListSelection Screen
							startActivity(new Intent(MainActivity.this,
									GrocListActivity.class));

						} else {
							// Error in login
							Context context = getApplicationContext();
							CharSequence text = "Incorrect username/password. "
									+ "Please enter a valid username/password combination.";
							int duration = Toast.LENGTH_LONG;

							Toast toast = Toast.makeText(context, text,
									duration);
							toast.setGravity(Gravity.CENTER | Gravity.CENTER,
									0, 0);
							toast.show();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (v == skip) {
				startActivity(new Intent(MainActivity.this,
						GrocListActivity.class));
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

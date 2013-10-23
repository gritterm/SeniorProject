package net.shoppier;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import net.shoppier.library.UserFunctions;

public class SplashActivity extends Activity {
	Button login, skip, test;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		
		String savedUserName = settings.getString("username", null);
		String savedPW = settings.getString("password", null);
		if(savedUserName != null && savedPW != null){
			startActivity(new Intent(SplashActivity.this, ListSelect.class));
		}
		setContentView(R.layout.activity_splash);
		login = (Button) findViewById(R.id.btnLogout);
		login.setOnClickListener(handler);
		skip = (Button) findViewById(R.id.button2);
		skip.setOnClickListener(handler);
		test = (Button) findViewById(R.id.test);
		test.setOnClickListener(handler);
	}
	private OnClickListener handler = new OnClickListener() {
				
		public void onClick(View v) {
			if (v == login){
				
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				
			}else if (v== skip){
				startActivity(new Intent(SplashActivity.this, GrocListActivity.class));
			}else if (v == test){
				 UserFunctions userFunction = new UserFunctions();
	                ArrayList<Lists> grocList = userFunction.getUserGrocList(getApplicationContext());
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}

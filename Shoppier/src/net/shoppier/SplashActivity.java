package net.shoppier;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SplashActivity extends Activity {
	Button login, skip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		login = (Button) findViewById(R.id.button1);
		login.setOnClickListener(handler);
		skip = (Button) findViewById(R.id.button2);
		skip.setOnClickListener(handler);
	}
	private OnClickListener handler = new OnClickListener() {
				
		public void onClick(View v) {
			if (v == login){
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
			}else if (v== skip){
				startActivity(new Intent(SplashActivity.this, ListSelect.class));
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

package net.shoppier;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button login;
	TextView skip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.button1);
		login.setOnClickListener(handler);
		skip = (TextView) findViewById(R.id.textView1);
		skip.setOnClickListener(handler);
	}

	private OnClickListener handler = new OnClickListener() {

		public void onClick(View v) {
			if (v == login) {
				if (false) {
					Intent loginIntent = new Intent(MainActivity.this,
							ListSelect.class);

					startActivity(loginIntent);
				}else{
					Intent loginIntent = new Intent(MainActivity.this,
							GrocListActivity.class);

					startActivity(loginIntent);
				}
			} else if (v == skip) {
				startActivity(new Intent(MainActivity.this, ListSelect.class));
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

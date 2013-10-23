package net.shoppier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class AddActivity extends Activity {
	private final String TAG = getClass().getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);
		setTitle("Add New Item");
		ImageButton add = (ImageButton) findViewById(R.id.but_add);
		final TextView nom = (TextView) findViewById(R.id.item_name);
		final TextView bran = (TextView) findViewById(R.id.item_brand);
		final TextView siz = (TextView) findViewById(R.id.item_size);
		
		OnClickListener confHandler = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent update = new Intent();
				String new_name = nom.getText().toString();
				update.putExtra("NewName",new_name);			
				String new_brand = bran.getText().toString();
				update.putExtra("NewBrand",new_brand);		
				float new_size = Float.valueOf(siz.getText().toString());
				update.putExtra("NewSize",new_size);
				setResult(RESULT_OK, update);
				finish();
			}
		};
		
		add.setOnClickListener(confHandler);
	}
	
}

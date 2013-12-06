package net.shoppier;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

//This Class will be used for displaying extra information about a product if the user press the (i) button 

public class SingleSearableItemView extends Activity {

	TextView txtName; 
	TextView txtBrand; 
	String name; 
	String brand; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_searable_item_view);
		
		// Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        // Get the results of name
        name = i.getStringExtra("name");
        // Get the results of brand
        brand = i.getStringExtra("brand");
 
        // Locate the TextViews in singleitemview.xml
        txtName = (TextView) findViewById(R.id.name);
        txtBrand = (TextView) findViewById(R.id.brand);
 
        // Load the results into the TextViews
        txtName.setText(name);
        txtBrand.setText(brand);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_searable_item_view, menu);
		return true;
	}

}

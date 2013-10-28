package net.shoppier;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;

import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;
import android.os.Bundle;
import android.app.ListActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends ListActivity {
	
	//listview
	private ListView lv; 
	
	//listview Adapter
	ArrayAdapter<SearchableItem> adapter; 
	
	//Search Bar
	EditText inputSearch; 

	UserFunctions userfunction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		userfunction = new UserFunctions();
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		try {
			db.clearItemTable();
			userfunction.getSearchableItems(getApplicationContext());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ArrayList<SearchableItem> items = new ArrayList<SearchableItem>();
		items = db.getAllSearchableItems();
		
		lv = getListView();
		inputSearch = (EditText)findViewById(R.id.InputSearch); 		
		adapter = new SearchableItemAdapter(getApplicationContext(),R.layout.activity_search, items);
		
		lv.setAdapter(adapter);
		
	
		
		inputSearch.addTextChangedListener(new TextWatcher() {
	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	            // When user changed the Text
	            String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
	                    ((SearchableItemAdapter) adapter).filter(text);
	        }

	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                            int arg3) {
	            // TODO Auto-generated method stub
	        }

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
	});
		
	}
	
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);

		return true;
	}

}

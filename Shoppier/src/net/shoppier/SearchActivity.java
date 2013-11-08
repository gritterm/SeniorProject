package net.shoppier;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;

import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends Activity implements OnItemClickListener {

	// listview
	private ListView lv;

	// listview Adapter
	ArrayAdapter<SearchableItem> adapter;

	// Search Bar
	EditText inputSearch;
	ArrayList<SearchableItem> items = new ArrayList<SearchableItem>();

	// private static final int ADD_FROM_SEARCH = 1;
	UserFunctions userfunction;
	
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		userfunction = new UserFunctions();
		 db = new DatabaseHandler(getApplicationContext());
		try {
			db.clearItemTable();
			userfunction.getSearchableItems(getApplicationContext());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		items = db.getAllSearchableItems();

		lv = (ListView) findViewById(R.id.list);
		lv.setOnItemClickListener(this);
		inputSearch = (EditText) findViewById(R.id.InputSearch);
		adapter = new SearchableItemAdapter(getApplicationContext(),
				R.layout.activity_search, items);
		lv.setAdapter(adapter);

		inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				String text = inputSearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				((SearchableItemAdapter) adapter).filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, final int pos, long id) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				SearchActivity.this);
		final ArrayList<SearchableItem> list= (ArrayList<SearchableItem>) ((SearchableItemAdapter) adapter).getItems();
		dialog.setTitle(list.get(pos).itemBrand + " "
				+ list.get(pos).itemName);
		dialog.setMessage("Add to list?");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent add = new Intent();
				String new_name = list.get(pos).itemName;
				String new_brand = list.get(pos).itemBrand;
				int search_id = list.get(pos).itemID;
				add.putExtra("NewName", new_name);
				add.putExtra("NewBrand", new_brand);
				add.putExtra("SearchId", Integer.toString(search_id));
				setResult(RESULT_OK, add);
				
				finish();
			}

		});
		dialog.setNegativeButton("No", null);
		dialog.create();
		dialog.show();
		return;

	}


		

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);

		return true;
	}

}

package net.shoppier;


import java.util.ArrayList;


import android.app.AlertDialog;
import android.app.ListActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;

public class GrocListActivity extends ListActivity {
	// private ArrayList<GrocItem> items;
	private ArrayList<ListsItem> items;
	private GrocAdapter adapter;
	private ListView lview;
	private ImageButton add;
	private Button logout;
	private static final int ADD_REQUEST = 0xFACEEE;
	 static final int ADD_FROM_SEARCH = 0x3;
	private DatabaseHandler db;
	private Button sync;
	private Button search;
	UserFunctions userfunction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groc_list);

		this.userfunction = new UserFunctions();
		lview = getListView();
		lview.setOnItemLongClickListener(lchandler);
		sync = (Button) findViewById(R.id.syncBtn);
		add = (ImageButton) findViewById(R.id.but_add);
		logout = (Button) findViewById(R.id.btnLogout);
		search = (Button) findViewById(R.id.searchBtn);
		add.setOnClickListener(handler);
		logout.setOnClickListener(handler);
		search.setOnClickListener(handler);

		if (userfunction.isUserLoggedIn(getApplicationContext())) {
			sync.setOnClickListener(handler);
		} else {
			sync.setVisibility(View.GONE);
			logout.setVisibility(View.GONE);
		}

		items = new ArrayList<ListsItem>();

		this.db = new DatabaseHandler(getApplicationContext());
		ArrayList<ListsItem> arryList = new ArrayList<ListsItem>();
		arryList = db.getList("3");

		for (ListsItem l : arryList) {
			if (!l.equals(null)) {
				items.add(l);
			}
		}

		if (arryList.size() == 0) {
			// Display a toast message saying that there is no list found and
			// give information about how to create one.
			ListsItem listItemToastMes1 = new ListsItem();
			listItemToastMes1
					.setListsItemName("Enter An Item by clicking the + sign or searching");
			items.add(listItemToastMes1);
			ListsItem listItemToastMes2 = new ListsItem();
			listItemToastMes2.setListsItemName("Long Click the item to Delete");
			items.add(listItemToastMes2);
		}

		adapter = new GrocAdapter(this, R.layout.item, items);

		setListAdapter(adapter);

		
		adapter.notifyDataSetChanged();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == ADD_REQUEST) {

			String newname = new String(data.getStringExtra("NewName"));
			String newbrand = new String(data.getStringExtra("NewBrand"));
			ListsItem selected = new ListsItem();
			selected.setListsItemName(newname);
			selected.setListItemBrand(newbrand);
			selected.setSearchItemId("0");
			//TODO Figure out the best way to keep listFK / update listFK
			selected.setListFK("3");
			items.add(selected);
			db.addItemToListDB(selected);
			adapter.notifyDataSetChanged();

		}if(resultCode == RESULT_OK && requestCode == ADD_FROM_SEARCH){
			String newname = new String(data.getStringExtra("NewName"));
			String newbrand = new String(data.getStringExtra("NewBrand"));
			ListsItem selected = new ListsItem();
			selected.setListsItemName(newname);
			selected.setListItemBrand(newbrand);
			items.add(selected);
			db.addItemToListDB(selected);
			adapter.notifyDataSetChanged();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.groc_list, menu);
		return true;
	}

	private OnClickListener handler = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == add) {
				Intent adder = new Intent(GrocListActivity.this,
						AddActivity.class);
				startActivityForResult(adder, ADD_REQUEST);

			}
			if (v == logout) {
				UserFunctions userfunction = new UserFunctions();

				userfunction.logoutUser(getBaseContext());

				SharedPreferences settings = getSharedPreferences("PreFile", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.clear();
				editor.commit();

				startActivity(new Intent(GrocListActivity.this,
						MainActivity.class));

			}
			if (v == sync) {
				userfunction.Sync(getApplicationContext(), "3");
			}
			if (v == search) {

				Intent search = new Intent(GrocListActivity.this,
						SearchActivity.class);
				startActivityForResult(search, ADD_FROM_SEARCH);

			}
		}

	};

	private OnItemLongClickListener lchandler = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> list, View item,
				int position, long id) {
			final int pos = position;
			final ListsItem itemToDel = (ListsItem) list.getItemAtPosition(position);

			AlertDialog.Builder dialog = new AlertDialog.Builder(
					GrocListActivity.this);
			dialog.setTitle("Confirmation Required");
			dialog.setMessage("Remove this item?");
			dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					items.remove(pos);
					db.removeItemFromList(itemToDel.getListsItemID());
					adapter.notifyDataSetChanged();
				}

			});

			dialog.setNegativeButton("No", null);
			dialog.create();
			dialog.show();
			return false;

		}
	};

	protected void onDestroy() {
		super.onDestroy();
		db.clearItemTable();
	}
}

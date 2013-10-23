package net.shoppier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.ListActivity;
import android.content.Context;
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
	//private ArrayList<GrocItem> items;
	private ArrayList<Lists> items;
	private GrocAdapter adapter;
	private ListView lview;
	private ImageButton add;
	private Button logout;
	private static final int ADD_REQUEST = 0xFACEEE;
	private DatabaseHandler db; 
	private Button sync;
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
		
		add.setOnClickListener(handler);
		logout.setOnClickListener(handler);
		
		if(userfunction.isUserLoggedIn(getApplicationContext())){
			sync.setOnClickListener(handler);
		}else{
			sync.setVisibility(View.GONE);
		}
		
		items = new ArrayList<Lists>();
				
			this.db = new DatabaseHandler(getApplicationContext());
			ArrayList<Lists> arryList= new ArrayList<Lists>();
			arryList = db.getList();
			
			for(Lists l : arryList){
				if(!l.equals(null)){
					items.add(l);
					
				}
			}
			
			if(arryList.size() == 0){
				// Display a toast message saying that there is no list found and
				// give information about how to create one.
				Lists listItemToastMes1 = new Lists(); 
				listItemToastMes1.setListItem("Enter An Item");
				items.add(listItemToastMes1);
				Lists listItemToastMes2 = new Lists();
				listItemToastMes2.setListItem("Long Click to Delete");
				items.add(listItemToastMes2);
			}
		
		adapter = new GrocAdapter(this, R.layout.item, items);

		setListAdapter(adapter);

		adapter.notifyDataSetChanged();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == ADD_REQUEST) {

			String newname = new String(
					data.getStringExtra("NewName"));
			Lists selected = new Lists();
			selected.setListItem(newname);
			//StringBuffer(new selected.getListsItem()) = newname;
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

			if (v == add){
				Intent adder = new Intent(GrocListActivity.this, AddActivity.class);
				startActivityForResult(adder, ADD_REQUEST);
				
				
			}if(v == logout){
				UserFunctions userfunction = new UserFunctions(); 
				
				userfunction.logoutUser(getBaseContext());
				
				SharedPreferences settings = getSharedPreferences("PreFile",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.clear();
				editor.commit();
				
				startActivity(new Intent(GrocListActivity.this, SplashActivity.class));
				
			}if(v == sync){
				userfunction.Sync(getApplicationContext());
			}

			
		}

	};
	private OnItemLongClickListener lchandler = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> list, View item,
				int position, long id) {
			final int pos = position;
			Lists itemToDel = (Lists) list.getItemAtPosition(position);
			/*
			 * AlertDialog.Builder dialog = new AlertDialog.Builder(
			 * GrocListActivity.this); dialog.setTitle("Confirmation Required");
			 * dialog.setMessage("Remove this item?");
			 * dialog.setPositiveButton("Yes", new OnClickListener() { public
			 * void onClick(DialogInterface dialog, int which) {
			 * items.remove(pos); adapter.notifyDataSetChanged(); }
			 * 
			 * });
			 * 
			 * dialog.setNegativeButton("No", null); dialog.create();
			 * dialog.show(); return false;
			 */
			items.remove(pos);
			db.removeItemFromList(itemToDel.getListsItemID(), itemToDel.getListsItem());
			adapter.notifyDataSetChanged();
			return false;
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		try {
			FileOutputStream myfile = openFileOutput("mydata",
					Context.MODE_PRIVATE);
			PrintWriter myprint = new PrintWriter(myfile);
			for (Lists e : items) {
				myprint.println(e.getListsItem().toString());
			}

			myprint.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

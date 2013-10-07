package net.shoppier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class GrocListActivity extends ListActivity {
	private ArrayList<GrocItem> items;
	private GrocAdapter adapter;
	private ListView lview;
	private ImageButton add;
	private static final int ADD_REQUEST = 0xFACEEE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groc_list);

		lview = getListView();
		lview.setOnItemLongClickListener(lchandler);
		items = new ArrayList<GrocItem>();
		add = (ImageButton) findViewById(R.id.but_add);
		add.setOnClickListener(handler);
		try {
			FileInputStream myfile = openFileInput("mydata");
			Scanner myscan = new Scanner(myfile);

			GrocItem item;
			while (myscan.hasNext()) {
				String lin1 = myscan.nextLine();
				item = new GrocItem();
				item.name = new StringBuffer(lin1);

				items.add(item);
			}

		} catch (FileNotFoundException e) {
			// Display a toast message saying that there is no list found and
			// give information about how to create one.
			GrocItem item;
			item = new GrocItem();
			item.name = new StringBuffer("Milk");
			items.add(item);
			item = new GrocItem();
			item.name = new StringBuffer("Oreos");
			items.add(item);

		}
		adapter = new GrocAdapter(this, R.layout.item, items);

		setListAdapter(adapter);

		adapter.notifyDataSetChanged();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == ADD_REQUEST) {

			StringBuffer newname = new StringBuffer(
					data.getStringExtra("NewName"));
			GrocItem selected = new GrocItem();
			selected.name = newname;
			items.add(selected);
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

			Intent adder = new Intent(GrocListActivity.this, AddActivity.class);

			startActivityForResult(adder, ADD_REQUEST);
		}

	};
	private OnItemLongClickListener lchandler = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> list, View item,
				int position, long id) {
			final int pos = position;

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
			for (GrocItem e : items) {
				myprint.println(e.name.toString());
			}

			myprint.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

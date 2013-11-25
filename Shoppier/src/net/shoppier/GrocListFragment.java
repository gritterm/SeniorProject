package net.shoppier;

import java.util.ArrayList;

import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class GrocListFragment extends Fragment {
	private ArrayList<ListsItem> items;
	private GrocAdapter adapter;
	private ListView lview;
	private ImageButton add;
	private static final int ADD_REQUEST = 0x4;
	static final int ADD_FROM_SEARCH = 0x3;
	static final int ADD_FROM_BARCODE = 0x7;
	static final int EDIT_ITEM = 0x10;
	static final int RESULT_OK = -1;
	private DatabaseHandler db;
	private ImageButton search;
	private ImageButton barcodeButton;
	private Button routeButton;
	String currentlistID;
	UserFunctions userfunction;

	public GrocListFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.activity_groc_list,
				container, false);
		currentlistID = getArguments().getString("listID");
		String currentlistName = getArguments().getString("ListName", "List");
		getActivity().setTitle(currentlistName);

		this.userfunction = new UserFunctions();
		lview = (ListView) rootView.findViewById(R.id.list);

		add = (ImageButton) rootView.findViewById(R.id.but_add);
		search = (ImageButton) rootView.findViewById(R.id.searchBtn);
		routeButton = (Button) rootView.findViewById(R.id.routeButton);
		barcodeButton = (ImageButton) rootView
				.findViewById(R.id.barcodeSearchButton);
		lview.setOnItemLongClickListener(lchandler);
		lview.setOnItemClickListener(clickhandler);
		add.setOnClickListener(handler);
		search.setOnClickListener(handler);
		barcodeButton.setOnClickListener(handler);
		routeButton.setOnClickListener(handler);

		items = new ArrayList<ListsItem>();

		this.db = new DatabaseHandler(getActivity());
		ArrayList<ListsItem> arryList = new ArrayList<ListsItem>();
		arryList = db.getList(currentlistID);

		for (ListsItem l : arryList) {
			if (!l.equals(null)) {
				items.add(l);
			}
		}

		if (arryList.size() == 0 && (db.getRowCount("listId") - 1) == 0) {
			AlertDialog.Builder remv_conf = new AlertDialog.Builder(
					getActivity());
			remv_conf.setTitle("Looks like you're new");
			remv_conf
					.setMessage("Here's a few things you can do:\n\n"
							+ "Enter an item by searching for it using the search button or by clicking the \"+\" button.\n\n"
							+ "Delete an item from your list by long pressing it.");

			remv_conf.setNegativeButton("Dismiss", null);
			remv_conf.create();
			remv_conf.show();
		}

		adapter = new GrocAdapter(this.getActivity(), R.layout.item, items);
		lview.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == ADD_REQUEST) {

			String newname = new String(data.getStringExtra("NewName"));
			String newbrand = new String(data.getStringExtra("NewBrand"));
			ListsItem selected = new ListsItem();
			selected.setListsItemName(newname);
			selected.setListItemBrand(newbrand);
			selected.setSearchItemId(0);

			selected.setListFK(Integer.parseInt(currentlistID));
			items.add(selected);
			db.addItemToListDB(selected);
			adapter.notifyDataSetChanged();

		}
		if (resultCode == RESULT_OK && requestCode == ADD_FROM_SEARCH) {
			String newname = new String(data.getStringExtra("NewName"));
			String newbrand = new String(data.getStringExtra("NewBrand"));
			String searchId = new String(data.getStringExtra("SearchId"));
			ListsItem selected = new ListsItem();
			selected.setListsItemName(newname);
			selected.setListItemBrand(newbrand);
			selected.setSearchItemId(Integer.parseInt(searchId));
			selected.setListFK(Integer.parseInt(currentlistID));
			items.add(selected);
			db.addItemToListDB(selected);
			adapter.notifyDataSetChanged();

		}
		if (resultCode == RESULT_OK && requestCode == ADD_FROM_BARCODE) {
			String contents = data.getStringExtra("SCAN_RESULT"); // this is the
																	// result
//			ListsItem newItem = userfunction.getBarcodeProduct(contents,
//					currentlistID);

			// Temporary Hack
			ListsItem newItem = new ListsItem();
			newItem.setListItemBrand("Gatorade");
			newItem.setListsItemName("Cool Blue");
			//newItem.setListFK(Integer.parseInt(listID));
			//newItem.setSearchItemId(0);
			
			confirmAddFromBarCode(newItem);
		}
		if (resultCode == RESULT_OK && requestCode == EDIT_ITEM) {
			int contents = data.getIntExtra("result", 0); // this is the
																	// result
		}
	}

	private OnClickListener handler = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == add) {
				Intent adder = new Intent(getActivity(), AddActivity.class);
				startActivityForResult(adder, ADD_REQUEST);

			}
			else if (v == search) {
				Intent search = new Intent(getActivity(), SearchActivity.class);
				startActivityForResult(search, ADD_FROM_SEARCH);

			}
			else if (v == barcodeButton) {
				try {
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "PRODUCT_MODE"); // "PRODUCT_MODE"
					intent.putExtra("SAVE_HISTORY", false);// this stops saving ur barcode in
															// barcode scanner app's history
					startActivityForResult(intent, ADD_FROM_BARCODE);
				} catch (ActivityNotFoundException ex) {
					confimDownloadingBarcodeReader();
				}
			} else if(v == routeButton){
				route();
			}
		}

	};

	private void confirmAddFromBarCode(final ListsItem item) {
		AlertDialog.Builder add_conf = new AlertDialog.Builder(getActivity());
		add_conf.setTitle("Confirmation Required");
		add_conf.setMessage("Would you like to add " + item.getListsItemName()
				+ "?");
		add_conf.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						items.add(item);
						db.addItemToListDB(item);
						adapter.notifyDataSetChanged();
					}

				});

		add_conf.setNegativeButton("No", null);
		add_conf.create();
		add_conf.show();
	}

	protected void route() {
		AlertDialog.Builder add_conf = new AlertDialog.Builder(getActivity());
		add_conf.setTitle("Confirmation Required");
		add_conf.setMessage("Routing your list will reorder it, would you like to continue?");
		add_conf.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						items = userfunction.routeList(items);
						adapter.notifyDataSetChanged();
					}

				});

		add_conf.setNegativeButton("No", null);
		add_conf.create();
		add_conf.show();
		
	}

	private void confimDownloadingBarcodeReader() {
		AlertDialog.Builder add_conf = new AlertDialog.Builder(getActivity());
		add_conf.setTitle("Confirmation Required");
		add_conf.setMessage("You need to install the ZXing barcode reader to continue. Would you like to?");
		add_conf.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Uri marketUri = Uri
								.parse("market://details?id=com.google.zxing.client.android");
						Intent marketIntent = new Intent(Intent.ACTION_VIEW,
								marketUri);
						startActivity(marketIntent);
					}

				});

		add_conf.setNegativeButton("No", null);
		add_conf.create();
		add_conf.show();
	}

	private OnItemLongClickListener lchandler = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> list, View item,
				int position, long id) {
			final int pos = position;
			final ListsItem itemToDel = (ListsItem) list
					.getItemAtPosition(position);

			AlertDialog.Builder remv_conf = new AlertDialog.Builder(
					getActivity());
			remv_conf.setTitle("Confirmation Required");
			remv_conf.setMessage("Remove this item?");
			remv_conf.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							items.remove(pos);
							db.removeItemFromList(itemToDel.getListsItemID());
							adapter.notifyDataSetChanged();
							
						}

					});

			remv_conf.setNegativeButton("No", null);
			remv_conf.create();
			remv_conf.show();
			return false;

		}
	};
	
	private OnItemClickListener clickhandler = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> list, View item,
				int position, long id) {
			final int pos = position;
			final ListsItem selectedItem = (ListsItem) list
					.getItemAtPosition(position);

			AlertDialog.Builder conf = new AlertDialog.Builder(
					getActivity());
			conf.setMessage("I would like to...");
			conf.setPositiveButton("Edit Item",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent editItem = new Intent(getActivity(),EditItemFragment.class);
							editItem.putExtra("selectedItem", String.valueOf(selectedItem.getListsItemID()));
							startActivityForResult(editItem, EDIT_ITEM);
						}

					});

			conf.setNegativeButton("Find Item in Store",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					
				}

			});
			conf.create();
			conf.show();
			

		}
	};

}

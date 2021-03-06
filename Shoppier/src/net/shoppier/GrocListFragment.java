	package net.shoppier;

import java.util.ArrayList;

import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoAdapter.DeleteItemCallback;

import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class GrocListFragment extends Fragment implements OnDismissCallback {
	private ArrayList<ListsItem> items;
	private GrocAdapter adapter;
	private ListView lview;
	//private ImageButton add;
	private static final int ADD_REQUEST = 0x4;
	static final int ADD_FROM_SEARCH = 0x3;
	static final int ADD_FROM_BARCODE = 0x7;
	static final int EDIT_ITEM = 0x10;
	static final int RESULT_OK = -1;
	private DatabaseHandler db;
	private Button search;
	//private ImageButton barcodeButton;
	private Button routeButton;
	private TextView totalCost; 
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
		
		fillUserGrcoList();
		search = (Button) rootView.findViewById(R.id.searchBtn);
		totalCost = (TextView)rootView.findViewById(R.id.totalCost);
		routeButton = (Button) rootView.findViewById(R.id.routeButton);
		lview.setOnItemClickListener(clickhandler);

		search.setOnClickListener(handler);
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
							+ "Enter an item by searching for it in our database using the search"
							+ " button or by clicking the \"+\" button.\n\n"
							+ "Delete an item from your list by long pressing it.");

			remv_conf.setNegativeButton("Dismiss", null);
			remv_conf.create();
			remv_conf.show();
		}

		adapter = new GrocAdapter(this.getActivity(), R.layout.item, items);
		//lview.setAdapter(adapter);
		//adapter.notifyDataSetChanged();
		
		SwipeDismissAdapter adapter1 = new SwipeDismissAdapter(adapter, this);
		adapter1.setAbsListView(lview);
		lview.setAdapter(adapter1);
		adapter1.notifyDataSetChanged();
		updateTotalCost();
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
			selected.setItemQTY("1");
			selected.setxCord(0);
			selected.setyCord(0);
			selected.setListFK(Integer.parseInt(currentlistID));
			selected.setChecked("False");
			items.add(selected);
			db.addItemToListDB(selected);
			userfunction.Sync(getActivity(), currentlistID);
			adapter.notifyDataSetChanged();

		}
		if (resultCode == RESULT_OK && requestCode == ADD_FROM_SEARCH) {
			String newname = new String(data.getStringExtra("NewName"));
			String newbrand = new String(data.getStringExtra("NewBrand"));
			String searchId = new String(data.getStringExtra("SearchId"));
			String catFK = new String(data.getStringExtra("NewCatFK"));

			ListsItem selected = new ListsItem();
			selected.setListsItemName(newname);
			selected.setListItemBrand(newbrand);
			selected.setSearchItemId(Integer.parseInt(searchId));
			selected.setListFK(Integer.parseInt(currentlistID));
			selected.setCatFK(Integer.parseInt(catFK));
			selected.setItemQTY("1");
			selected.setChecked("false");
			if(!catFK.equals("0")){			
				ArrayList<Integer> cord = db.getItemCorFromCatPK(Integer.parseInt(catFK));
				
				selected.setxCord(cord.get(0));
				selected.setyCord(cord.get(1));
			}else {
				selected.setxCord(0);
				selected.setyCord(0);
			}
			items.add(selected);
			db.addItemToListDB(selected);
			userfunction.Sync(getActivity(), currentlistID);
			updateTotalCost();
			adapter.notifyDataSetChanged();

		}
		if (resultCode == RESULT_OK && requestCode == ADD_FROM_BARCODE) {
			String contents = data.getStringExtra("SCAN_RESULT"); // this is the
																	// result
																	// ListsItem
																	// newItem =
																	// userfunction.getBarcodeProduct(contents,
			// currentlistID);

			// Temporary Hack
			ListsItem newItem = new ListsItem();
			newItem.setListItemBrand("Gatorade");
			newItem.setListsItemName("Cool Blue");
			// newItem.setListFK(Integer.parseInt(listID));
			// newItem.setSearchItemId(0);

			confirmAddFromBarCode(newItem);
		}
		if (resultCode == RESULT_OK && requestCode == EDIT_ITEM) {
			int contents = data.getIntExtra("result", 0); // this is the result
			this.db = new DatabaseHandler(getActivity());
			ArrayList<ListsItem> arryList = new ArrayList<ListsItem>();
			arryList = db.getList(currentlistID);
			items.clear();
			for (ListsItem l : arryList) {
				if (!l.equals(null)) {
					
					items.add(l);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}

	private OnClickListener handler = new OnClickListener() {

		@Override
		public void onClick(View v) {

				if (v == search) {
				Intent search = new Intent(getActivity(), SearchActivity.class);
				startActivityForResult(search, ADD_FROM_SEARCH);

//			} else if (v == barcodeButton) {
//				try {
//					Intent intent = new Intent(
//							"com.google.zxing.client.android.SCAN");
//					intent.putExtra("SCAN_MODE", "PRODUCT_MODE"); // "PRODUCT_MODE"
//					intent.putExtra("SAVE_HISTORY", false);// this stops saving
//															// ur barcode in
//															// barcode scanner
//															// app's history
//					startActivityForResult(intent, ADD_FROM_BARCODE);
//				} catch (ActivityNotFoundException ex) {
//					confimDownloadingBarcodeReader();
//				}
			} else if (v == routeButton) {
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
						
						ArrayList<ListsItem> tempList = userfunction.routeList(items, getActivity(), Integer.parseInt(currentlistID));
						items.clear();
						for (ListsItem l : tempList) {
							if (!l.equals(null)) {
								items.add(l);
							}
						}
						//items.addAll(tempList);
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

//	private OnItemLongClickListener lchandler = new OnItemLongClickListener() {
//		@Override
//		public boolean onItemLongClick(AdapterView<?> list, View item,
//				int position, long id) {
//			final int pos = position;
//			final ListsItem itemToDel = (ListsItem) list
//					.getItemAtPosition(position);
//
//			AlertDialog.Builder remv_conf = new AlertDialog.Builder(
//					getActivity());
//			remv_conf.setTitle("Confirmation Required");
//			remv_conf.setMessage("Remove this item?");
//			remv_conf.setPositiveButton("Yes",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							items.remove(pos);
//							db.removeItemFromList(itemToDel.getListsItemID());
//							updateTotalCost();
//							adapter.notifyDataSetChanged();
//
//						}
//
//					});
//
//			remv_conf.setNegativeButton("No", null);
//			remv_conf.create();
//			remv_conf.show();
//			return false;
//
//		}
//	};
	

	
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
							updateTotalCost();
						}

					});

			conf.setNegativeButton("Find Item in Store",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(selectedItem.getxCord() == 0 || selectedItem.getyCord() == 0){
						Toast msg = Toast.makeText(getActivity(), "Item cannot be located at this time.",
								Toast.LENGTH_SHORT);
						msg.setGravity(Gravity.TOP, 0, 300);
						msg.show();
							
					}else{
						Intent findItem = new Intent(getActivity(), MapLocator.class);
						findItem.putExtra("selectedItem", String.valueOf(selectedItem.getSearchItemId()));
						startActivity(findItem);
					}
				}

			});
			conf.create();
			conf.show();
			

		}
	};

	public void fillUserGrcoList(){
		// get users groclist items
		ArrayList<ListsItem> grocList = userfunction
				.getUserGrocList(getActivity());
		DatabaseHandler dbhandler = new DatabaseHandler(getActivity());
		dbhandler.clearListTable();
		dbhandler.clearListItemTable();
		userfunction.getListIDS(getActivity());
		// add groclist items to list sql lite
		// database
		// table
		for (ListsItem l : grocList) {
			if (!l.equals(null)) {
				dbhandler.addItemToListDB(l);
			}
		}
	}
	public void updateTotalCost(){
		totalCost.setText("$"+db.getTotalCost(Integer.parseInt(currentlistID)));
	}



	@Override
	public void onDismiss(AbsListView listview, int[] postition) {
		ListsItem item = (ListsItem) listview.getItemAtPosition(postition[0]);
		items.remove(postition[0]);
		db.removeItemFromList(item.getListsItemID());
		userfunction.Sync(getActivity(), currentlistID);
		updateTotalCost();
		adapter.notifyDataSetChanged();
		
	}
	
	 
}

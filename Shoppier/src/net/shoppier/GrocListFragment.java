package net.shoppier;

import java.util.ArrayList;

import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class GrocListFragment extends Fragment{
	private ArrayList<ListsItem> items;
	private GrocAdapter adapter;
	private ListView lview;
	private ImageButton add;
	private Button logout;
	private static final int ADD_REQUEST = 0x4;
	 static final int ADD_FROM_SEARCH = 0x3;
	 static final int RESULT_OK = -1;
	private DatabaseHandler db;
	private Button sync;
	private Button search;
	UserFunctions userfunction;

	
	public GrocListFragment() {
		
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.activity_groc_list, container, false);
        String currentlistID = getArguments().getString("listID");
        String currentlistName = getArguments().getString("ListName", "List");
        getActivity().setTitle(currentlistName);
        
        this.userfunction = new UserFunctions();
		lview = (ListView) rootView.findViewById(R.id.list);
		
		sync = (Button) rootView.findViewById(R.id.syncBtn);
		add = (ImageButton) rootView.findViewById(R.id.but_add);
		logout = (Button) rootView.findViewById(R.id.btnLogout);
		search = (Button) rootView.findViewById(R.id.searchBtn);
		lview.setOnItemLongClickListener(lchandler);
		add.setOnClickListener(handler);
		logout.setOnClickListener(handler);
		search.setOnClickListener(handler);

		if (userfunction.isUserLoggedIn(getActivity())) {
			sync.setOnClickListener(handler);
		} else {
			sync.setVisibility(View.GONE);
			logout.setVisibility(View.GONE);
		}


		items = new ArrayList<ListsItem>();

	
		this.db = new DatabaseHandler(getActivity());
		ArrayList<ListsItem> arryList = new ArrayList<ListsItem>();
		arryList = db.getList(currentlistID);

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
			//TODO Figure out the best way to keep listFK / update listFK
			selected.setListFK(3);
			items.add(selected);
			db.addItemToListDB(selected);
			adapter.notifyDataSetChanged();

		}if(resultCode == RESULT_OK && requestCode == ADD_FROM_SEARCH){
			String newname = new String(data.getStringExtra("NewName"));
			String newbrand = new String(data.getStringExtra("NewBrand"));
			String searchId = new String(data.getStringExtra("SearchId"));
			ListsItem selected = new ListsItem();
			selected.setListsItemName(newname);
			selected.setListItemBrand(newbrand);
			selected.setSearchItemId(Integer.parseInt(searchId));
			//TODO Fix when add multiple list 
			selected.setListFK(3);
			items.add(selected);
			db.addItemToListDB(selected);
			adapter.notifyDataSetChanged();

		}
	}
	
	private OnClickListener handler = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == add) {
				Intent adder = new Intent(getActivity(),
						AddActivity.class);
				startActivityForResult(adder, ADD_REQUEST);

			}
//			if (v == logout) {
//				UserFunctions userfunction = new UserFunctions();
//
//				userfunction.logoutUser(getBaseContext());
//
//				SharedPreferences settings = getSharedPreferences("PreFile", 0);
//				SharedPreferences.Editor editor = settings.edit();
//				editor.clear();
//				editor.commit();
//
//				startActivity(new Intent(getActivity(),
//						MainActivity.class));
//
//			}
			if (v == sync) {
				userfunction.Sync(getActivity(), "3");
			}
			if (v == search) {

				Intent search = new Intent(getActivity(),
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

			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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

}

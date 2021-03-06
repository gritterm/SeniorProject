package net.shoppier;

import java.util.ArrayList;
import java.util.List;

import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


/**
 * Fragment to create a new list  
 * 
 * @param email
 * @param password
 * */
public class addListFragment extends Fragment{
	
	private Button addListBtn; //add list button
	private EditText inputListName; //list name
	private static final int ADD_REQUEST = 0x5; //add_request ID used by NavMenuItem
	static final int RESULT_OK = -1; 
	private DatabaseHandler db; 
	private Spinner storespinner;
	private UserFunctions uf;
	
	public addListFragment(){
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View rootView = inflater.inflate(R.layout.add_list, container, false);
	        
			addListBtn = (Button) rootView.findViewById(R.id.but_add_list);
			inputListName = (EditText) rootView.findViewById(R.id.list_name);
			storespinner = (Spinner)rootView.findViewById(R.id.newlist_storespinner);
			addListBtn.setOnClickListener(handler);
			addItemsOnStoreSpinner(storespinner);
			db = new DatabaseHandler(getActivity());
			uf = new UserFunctions();
	        return rootView; 
	        
	}
	
	private OnClickListener handler = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == addListBtn) {
				
				CompleteList tempList = new CompleteList();
				String list_name = inputListName.getText().toString();
				StoreObject store = (StoreObject)storespinner.getSelectedItem();
				tempList.setChanged(true);
				tempList.setListName(list_name);
				tempList.setStore(store.getStorePK());
				tempList.setListPK(db.addListID(tempList)); // adding list to DB and setting PK
				uf.AddListToSqlServer(getActivity(), tempList);
				((DrawerActivity)getActivity()).startNewList(tempList); //new list send back to DrawerActivity.
				

			}
		}

	};
	
	/**
	 * function fills store drop down from database based 
	 * 
	 * @param store spinner
	 * */
	public void addItemsOnStoreSpinner(Spinner spinner) {

		List<StoreObject> list = new ArrayList<StoreObject>();

		DatabaseHandler db = new DatabaseHandler(getActivity());
		list = db.getStores();

		ArrayAdapter<StoreObject> dataAdapter = new ArrayAdapter<StoreObject>(
				getActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
}

package net.shoppier;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.shoppier.library.*;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class addToDBFragment extends Fragment {

	private Button addToDB;
	private EditText itemName, itemBrand;
	private Spinner store, aisle, location;
	private String arr_store[], arr_aisle[];
	private static final int ADD_REQUEST = 0x5;
	static final int RESULT_OK = -1;
	private UserFunctions uf;

	public addToDBFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.db_add, container, false);
		addToDB = (Button) rootView.findViewById(R.id.db_add_button);
		addToDB.setOnClickListener(handler);
		itemName = (EditText) rootView.findViewById(R.id.db_add_name);
		itemBrand = (EditText) rootView.findViewById(R.id.edit_item_brand);
		store = (Spinner) rootView.findViewById(R.id.db_add_store);
		addItemsOnStoreSpinner(store);
		aisle = (Spinner) rootView.findViewById(R.id.db_add_aisle);
		location = (Spinner) rootView.findViewById(R.id.db_add_loc);
		
		store.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int pos, long id) {
				StoreObject storeSelected = (StoreObject) parentView
						.getItemAtPosition(pos);
				int storePK = storeSelected.getStorePK();
				addItemsOnAisleSpinner(aisle, storePK);
				addItemsOnLocationSpinner(location, 0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		aisle.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int pos, long id) {
				AisleObject storeSelected = (AisleObject) parentView
						.getItemAtPosition(pos);
				int aislePK = storeSelected.getAislePK();
				addItemsOnLocationSpinner(location, aislePK);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		return rootView;

	}

	private OnClickListener handler = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == addToDB) {
				String name = itemName.getText().toString();
				String brand = itemBrand.getText().toString();

				// Cleanup to keep format consistent
				
				name = name.toLowerCase().trim();
				brand = brand.toLowerCase().trim();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				brand = brand.substring(0, 1).toUpperCase()
						+ brand.substring(1);

				//int itemCatFK = aisle.getSelectedItemPosition() + 1;
				StoreObject storeob = (StoreObject) store.getSelectedItem();
				AisleObject aisleob = (AisleObject) aisle.getSelectedItem();
				CategoryObject catob = (CategoryObject) location.getSelectedItem();
				int itemCatFK  = catob.getCat_pk();
				uf = new UserFunctions();
				JSONObject returnValue = uf.sendCrowdSourceItem(name, brand, itemCatFK);
				
					Toast toast = Toast.makeText(getActivity(),
							"Thank you for adding " + brand + " " + name
									+ " to our database!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 300);
					toast.show();
					//reshow addToDBFrag for user to add another item
					Fragment fragment = new addToDBFragment();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();
				
			}
		}

	};
		
	
	 public void addItemsOnStoreSpinner(Spinner spinner) {
		 
			List<StoreObject> list = new ArrayList<StoreObject>();
			
			DatabaseHandler db = new DatabaseHandler(getActivity());
			list = db.getStores();


			ArrayAdapter<StoreObject> dataAdapter = new ArrayAdapter<StoreObject>(getActivity(),
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter);
		  }
	 
	 public void addItemsOnAisleSpinner(Spinner spinner, int storePK) {
		 
			List<AisleObject> list = new ArrayList<AisleObject>();
			
			DatabaseHandler db = new DatabaseHandler(getActivity());
			list = db.getAisle(storePK);

			ArrayAdapter<AisleObject> dataAdapter = new ArrayAdapter<AisleObject>(getActivity(),
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter);
			
		  }
	 public void addItemsOnLocationSpinner(Spinner spinner, int aislePK) {
		 
			List<CategoryObject> list = new ArrayList<CategoryObject>();
			
			DatabaseHandler db = new DatabaseHandler(getActivity());
			list = db.getCatFromAisle(aislePK);

			ArrayAdapter<CategoryObject> dataAdapter = new ArrayAdapter<CategoryObject>(getActivity(),
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter);
		  }
}

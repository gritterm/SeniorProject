package net.shoppier;

import net.shoppier.library.*;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class addToDBFragment extends Fragment {

	private Button addToDB;
	private EditText itemName, itemBrand;
	private Spinner store, aisle;
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
		itemBrand = (EditText) rootView.findViewById(R.id.db_add_brand);
		store = (Spinner) rootView.findViewById(R.id.db_add_store);
		arr_store = new String[1];
		arr_store[0] = "Meijer";
		uf = new UserFunctions();
		ArrayAdapter<String> s_adap = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, arr_store);
		store.setAdapter(s_adap);
		aisle = (Spinner) rootView.findViewById(R.id.db_add_aisle);
		arr_aisle = new String[16];
		arr_aisle[0] = "Aisle 1";
		arr_aisle[1] = "Aisle 2";
		arr_aisle[2] = "Aisle 3";
		arr_aisle[3] = "Aisle 4";
		arr_aisle[4] = "Aisle 5";
		arr_aisle[5] = "Aisle 6";
		arr_aisle[6] = "Aisle 7";
		arr_aisle[7] = "Aisle 8";
		arr_aisle[8] = "Aisle 9";
		arr_aisle[9] = "Aisle 10";
		arr_aisle[10] = "Aisle 11";
		arr_aisle[11] = "Aisle 12";
		arr_aisle[12] = "Aisle 13";
		arr_aisle[13] = "Aisle 14";
		arr_aisle[14] = "Aisle 15";
		arr_aisle[15] = "Bakery";

		ArrayAdapter<String> a_adap = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, arr_aisle);
		aisle.setAdapter(a_adap);

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

				int itemCatFK = aisle.getSelectedItemPosition() + 1;

				uf.sendCrowdSourceItem(name, brand, itemCatFK);
				Toast toast = Toast.makeText(getActivity(),
						"Thank you for adding " + brand + " " + name
								+ " to our database.", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 300);
				toast.show();
				
			}
		}

	};
}

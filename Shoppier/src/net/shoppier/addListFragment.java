package net.shoppier;

import net.shoppier.library.DatabaseHandler;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class addListFragment extends Fragment{
	
	private ImageButton addListBtn; 
	private EditText inputListName;
	private static final int ADD_REQUEST = 0x5;
	static final int RESULT_OK = -1;
	private DatabaseHandler db; 
	
	public addListFragment(){
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View rootView = inflater.inflate(R.layout.add_list, container, false);
	        
			addListBtn = (ImageButton) rootView.findViewById(R.id.but_add_list);
			inputListName = (EditText) rootView.findViewById(R.id.list_name);
			addListBtn.setOnClickListener(handler);
			db = new DatabaseHandler(getActivity());

	        return rootView; 
	        
	}
	
	private OnClickListener handler = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == addListBtn) {
				
				CompleteList tempList = new CompleteList();
				String list_name = inputListName.getText().toString();
				tempList.setChanged(true);
				tempList.setListName(list_name);
				tempList.setListPK(db.addListID(tempList)); // adding list to DB and setting PK
				
				((DrawerActivity)getActivity()).startNewList(tempList);

				
				

			}
		}

	};
}

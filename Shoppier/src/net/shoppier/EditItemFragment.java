package net.shoppier;


import net.shoppier.library.DatabaseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;



public class EditItemFragment extends Activity {
	EditText itemName, itemBrand, itemPrice; 
	NumberPicker qtyPicker; 
	DatabaseHandler db; 
	String listID;
	Button addButton; 
	ListsItem li;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_item);
		
		db = new DatabaseHandler(getApplicationContext());
		addButton = (Button) findViewById(R.id.editItemDoneBtn);
		addButton.setOnClickListener(handler);
		itemName = (EditText) findViewById(R.id.edit_item_name);
		itemBrand = (EditText) findViewById(R.id.edit_item_brand);
		itemPrice = (EditText) findViewById(R.id.editItemPrice);
		qtyPicker = (NumberPicker) findViewById(R.id.numberPicker);
		qtyPicker.setMinValue(0);
		qtyPicker.setMaxValue(50);
		
		Intent extra = getIntent();
		if(extra != null){
			 listID = extra.getExtras().getString("selectedItem");
		}
		 li = db.getListItem(listID);
		//if item from search make name and brand field un-editable
		if(li.getSearchItemId() != 0){
			itemName.setEnabled(false);
			itemBrand.setEnabled(false);
		}
		itemName.setText(li.getListsItemName());
		itemBrand.setText(li.getListItemBrand());
		itemPrice.setText(String.valueOf(li.getItemPrice()));
		if(li.getItemQTY() != null){
			qtyPicker.setValue(Integer.parseInt(li.getItemQTY()));
		}
	}
	OnClickListener handler = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ListsItem tempItem = new ListsItem();
			
			String newItemName = itemName.getText().toString();
			String newBrandName = itemBrand.getText().toString();
			String newPrice = itemPrice.getText().toString();
			double price = Double.parseDouble(newPrice);
			int newQty = qtyPicker.getValue();
			
			tempItem.setItemPrice(price);
			tempItem.setItemQTY(String.valueOf(newQty));
			tempItem.setListItemBrand(newBrandName);
			tempItem.setListsItemName(newItemName);
			tempItem.setListsItemID(li.getListsItemID());
			tempItem.setSearchItemId(li.getSearchItemId());
			int result = db.editListItem(tempItem);
			
			Intent update = new Intent();
			update.putExtra("NewBrand", result);
			
			setResult(RESULT_OK, update);
			finish();
		}
	};

}

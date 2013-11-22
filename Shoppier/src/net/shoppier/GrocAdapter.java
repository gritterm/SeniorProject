package net.shoppier;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GrocAdapter extends ArrayAdapter<ListsItem>{
	private LayoutInflater pump;
	private int itemLayout;
	private ArrayList<ListsItem> dataSource;
	
	
	public GrocAdapter(Context context, int layout, ArrayList<ListsItem> src) {
		super(context, layout, src);
		pump = LayoutInflater.from(context);
		itemLayout = layout;
		dataSource = src;
	}
	
	class ViewHolder {
		TextView name_holder, brand_holder, qty_holder, price_holder;

	}@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder bin;
		if (convertView == null) {
			Log.d("=====", "Inflating from XML");
			convertView = pump.inflate(itemLayout, parent, false);

			bin = new ViewHolder();

			bin.name_holder = (TextView) convertView
					.findViewById(R.id.textView1);
			bin.brand_holder = (TextView) convertView
					.findViewById(R.id.label_name);
			bin.qty_holder = (TextView) convertView
					.findViewById(R.id.inputItemQty);
			bin.price_holder = (TextView) convertView
					.findViewById(R.id.itemAmt);

			convertView.setTag(bin);

		} else {
			Log.d("=====", "Recycling view for item " + position);
			bin = (ViewHolder) convertView.getTag();

		}
		String name_context = dataSource.get(position).getListsItemName();
		String brand_context = dataSource.get(position).getListItemBrand();
		String qty_context = dataSource.get(position).getItemQTY();
		String price_context = NumberFormat.getCurrencyInstance().format(dataSource.get(position).getItemPrice());		 
		bin.name_holder.setText(name_context);
		bin.brand_holder.setText(brand_context);
		bin.qty_holder.setText(qty_context);
		if(price_context.equals("$0.00")){
			bin.price_holder.setText("");
		}else{
			bin.price_holder.setText(price_context);
		}
		return convertView;
	}



}

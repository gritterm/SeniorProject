package net.shoppier;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GrocAdapter extends ArrayAdapter<Lists>{
	private LayoutInflater pump;
	private int itemLayout;
	private ArrayList<Lists> dataSource;
	
	
	public GrocAdapter(Context context, int layout, ArrayList<Lists> src) {
		super(context, layout, src);
		pump = LayoutInflater.from(context);
		itemLayout = layout;
		dataSource = src;
	}
	
	class ViewHolder {
		TextView name_holder;

	}@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder bin;
		if (convertView == null) {
			Log.d("=====", "Inflating from XML");
			convertView = pump.inflate(itemLayout, parent, false);

			bin = new ViewHolder();

			bin.name_holder = (TextView) convertView
					.findViewById(R.id.textView1);

			convertView.setTag(bin);

		} else {
			Log.d("=====", "Recycling view for item " + position);
			bin = (ViewHolder) convertView.getTag();

		}
		String name_context = dataSource.get(position).getListsItem();
		bin.name_holder.setText(name_context);
		return convertView;
	}



}

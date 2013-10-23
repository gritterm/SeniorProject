package net.shoppier;

import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GrocAdapter extends ArrayAdapter<GrocItem> {
	private LayoutInflater pump;
	private int itemLayout;
	private List<GrocItem> dataSource;

	public GrocAdapter(Context context, int layout, List<GrocItem> src) {
		super(context, layout, src);
		pump = LayoutInflater.from(context);
		itemLayout = layout;
		dataSource = src;

	}

	class ViewHolder {
		TextView name_holder;
		TextView brand_holder;
		TextView size_holder;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder bin;
		if (convertView == null) {

			convertView = pump.inflate(itemLayout, parent, false);

			bin = new ViewHolder();

			bin.name_holder = (TextView) convertView
					.findViewById(R.id.itemName);
			bin.brand_holder = (TextView) convertView
					.findViewById(R.id.itemBrand);
			bin.size_holder = (TextView) convertView
					.findViewById(R.id.itemSize);

			convertView.setTag(bin);

		} else {
			bin = (ViewHolder) convertView.getTag();

		}
		StringBuffer name_context = dataSource.get(position).name;
		bin.name_holder.setText(name_context);
		StringBuffer brand_context = dataSource.get(position).brand;
		bin.brand_holder.setText(brand_context);
		float size_context = dataSource.get(position).size;
		bin.size_holder.setText(""+size_context);
		return convertView;
	}

}

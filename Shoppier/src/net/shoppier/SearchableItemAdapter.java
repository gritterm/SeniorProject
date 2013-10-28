package net.shoppier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SearchableItemAdapter  extends ArrayAdapter<SearchableItem>{
	private LayoutInflater pump;
	private int itemLayout;
	private ArrayList<SearchableItem> dataSource;
	private List<SearchableItem> list; 
	private Context mycontext;

	
	public SearchableItemAdapter(Context context, int layout, ArrayList<SearchableItem> src) {
		super(context, layout, src);
		pump = LayoutInflater.from(context);
		mycontext = context;
		this.itemLayout = layout;
		this.dataSource = src;
		this.list = new ArrayList<SearchableItem>();
		this.list.addAll(src);
	}
	
	
	class ViewHolder {
		TextView name; 
		TextView brand; 
		Button addBtn;

	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder bin;
		if (convertView == null) {
			Log.d("=====", "Inflating from XML");
			convertView = pump.inflate(R.layout.searachable_itemview, parent, false);

			bin = new ViewHolder();
			 // Locate the name and brand textviews
			bin.brand = (TextView) convertView.findViewById(R.id.brand);
			bin.name = (TextView) convertView.findViewById(R.id.name);
			bin.addBtn = (Button) convertView.findViewById(R.id.addItemBtn);			
			convertView.setTag(bin); 
			
			bin.addBtn.setOnClickListener(	new OnClickListener(){

			@Override
			public void onClick(View v) {
				 // Send single item click data to g Class
                Intent intent = new Intent(mycontext, GrocListActivity.class);
                // Pass all data brand
                intent.putExtra("brand",
                        (list.get(position).getItemBrand()));
                // Pass all data name
                intent.putExtra("name",
                        (list.get(position).getItemName()));
                //Pass the item that was passed
                intent.putExtra("ItemID", 
                		list.get(position).getItemID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mycontext.startActivity(intent);
			}
			
		});
		

		} else {
			Log.d("=====", "Recycling view for item " + position);
			bin = (ViewHolder) convertView.getTag();

		}
		// String name_context = list.get(position).getItemName();
			SearchableItem i = list.get(position);
			if (i != null) {
				bin.brand.setText(i.getItemBrand() + " ");
				bin.name.setText(i.getItemName());
			}
			
		
			

		return convertView;
	}
	
	@Override
	public int getCount () {
	    return list.size ();
	}
	
    @Override
    public long getItemId(int position) {
        return position;
    }

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
        list.clear(); 
        if (charText.length() == 0) {
            list.addAll(dataSource);
        } else {
            for (SearchableItem wp : dataSource) {
                if (wp.getItemName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
	
}

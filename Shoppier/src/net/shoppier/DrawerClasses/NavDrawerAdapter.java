package net.shoppier.DrawerClasses;

import java.util.ArrayList;

import net.shoppier.CompleteList;
import net.shoppier.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem>  {
	
	 private LayoutInflater inflater;
	 ArrayList<NavDrawerItem> objects;
	    public NavDrawerAdapter(Context context, int textViewResourceId, ArrayList<NavDrawerItem> inputobjects ) {
	        super(context, textViewResourceId, inputobjects);
	        this.objects = inputobjects;
	        this.inflater = LayoutInflater.from(context);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = null ;
	        NavDrawerItem menuItem = this.getItem(position);
	        if ( menuItem.getType() == NavMenuItem.LIST_TYPE ) {
	            view = getListView(convertView, parent, menuItem );
	        }
	        else if(menuItem.getType() == NavMenuItem.AddLIST_TYPE || 
	        		menuItem.getType() == NavMenuItem.LOGOUT_TYPE ||
	        		menuItem.getType() == NavMenuItem.SYNC_TYPE ||
	        		menuItem.getType() == NavMenuItem.LOGIN_TYPE ||
					menuItem.getType() == NavMenuItem.ADDTODB_TYPE) { 
	        	view = getItemView(convertView, parent, menuItem);
	        }else{
	            view = getSectionView(convertView, parent, menuItem);
	        }
	        return view ;
	    }

	    public View getListView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem ) {
	        
	        CompleteList menuItem = (CompleteList) navDrawerItem ;
	        NavMenuItemHolder navMenuItemHolder = null;
	        
	        if (convertView == null) {
	            convertView = inflater.inflate( R.layout.navdrawer_item, parentView, false);
	            TextView labelView = (TextView) convertView
	                    .findViewById( R.id.navmenuitem_label );
	            ImageView iconView = (ImageView) convertView
	                    .findViewById( R.id.navmenuitem_icon );

	            navMenuItemHolder = new NavMenuItemHolder();
	            navMenuItemHolder.labelView = labelView ;
	            navMenuItemHolder.iconView = iconView ;

	            convertView.setTag(navMenuItemHolder);
	        }

	        if ( navMenuItemHolder == null ) {
	            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
	        }
	                    
	        navMenuItemHolder.labelView.setText(menuItem.getLabel());
	        
	        return convertView ;
	    }
	    
	    public View getItemView( View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem ) {
	        
	        NavMenuItem menuItem = (NavMenuItem) navDrawerItem ;
	        NavMenuItemHolder navMenuItemHolder = null;
	        
	        if (convertView == null) {
	            convertView = inflater.inflate( R.layout.navdrawer_item, parentView, false);
	            TextView labelView = (TextView) convertView
	                    .findViewById( R.id.navmenuitem_label );
	            ImageView iconView = (ImageView) convertView
	                    .findViewById( R.id.navmenuitem_icon );

	            navMenuItemHolder = new NavMenuItemHolder();
	            navMenuItemHolder.labelView = labelView ;
	            navMenuItemHolder.iconView = iconView ;

	            convertView.setTag(navMenuItemHolder);
	        }

	        if ( navMenuItemHolder == null ) {
	            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
	        }
	                    
	        navMenuItemHolder.labelView.setText(menuItem.getLabel());
	        navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());
	        
	        return convertView ;
	    }


	    public View getSectionView(View convertView, ViewGroup parentView,
	            NavDrawerItem navDrawerItem) {
	        
	        NavMenuSection menuSection = (NavMenuSection) navDrawerItem ;
	        NavMenuSectionHolder navMenuItemHolder = null;
	        
	        if (convertView == null) {
	            convertView = inflater.inflate( R.layout.navdrawer_section, parentView, false);
	            TextView labelView = (TextView) convertView
	                    .findViewById( R.id.navmenusection_label );

	            navMenuItemHolder = new NavMenuSectionHolder();
	            navMenuItemHolder.labelView = labelView ;
	            convertView.setTag(navMenuItemHolder);
	        }

	        if ( navMenuItemHolder == null ) {
	            navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
	        }
	                    
	        navMenuItemHolder.labelView.setText(menuSection.getLabel());
	        
	        return convertView ;
	    }
	    
	    @Override
	    public int getViewTypeCount() {
	    	//Every new view needs to be added to this count
	        return objects.size() + 1;
	    }
	    
	    @Override
	    public int getItemViewType(int position) {
	        return this.getItem(position).getType();
	    }
	    
	    @Override
	    public boolean isEnabled(int position) {
	        return getItem(position).isEnabled();
	    }
	    
	    
	    private static class NavMenuItemHolder {
	        private TextView labelView;
	        private ImageView iconView;
	    }
	    
	    private class NavMenuSectionHolder {
	        private TextView labelView;
	    }
	}


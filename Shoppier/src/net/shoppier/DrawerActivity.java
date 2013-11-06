package net.shoppier;

import java.util.ArrayList;

import net.shoppier.DrawerClasses.NavDrawerAdapter;
import net.shoppier.DrawerClasses.NavDrawerItem;
import net.shoppier.DrawerClasses.NavMenuSection;
import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;


import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawerActivity extends Activity {
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayList<CompleteList> mDrawerLists;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayAdapter drawerAdapter;
    private NavDrawerAdapter navAdapter;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		//Setting up the Drawer 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLists = new ArrayList<CompleteList>();

        mDrawerLists = db.getList();        
     // Set the adapter for the list view
//        drawerAdapter = new ArrayAdapter<CompleteList>(this, R.layout.drawer_item, mDrawerLists);
//        mDrawerList.setAdapter(drawerAdapter);

        ArrayList<NavDrawerItem> Navlists = new ArrayList<NavDrawerItem>();
        
        //List Section Head
       Navlists.add( NavMenuSection.create(100, "Lists"));

        
        for (CompleteList l : mDrawerLists) {
			if (!l.equals(null)) {
				Navlists.add(l);
			}
		}
        
        Navlists.add( NavMenuSection.create(200, "Settings"));

        navAdapter = new NavDrawerAdapter(this, R.layout.navdrawer_item, Navlists);
        
        mDrawerList.setAdapter(navAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drawer, menu);
		return true;
	}
	
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }
    
    /** Swaps fragments in the main content view */
	private void selectItem(int positionBefore) {
	    // Create a new fragment and specify the list to show based on position
		Fragment fragment = new GrocListFragment();
	    Bundle args = new Bundle();
	    int position = positionBefore - 1;
	    int currentListID = mDrawerLists.get(position).getListPK();
	    args.putString("listID", Integer.toString(currentListID));
	    args.putString("ListName", mDrawerLists.get(position).getListName());
	    fragment.setArguments(args);
	    
	    // Insert the fragment by replacing any existing fragment
	    FragmentManager fragmentManager = getFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.content_frame, fragment)
	                   .commit();
	    

	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(mDrawerLists.get(position).toString());
	    mDrawerLayout.closeDrawer(mDrawerList);
	}

	
  
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
     


}

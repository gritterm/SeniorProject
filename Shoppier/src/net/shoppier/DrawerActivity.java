package net.shoppier;

import java.util.ArrayList;

import net.shoppier.DrawerClasses.NavDrawerAdapter;
import net.shoppier.DrawerClasses.NavDrawerItem;
import net.shoppier.DrawerClasses.NavMenuItem;
import net.shoppier.DrawerClasses.NavMenuSection;
import net.shoppier.library.DatabaseHandler;
import net.shoppier.library.UserFunctions;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
	private static ArrayList<CompleteList> mDrawerLists;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private static NavDrawerAdapter navAdapter;
	private static int numOfList;
	static final int ADD_LIST_REQUEST = 0x8;
	static final int RESULT_OK = -1;
	private static ArrayList<NavDrawerItem> Navlists;
	DatabaseHandler db;
	UserFunctions userFunctions; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);

		userFunctions = new UserFunctions();
		db = new DatabaseHandler(getApplicationContext());
		// Setting up the Drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLists = new ArrayList<CompleteList>();

		mDrawerLists = db.getList();
		numOfList = mDrawerLists.size();
		// Set the adapter for the list view
		Navlists = new ArrayList<NavDrawerItem>();

		// List Section Head
		Navlists.add(NavMenuSection.create(100, "Lists"));

		for (CompleteList l : mDrawerLists) {
			if (!l.equals(null)) {
				Navlists.add(l);
			}
		}

		// Settings Section Header
		Navlists.add(NavMenuSection.create(200, "Settings"));

		//Sync Button
		Navlists.add(NavMenuItem.create(203, "Sync", "sync_icon", true,
				getBaseContext()));
			
		// Add List button
		Navlists.add(NavMenuItem.create(201, "Add List", "add_list_icon", true,
				getBaseContext()));

		if(userFunctions.isUserLoggedIn(getApplicationContext())){
			// LogOut button for user
			Navlists.add(NavMenuItem.create(202, "Logout", "logout_icon", true,
				getBaseContext()));

		}else{
			//login option for 
			Navlists.add(NavMenuItem.create(204, "Login", "login_icon", true,
					getBaseContext()));
		}
		navAdapter = new NavDrawerAdapter(this, R.layout.navdrawer_item,
				Navlists);

		mDrawerList.setAdapter(navAdapter);

		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
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

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int positionBefore) {
		if (Navlists.get(positionBefore).getType() == NavMenuItem.LIST_TYPE) {
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
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			mDrawerList.setItemChecked(positionBefore, true);
			setTitle(mDrawerLists.get(position).toString());
			mDrawerLayout.closeDrawer(mDrawerList);
			
		} else if (Navlists.get(positionBefore).getType() == NavMenuItem.SYNC_TYPE){
			sync();
		}else if(Navlists.get(positionBefore).getType() == NavMenuItem.LOGIN_TYPE){
			startActivity(new Intent(DrawerActivity.this, MainActivity.class));
		}else if (Navlists.get(positionBefore).getType() == NavMenuItem.AddLIST_TYPE ) { 
			Fragment fragment = new addListFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
			// Highlight the selected item, update the title, and close the
			// drawer
			mDrawerList.setItemChecked(positionBefore, true);
			setTitle("Add List");
			mDrawerLayout.closeDrawer(mDrawerList);
		}else if(Navlists.get(positionBefore).getType() == NavMenuItem.LOGOUT_TYPE){
			mDrawerLayout.closeDrawer(mDrawerList);
			logout();
		}
	}
	
	
	private void sync() {
		
		
	}

	public void startNewList(CompleteList newList) {
		mDrawerLists.add(newList);
		Navlists.add((numOfList + 1), newList);

		navAdapter.notifyDataSetChanged();		

		Fragment fragment = new GrocListFragment();
		Bundle args = new Bundle();
		args.putString("listID", Integer.toString(newList.getListPK()));
		args.putString("ListName",newList.getListName());
		fragment.setArguments(args);
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		
		mDrawerList.setItemChecked(numOfList + 1, true);
		setTitle(mDrawerLists.get(numOfList).toString());
		mDrawerLayout.closeDrawer(mDrawerList);
		numOfList = numOfList + 1;

	}
	
//	public void removeList(int listID){
//		mDrawerLists.remove(listID);
//		Navlists.remove(listID);
//	}
	
	public void logout(){
		
		AlertDialog.Builder remv_conf = new AlertDialog.Builder(
				this);
		remv_conf.setTitle("Confirmation Required");
		remv_conf.setMessage("Are you sure you want to leave so soon?");
		remv_conf.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				userFunctions.logoutUser(getApplicationContext());
				startActivity(new Intent(DrawerActivity.this, MainActivity.class));
			}

		});

		remv_conf.setNegativeButton("No", null);
		remv_conf.create();
		
		remv_conf.show();
		
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
	public void onBackPressed() {
		
	}


	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

}

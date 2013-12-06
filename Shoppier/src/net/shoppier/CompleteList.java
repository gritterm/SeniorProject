package net.shoppier;

import android.content.Context;
import net.shoppier.DrawerClasses.NavDrawerItem;
import net.shoppier.library.DatabaseHandler;

public class CompleteList implements NavDrawerItem  {
	
	
	private int listPK; // list primary key 
	
	private String listName; //name of list
	
	private String listRoute; // route for each list 
	
	private boolean isChanged; // boolean to tell weather the list has been changed 
	
	public static final int ITEM_TYPE = 1; //type 1 = list

	private  boolean updateActionBarTitle; //update title bar for list 
	
	private int storeFK; //The store associated with this list

	public CompleteList(){
		
	}
	public CompleteList(int listPK, String listName, String listRoute,
			boolean isChanged, int storeFK) {
		super();
		this.listPK = listPK;
		this.listName = listName;
		this.listRoute = listRoute;
		this.isChanged = isChanged;
		this.storeFK = storeFK;
		
	}
	
	
	

	public int getStore() {
		return storeFK;
	}
	public void setStore(int storeFK) {
		this.storeFK = storeFK;
	}
	@Override
	public String toString() {
		return listName;
	}
	public int getListPK() {
		return listPK;
	}

	public void setListPK(int listPK) {
		this.listPK = listPK;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getListRoute() {
		return listRoute;
	}

	public void setListRoute(String listRoute) {
		this.listRoute = listRoute;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
	@Override
	public int getId() {
		return listPK;
	}
	@Override
	public String getLabel() {
		return listName;
	}
	@Override
	public int getType() {
		return  ITEM_TYPE;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
    @Override
    public boolean updateActionBarTitle() {
        return this.updateActionBarTitle;
    }

    public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
        this.updateActionBarTitle = updateActionBarTitle;
    }
    
    public StoreObject getStoreObject(int storePK, Context context){
    	DatabaseHandler db = new DatabaseHandler(context);
    	return db.getStoreObject(storePK);
    }
	
	

}

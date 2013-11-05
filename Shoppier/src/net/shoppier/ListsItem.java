package net.shoppier;
/*
 * This is represents a List Item Object 
 * 
 */
public class ListsItem {
	
	//The FK of the  item ID
	int listsItemID; 
	
	//The List Item Name
	String listsItemName; 
	
	//The List route 
	String listRoute;
	
	//The Search item ID  if it exist
	int searchItemId; 
	
	//The list it belongs to 
	int listFK; 
	
	//Brand name 
	String listItemBrand; 

	public ListsItem(){
		
	}


	public String getListItemBrand() {
		return listItemBrand;
	}


	public void setListItemBrand(String listItemBrand) {
		this.listItemBrand = listItemBrand;
	}


	public int getListsItemID() {
		return listsItemID;
	}


	public void setListsItemID(int listsItemID) {
		this.listsItemID = listsItemID;
	}


	public String getListsItemName() {
		return listsItemName;
	}


	public void setListsItemName(String listsItemName) {
		this.listsItemName = listsItemName;
	}


	public String getListRoute() {
		return listRoute;
	}


	public void setListRoute(String listRoute) {
		this.listRoute = listRoute;
	}


	public int getSearchItemId() {
		return searchItemId;
	}


	public void setSearchItemId(int searchItemId) {
		this.searchItemId = searchItemId;
	}


	public int getListFK() {
		return listFK;
	}


	public void setListFK(int listFK) {
		this.listFK = listFK;
	}
	
	

//TODO change 
	@Override
	public String toString() {
		return "[listsItemID=" + listsItemID + ", listsItemName="
				+ listsItemName + ", listRoute=" + listRoute
				+ ", searchItemId=" + searchItemId + ", listFK=" + listFK + "]";
	}
	
	
	
	
	
	
}

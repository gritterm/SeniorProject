package net.shoppier;
/*
 * This is represents a List Item Object 
 * 
 */
public class ListsItem {
	
	//The PK of the list item for Search re
	String listsItemID; 
	
	//The List Item Name
	String listsItemName; 
	
	//The List route 
	String listRoute;
	
	//The Search item ID  if it exist
	String searchItemId; 
	
	//The list it belongs to 
	String listFK; 
	
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


	public String getListsItemID() {
		return listsItemID;
	}


	public void setListsItemID(String listsItemID) {
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


	public String getSearchItemId() {
		return searchItemId;
	}


	public void setSearchItemId(String searchItemId) {
		this.searchItemId = searchItemId;
	}


	public String getListFK() {
		return listFK;
	}


	public void setListFK(String listFK) {
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

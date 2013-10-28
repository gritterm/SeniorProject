package net.shoppier;
/*
 * This is represents a List Item Object 
 * 
 */
public class Lists {
	
	//The PK of the list item 
	String listsItemID; 
	
	//The List Item Name
	String listsItem; 
	
	//The List route 
	String listRoute;
	
	//The Search item ID  if it exist
	String searchItemId; 
	
	@Override
	public String toString() {
		return "Lists [listsItemID=" + listsItemID + ", listsItem=" + listsItem
				+ ", listRoute=" + listRoute + ", searchItemId=" + searchItemId
				+ "]";
	}

	public String getListsItemID() {
		return listsItemID;
	}

	public void setListsItemID(String listsItemID) {
		this.listsItemID = listsItemID;
	}

	public String getListsItem() {
		return listsItem;
	}

	public void setListsItem(String listsItem) {
		this.listsItem = listsItem;
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

	public Lists(){
		
	}
	
	public Lists(String listID, String listText, String listRoute, String searchItemID){
		this.listsItemID = listID; 
		this.listsItem = listText;
		this.listRoute = listRoute; 
		this.searchItemId = searchItemID; 
	}



	
	
	
	
}

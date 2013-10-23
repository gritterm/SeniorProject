package net.shoppier;

public class Lists {
	
	String listsItemID; 
	String listsItem; 
	
	public Lists(){
		
	}
	
	public Lists(String listID, String listText){
		this.listsItem = listID; 
		this.listsItem = listText;
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

	public void setListItem(String listsItem) {
		this.listsItem = listsItem;
	}

	@Override
	public String toString() {
		return "[listsItemID=" + listsItemID + ", listsItem=" + listsItem
				+ "]";
	}
	
	
	
	
}

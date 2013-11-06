package net.shoppier;

public class CompleteList {
	
	
	int listPK; // list primary key 
	
	String listName; //name of list
	
	String listRoute; // route for each list 
	
	boolean isChanged; // boolean to tell weather the list has been changed 

	public CompleteList(){
		
	}
	public CompleteList(int listPK, String listName, String listRoute,
			boolean isChanged) {
		super();
		this.listPK = listPK;
		this.listName = listName;
		this.listRoute = listRoute;
		this.isChanged = isChanged;
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
	
	

}

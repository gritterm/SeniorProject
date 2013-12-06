package net.shoppier;

public class SearchableItem {
	
	int itemID; 
	String itemName; 
	String itemBrand; 
	String itemCat; 
	StoreObject store; 
	
	public SearchableItem(){
		
	}

	public SearchableItem(int itemID, String itemName,
			String itemBrand, String itemCat, StoreObject store) {
		super();
		this.itemID = itemID;
		this.itemName = itemName;
		this.itemBrand = itemBrand;
		this.itemCat = itemCat;
		this.store = store; 
	}

	
	public StoreObject getStore() {
		return store;
	}

	public void setStore(StoreObject store) {
		this.store = store;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}


	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public String getItemCat() {
		return itemCat;
	}

	public void setItemCat(String itemCat) {
		this.itemCat = itemCat;
	}

	@Override
	public String toString() {
		return "[itemID=" + itemID 
				 + ", itemName=" + itemName + ", itemBrand="
				+ itemBrand + ", itemCat=" + itemCat + "]";
	}
	
	

}

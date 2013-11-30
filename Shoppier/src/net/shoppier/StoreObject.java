package net.shoppier;


public class StoreObject {
	
	int storePK;  //store Primary key
	
	String storeName; //store name 
	
	String storeAddress; //store address
	
	String storeCity; //store city
	
	int storeZipCode; //store zipcode
	
	String storeType; //store type
	
	String storeImage; //store map
	
	

	public StoreObject(int storePK, String storeName, String storeAddress,
			String storeCity, int storeZipCode, String storeType,
			String storeImage) {
		super();
		this.storePK = storePK;
		this.storeName = storeName;
		this.storeAddress = storeAddress;
		this.storeCity = storeCity;
		this.storeZipCode = storeZipCode;
		this.storeType = storeType;
		this.storeImage = storeImage;
	}

	public StoreObject() {
	}

	@Override
	public String toString() {
		return storeName + ", " + storeCity;
	}

	public int getStorePK() {
		return storePK;
	}

	public void setStorePK(int storePK) {
		this.storePK = storePK;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreCity() {
		return storeCity;
	}

	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}

	public int getStoreZipCode() {
		return storeZipCode;
	}

	public void setStoreZipCode(int storeZipCode) {
		this.storeZipCode = storeZipCode;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreImage() {
		return storeImage;
	}

	public void setStoreImage(String storeImage) {
		this.storeImage = storeImage;
	} 
	
	
}

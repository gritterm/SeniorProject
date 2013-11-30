package net.shoppier;

public class AisleObject {
	int aislePK; //aisle primary key  
	
	String aisleName; //aisle name
	
	int aisleStoreFK; //store associated with aisle

	
	public AisleObject(int aislePK, String aisleName, int aisleStoreFK) {
		super();
		this.aislePK = aislePK;
		this.aisleName = aisleName;
		this.aisleStoreFK = aisleStoreFK;
	}
	
	public AisleObject(){
		
	}
	
	
	@Override
	public String toString() {
		return  aisleName;
	}

	public int getAislePK() {
		return aislePK;
	}

	public void setAislePK(int aislePK) {
		this.aislePK = aislePK;
	}

	public String getAisleName() {
		return aisleName;
	}

	public void setAisleName(String aisleName) {
		this.aisleName = aisleName;
	}

	public int getAisleStoreFK() {
		return aisleStoreFK;
	}

	public void setAisleStoreFK(int aisleStoreFK) {
		this.aisleStoreFK = aisleStoreFK;
	}
	
	
}

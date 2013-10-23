package net.shoppier;

public class GrocItem {
	public StringBuffer name;
	public StringBuffer brand;
	public float size;
	
	public String toString() {
		return "" + brand +" "+ name +" "+ size +"oz.";
	}
}

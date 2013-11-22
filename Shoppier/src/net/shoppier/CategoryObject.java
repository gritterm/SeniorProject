package net.shoppier;

public class CategoryObject {
	
	int cat_pk;
	
	int cat_locfk;
	
	String cat_name;
	
	int cat_value; 
	
	String cat_x;
	
	String cat_y;
	
	

	public CategoryObject(int cat_pk, int cat_locfk, String cat_name,
			int cat_value, String cat_x, String cat_y) {
		super();
		this.cat_pk = cat_pk;
		this.cat_locfk = cat_locfk;
		this.cat_name = cat_name;
		this.cat_value = cat_value;
		this.cat_x = cat_x;
		this.cat_y = cat_y;
	}
	
	public CategoryObject(){
		
	}

	@Override
	public String toString() {
		return cat_name;
	}

	public int getCat_pk() {
		return cat_pk;
	}

	public void setCat_pk(int cat_pk) {
		this.cat_pk = cat_pk;
	}

	public int getCat_locfk() {
		return cat_locfk;
	}

	public void setCat_locfk(int cat_locfk) {
		this.cat_locfk = cat_locfk;
	}

	public String getCat_name() {
		return cat_name;
	}

	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}

	public int getCat_value() {
		return cat_value;
	}

	public void setCat_value(int cat_value) {
		this.cat_value = cat_value;
	}

	public String getCat_x() {
		return cat_x;
	}

	public void setCat_x(String cat_x) {
		this.cat_x = cat_x;
	}

	public String getCat_y() {
		return cat_y;
	}

	public void setCat_y(String cat_y) {
		this.cat_y = cat_y;
	}
	
	
}

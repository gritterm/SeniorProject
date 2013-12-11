package net.shoppier.DrawerClasses;

import android.content.Context;

public class NavMenuItem implements NavDrawerItem {

	public static final int LIST_TYPE = 1;
	public static final int AddLIST_TYPE = 2;
	public static final int LOGOUT_TYPE = 3;
	public static final int SYNC_TYPE = 4;
	public static final int LOGIN_TYPE = 5;
	public static final int ADDTODB_TYPE = 6;
	public static final int HELP_PAGE = 7; 
	public static final int ABOUT_PAGE = 8;

	private int id;
	private String label;
	private int icon;
	private boolean updateActionBarTitle;

	public NavMenuItem() {
	}

	public static NavMenuItem create(int id, String label, String icon,
			boolean updateActionBarTitle, Context context) {
		NavMenuItem item = new NavMenuItem();
		item.setId(id);
		item.setLabel(label);
		item.setIcon(context.getResources().getIdentifier(icon, "drawable",
				context.getPackageName()));
		item.setUpdateActionBarTitle(updateActionBarTitle);
		return item;
	}

	@Override
	public int getType() {

		if (id == 201) {
			return AddLIST_TYPE;
		} else if (id == 202) {
			return LOGOUT_TYPE;
		} else if (id == 203) {
			return SYNC_TYPE;
		} else if (id == 204) {
			return LOGIN_TYPE;
		} else if (id == 205){
			return ADDTODB_TYPE;
		}else if(id == 205){
			return HELP_PAGE;
		}else if(id == 206){
			return ABOUT_PAGE;
		}
		return LIST_TYPE;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean updateActionBarTitle() {
		return this.updateActionBarTitle;
	}

	public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
		this.updateActionBarTitle = updateActionBarTitle;
	}
}
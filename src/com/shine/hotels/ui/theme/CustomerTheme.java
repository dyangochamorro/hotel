package com.shine.hotels.ui.theme;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;

public class CustomerTheme {

	Context themeContext;
	Resources themeResources;
	String themePkgName;

	public CustomerTheme(Context context, String pkgName) {
		themeContext = context;
		themePkgName = pkgName;
		themeResources = context.getResources();
	}
	
	public View getLayoutView(String resName) {

		int id = getIdentifier("layout", resName);

		return ((LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(id,
				null);
	}

	public int getIdentifier(String resType, String resName) {
		return themeResources.getIdentifier(resName, resType, themePkgName);
	}
}

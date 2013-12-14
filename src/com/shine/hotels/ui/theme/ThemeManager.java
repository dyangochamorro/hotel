package com.shine.hotels.ui.theme;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class ThemeManager {

    private static ThemeManager sThemeManagerInstance;
    
    private Context mSelfContext;
//    private Context mThemeContext;
//    private Resources mThemeResources;
//    private String mThemePkgName;
    private CustomerTheme mTheme;
    
    private ThemeManager(Context context) {
        mSelfContext = context;
    }
    
    public synchronized static ThemeManager get(Context context) {
        if (sThemeManagerInstance == null) {
            sThemeManagerInstance = new ThemeManager(context);
        }
        
        return sThemeManagerInstance;
    }
    
    public synchronized void setTheme(String pkgName) {
        try {
            Context themeContext = mSelfContext.createPackageContext(pkgName,
                    Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            mTheme = new CustomerTheme(themeContext, pkgName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
//    public View getLayoutView(String resName) {
//        
//        int id = getIdentifier("layout", resName);
//        
//        return ((LayoutInflater) mThemeContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(id,
//                null);
//    }
//    
//    public int getIdentifier(String resType, String resName) {
//        return mThemeResources.getIdentifier(resName, resType, mThemePkgName);
//    }
    /**
     * 判断是否加载了主题包
     * @return
     */
    public boolean hasTheme() {
    	return mTheme != null;
    }
    
    public CustomerTheme getTheme() {
//    	return mTheme;
    	return null ;
    }
    
}

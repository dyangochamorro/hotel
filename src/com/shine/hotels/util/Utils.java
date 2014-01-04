package com.shine.hotels.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.shine.hotels.center.TravelInfoCenter;

public class Utils {
	public static final String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"} ;
	public static final String[] weekDays_en = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"} ;
	
	/**
	 * 根据配置获取当前时间
	 * @param format
	 * @return
	 */
	//@SuppressLint("SimpleDateFormat")
	public static String getDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format) ;       
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间    
        
        return formatter.format(curDate);  
	}
	
	public final static String format(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	public final static Date parser(String datetime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final static Date parserYMDHMS(String datetime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			return format.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 比较两个时间： 如果A时间大于B时间返回1 如果A时间小于B时间返回-1 如果A时间等于B时间返回0 否则返回null
	 */
	public final static Integer compareTo(Date dateA, String datetime) {
		Integer result = null;
		if (dateA != null || datetime != null) {
			String ad = format(dateA);
			String a = ad.replaceAll("-", "").replaceAll(":", "").replaceAll(
					" ", "");
			String b = datetime.replaceAll("-", "").replaceAll(":", "")
					.replaceAll(" ", "");
			if (a.indexOf(".") != -1) {
				a = a.substring(0, a.indexOf("."));
			}
			if (b.indexOf(".") != -1) {
				b = b.substring(0, b.indexOf("."));
			}
			Long at = Long.parseLong(a);
			Long bt = Long.parseLong(b);
			if (at > bt)
				result = 1;
			else if (at < bt)
				result = -1;
			else
				result = 0;
			ad = null;
			datetime = null;
			a = null;
			b = null;
			at = null;
			bt = null;
		}
		return result;
	}
	
	 /**
	  * 根据当前日期获取星期几
	  * @return
	  */
	 public static String getWeek(int language) {
         Calendar cal = Calendar.getInstance() ;
         int index = cal.get(Calendar.DAY_OF_WEEK) - 1 ;
         if (index < 0){
        	 index = 0 ;
         }
         if (language==1) {
        	 return weekDays_en[index] ;
         } else {
        	 return weekDays[index] ;
         }
	 }
	 
	 /** 
		 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
		 */  
		    public static int dip2px(Context context, float dpValue) {  
		        final float scale = context.getResources().getDisplayMetrics().density;  
		        return (int) (dpValue * scale + 0.5f);  
		    }  
		  
		    /** 
		 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
		 */  
		public static int px2dip(Context context, float pxValue) {  
		    final float scale = context.getResources().getDisplayMetrics().density;  
		    return (int) (pxValue / scale + 0.5f);  
		} 
	 
	 public static void sendLocalBrodcast(Context context, Intent localIntent) {
//	     Log.d("shine", "sendLocalBrodcast ");
	     LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
	 }
	 
	 public static void sendGolbalBrodcast(Context context, Intent intent) {
         context.sendBroadcast(intent);
     }
	 
	 public static String getCityFromCode(Context context, String code) {
	     String city = "";
	     
	     int i = 0;
	     for (; i < TravelInfoCenter.codes.length; i++) {
	         if (code.equalsIgnoreCase(TravelInfoCenter.codes[i])) break;
	     }
	     
	     city = TravelInfoCenter.cities[i];
	     
	     return city;
	 }
	 
	 public static String formatTime(int time) {
	        if (time < 1) {
	            return "00:00";
	        }
	        
//	        time /= 1000;
	        int hour = time / 3600;
	        int suf = time % 3600;
	        int min = suf / 60;
	        int sec = suf % 60;
	        
	        if (hour > 0) {
	            return String.format("%02d:%02d:%02d", hour, min, sec);
	        } else {
	            return String.format("%02d:%02d", min, sec);
	        }
	    }
	 
	 public static boolean isNetworking(Context context) {
	        ConnectivityManager cManager = (ConnectivityManager)context.getSystemService(
	                Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
	        if (info != null && info.isAvailable()) {
	            // do something
	            Log.v("shine", "networking true");
	            return true;
	        } else {
	            // do something
	            Log.v("shine", "networking false");
	            return false;
	        }
	    }
}

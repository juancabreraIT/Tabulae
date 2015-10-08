package com.haya.tabulae.utils;

import com.haya.tabulae.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public final class Utils {
		
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setActionBar(Activity activity, int background) {
		
		// Action Bar Icon
		activity.getActionBar().setIcon(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
		
		// Action Bar Background
		Drawable draw;
        if(android.os.Build.VERSION.SDK_INT >= 21){
        	draw = activity.getResources().getDrawable(background, activity.getTheme());
        	activity.getActionBar().setBackgroundDrawable(draw);
        } else {
        	draw = activity.getResources().getDrawable(background);
        	activity.getActionBar().setBackgroundDrawable(draw);
        }
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setActionBar(Activity activity, int background, String title) {
		
		// Action Bar Icon
		activity.getActionBar().setIcon(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
		
		// Action Bar Title
		activity.getActionBar().setTitle(title);
		
		// Action Bar Background
		Drawable draw;
        if(android.os.Build.VERSION.SDK_INT >= 21){
        	draw = activity.getResources().getDrawable(background, activity.getTheme());
        	activity.getActionBar().setBackgroundDrawable(draw);
        } else {
        	draw = activity.getResources().getDrawable(background);
        	activity.getActionBar().setBackgroundDrawable(draw);
        }
	}

	public static void setActionBarBack(Activity activity) {
		activity.getActionBar().setHomeButtonEnabled(true);
		activity.getActionBar().setLogo(R.drawable.ic_arrow_back);
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackground(Activity activity, int background) {
		
		activity.getActionBar().setIcon(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
		
		Drawable draw;
        if(android.os.Build.VERSION.SDK_INT >= 21){
        	draw = activity.getResources().getDrawable(background, activity.getTheme());
        	activity.getActionBar().setBackgroundDrawable(draw);
        } else {
        	draw = activity.getResources().getDrawable(background);
        	activity.getActionBar().setBackgroundDrawable(draw);
        }
	}

	public final static String DEFAULT_LIST = "My quick list";
	public final static int ITEM_DETAIL = 101;
	public final static int MARKET_DETAIL = 102;
	
	public final static String[] drawerList = { "Pending shopping",
												"My items", 
												"My markets", 												
												"Settings", 
												"About",
												"Donate"};
	public final static int[] drawerImg = {
			
			R.drawable.ic_shopping_basket_white_36dp,
			R.drawable.ic_local_offer_white_36dp,
			R.drawable.ic_store_mall_directory_white_36dp,			
			R.drawable.ic_settings_white_36dp, 
			R.drawable.ic_error_outline_white_36dp,
			R.drawable.ic_card_giftcard_white_36dp				
	};
}

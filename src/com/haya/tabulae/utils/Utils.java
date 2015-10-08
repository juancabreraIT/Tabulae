package com.haya.tabulae.utils;

import com.haya.tabulae.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;

public final class Utils {

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackground(Activity activity, int background) {
		
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

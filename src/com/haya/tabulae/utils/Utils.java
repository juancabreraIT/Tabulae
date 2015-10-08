package com.haya.tabulae.utils;

import com.haya.tabulae.R;
import com.haya.tabulae.activities.ItemsActivity;
import com.haya.tabulae.activities.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public final class Utils {
	
	public static void goToSection(Activity activity, int position) {		
		
		String className = activity.getClass().toString();
		
		switch(position) {		
			case 0:
				if ( className.equals(MainActivity.class.toString() ) ) {
					Intent intent = new Intent(activity, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					activity.startActivity(intent);					
				}				
			break;

			case 1:
				if ( className.equals(ItemsActivity.class.toString() ) ) {
					Intent intent = new Intent(activity, ItemsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					activity.startActivity(intent);
				}
			break;
			
//			case 3:
//				if ( className.equals(MarketsActivity.class.toString() ) ) {
//					Intent intent = new Intent(activity, ItemsActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					activity.startActivity(intent);
//				}
//			break;
			
//			case 4:
//				if ( className.equals(ItemsActivity.class.toString() ) ) {
//					Intent intent = new Intent(activity, Settings.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					activity.startActivity(intent);
//				}
//			break;
			
//			case 5:
//				if ( className.equals(ItemsActivity.class.toString() ) ) {
//					Intent intent = new Intent(activity, About.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					activity.startActivity(intent);
//				}
//			break;
			
//			case 6:
//				if ( className.equals(ItemsActivity.class.toString() ) ) {
//					Intent intent = new Intent(activity, Donate.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					activity.startActivity(intent);
//				}
//			break;

			default:
				Toast.makeText(activity, drawerList[position] + " pushed", Toast.LENGTH_LONG).show();
		}
	}
	
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

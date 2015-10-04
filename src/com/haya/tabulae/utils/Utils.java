package com.haya.tabulae.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;

public final class Utils {

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
}

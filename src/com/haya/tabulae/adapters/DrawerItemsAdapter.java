package com.haya.tabulae.adapters;

import com.haya.tabulae.R;
import com.haya.tabulae.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class DrawerItemsAdapter extends ArrayAdapter<String> {
	
	private Context context;

	public DrawerItemsAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);		
		setBackground(v, R.drawable.row_states);

		getItem(position);
		
		ImageView image = (ImageView) v.findViewById(R.id.imgDrawerItem);
		if ( position < Utils.drawerImg.length ) {
			image.setImageResource(Utils.drawerImg[position]);
		}

		return v;
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setBackground(View v, int background) {
		
		Drawable draw;
        if(android.os.Build.VERSION.SDK_INT >= 21){
        	draw = context.getResources().getDrawable(background, context.getTheme());
        	v.setBackground(draw);
        } else {
        	draw = context.getResources().getDrawable(background);
        	v.setBackgroundDrawable(draw);
        }
	}

}

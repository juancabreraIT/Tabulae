package com.haya.tabulae.adapters;

import java.util.ArrayList;

import com.haya.tabulae.R;
import com.haya.tabulae.models.Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ItemAdapter extends ArrayAdapter<Item> {
	
	private Context context;

	public ItemAdapter(Context context, int resource, int textViewResourceId, ArrayList<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);		
		setBackground(v, R.drawable.row_states);

//		TextView itemName = (TextView) v.findViewById(R.id.ItemTitle);
//		itemName.setText(getItem(position).getName());

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

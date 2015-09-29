package com.haya.tabulae.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.haya.tabulae.R;
import com.haya.tabulae.models.Item;

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

		TextView itemName = (TextView) v.findViewById(R.id.ItemTitle);
		
		Log.d("Tabulae", "Count" + getCount());
		Log.d("Tabulae", "Position" + position);
		
		itemName.setText(getItem(position).
				getName());

		CheckBox checkBox = (CheckBox) v.findViewById(R.id.Check);
		checkBox.setTag(position);

		return v;
	}
	
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

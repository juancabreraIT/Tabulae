package com.haya.tabulae.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.haya.tabulae.R;
import com.haya.tabulae.models.Item;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

@SuppressLint("UseSparseArrays")
public class ItemAdapter extends ArrayAdapter<Item> {
	
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	private Context context;

	public ItemAdapter(Context context, int resource, int textViewResourceId, ArrayList<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}	
	
	public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }
	
	public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }
	
	public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }	
	
	public void selectAll(int size) {
    	mSelection = new HashMap<Integer, Boolean>();
    	
    	for(int i = 0; i < size; i++) {
    		mSelection.put(i, true);
    	}
    	notifyDataSetChanged();    	
    }

	public boolean isPositionSelected(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }	

	public Set<Integer> getCurrentSelectedPosition() {
        return mSelection.keySet();
    }	
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);
		setBackground(v, R.drawable.row_states);

		if ( mSelection.get(position) != null && mSelection.get(position) ) {			
        	v.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
		return v;
	}

	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setBackground(View v, int background) {
		
		Drawable draw;
        if(android.os.Build.VERSION.SDK_INT >= 21){
//        	draw = context.getResources().getDrawable(background, context.getTheme());
        	draw = ContextCompat.getDrawable(context, background);
//        	v.setBackground(draw);
        	v.setBackgroundDrawable(draw);
        } else {
        	draw = context.getResources().getDrawable(background);
        	v.setBackgroundDrawable(draw);
        }
	}
	
}

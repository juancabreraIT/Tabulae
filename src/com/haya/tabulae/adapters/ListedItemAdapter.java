package com.haya.tabulae.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.haya.tabulae.R;
import com.haya.tabulae.models.ListedItem;
import com.haya.tabulae.models.Market;
import com.haya.tabulae.models.Price;

public class ListedItemAdapter extends ArrayAdapter<ListedItem> {
	
	private Context context;
	private Spinner spinner;
	
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	
	
	public ListedItemAdapter(Context context, int resource, int textViewResourceId, ArrayList<ListedItem> objects, Spinner spinner) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.spinner = spinner;
	}	
	
    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }
	
    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }
    
    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
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
    
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);		
		setBackground(v, R.drawable.row_states);
		
		ListedItem listedItem = getItem(position);

		TextView itemName = (TextView) v.findViewById(R.id.ItemTitle);				
		itemName.setText(listedItem.getItem().getName());
		
		CheckBox checkBox = (CheckBox) v.findViewById(R.id.Check);
		checkBox.setTag(position);
		
		if ( checkBox.isChecked() ) {
			itemName.setPaintFlags( itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			itemName.setPaintFlags(itemName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
		}
		
		if (mSelection.get(position) != null) {
        	v.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
		ImageView dolarImg = (ImageView) v.findViewById(R.id.imageDolar);
				
		Market selectMarket = (Market) spinner.getSelectedItem();
		
		for(Price price : listedItem.getItem().prices()) {
			if ( price.getMarket().equals(selectMarket) ) {
				dolarImg.setVisibility(View.VISIBLE);
				return v;
			}
		}

		dolarImg.setVisibility(View.INVISIBLE);

		return v;
	}
		
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
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

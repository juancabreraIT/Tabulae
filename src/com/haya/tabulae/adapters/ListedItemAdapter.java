package com.haya.tabulae.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.haya.tabulae.R;
import com.haya.tabulae.models.ListedItem;
import com.haya.tabulae.models.Market;
import com.haya.tabulae.models.Price;

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

@SuppressLint("UseSparseArrays")
public class ListedItemAdapter extends ArrayAdapter<ListedItem> {
	
	private Context context;
	private Spinner spinner;
	
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	private HashMap<Integer, Boolean> mChecked = new HashMap<Integer, Boolean>();
	
	public ListedItemAdapter(Context context, int resource, int textViewResourceId, ArrayList<ListedItem> objects, Spinner spinner) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.spinner = spinner;
		
		for(int i = 0; i < objects.size(); i++) {
			mChecked.put(i, objects.get(i).isChecked());
		}
	}	
	
	// Selections
    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }
	
    public boolean isPositionSelected(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }
    
    public Set<Integer> getCurrentSelectedPosition() {
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
    
    // Checks
    public boolean setNewChecked(int position) {
    	    	
    	if ( !mChecked.containsKey(position) ) {
    		mChecked.put(position, true);
    	} else { 
    		mChecked.put(position, !mChecked.get(position) );
    	}
    	
    	return mChecked.get(position);
    }
    
    private boolean isPositionChecked(int position) {
    	Boolean result = mChecked.get(position);
        return (result == null || result == false) ? false : true;
    }

    public ArrayList<Integer> getCurrentCheckedPosition() {
    	
    	ArrayList<Integer> checkedList = new ArrayList<Integer>();
    	
    	Set<Integer> positions = mChecked.keySet();
    	Iterator<Integer> it = positions.iterator();
    	
    	while(it.hasNext()) {
    		int position = it.next();
    		if ( mChecked.get(position) ) {
    			checkedList.add(position);
    		}
    	}    	
    	
    	return checkedList;
    }

    public void clearChecks() {
    	mChecked = new HashMap<Integer, Boolean>();
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
		
		if ( isPositionChecked(position) ) {
			itemName.setPaintFlags( itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			checkBox.setChecked(true);
		} else {
			itemName.setPaintFlags(itemName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			checkBox.setChecked(false);
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

package com.haya.tabulae.adapters;

import java.util.ArrayList;

import com.haya.tabulae.models.Item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ItemAdapter extends ArrayAdapter<Item> {
	

	public ItemAdapter(Context context, int resource, int textViewResourceId, ArrayList<Item> objects) {
		super(context, resource, textViewResourceId, objects);
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);		

		return v;
	}

}

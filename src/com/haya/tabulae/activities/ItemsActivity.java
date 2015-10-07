package com.haya.tabulae.activities;

import java.util.ArrayList;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.adapters.ItemAdapter;
import com.haya.tabulae.models.Item;
import com.haya.tabulae.utils.Utils;

import android.app.ListActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ItemsActivity extends ListActivity {

	private ItemAdapter adapterList;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_items);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		Utils.setBackground(this, android.R.color.holo_purple);
		
		populateList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void populateList() {
		
		ArrayList<Item> items = new Select().from(Item.class).execute();		
		adapterList = new ItemAdapter(this, R.layout.item, R.id.ItemTitle, items);
		getListView().setAdapter(adapterList);
	}
	
}

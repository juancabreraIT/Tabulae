package com.haya.tabulae.activities;

import java.util.ArrayList;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.adapters.ItemAdapter;
import com.haya.tabulae.models.Item;
import com.haya.tabulae.utils.Utils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class ItemsActivity extends ListActivity {

	private ArrayList<Item> allItems;
	private ItemAdapter adapterList;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_items);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		Utils.setBackground(this, android.R.color.holo_purple);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(R.drawable.ic_arrow_back);
		
		populateList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		
		switch(id) {
			case R.id.action_add:
				addItemDialog();
				break;
			case android.R.id.home:
				finish();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	private void populateList() {
		
		allItems = new Select().from(Item.class).execute();		
		adapterList = new ItemAdapter(this, R.layout.item, R.id.ItemTitle, allItems);
		getListView().setAdapter(adapterList);
	}

	
	// Item
	private void addItemDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;
		builder.setTitle(getResources().getText(R.string.newItem));

		ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_dropdown_item_1line, allItems);

		final AutoCompleteTextView autoText = new AutoCompleteTextView(this);
		autoText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		autoText.requestFocus();
		autoText.setAdapter(adapter);
		autoText.setThreshold(2);

		builder.setView(autoText);

		// OK
		builder.setPositiveButton(getResources().getText(android.R.string.ok), new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	String itemName = autoText.getText().toString().trim();
		    	if ( !itemName.isEmpty() ) {
		    		addItem(itemName);
		    	}
		    }
		});
		
		// CANCEL
		builder.setNegativeButton(getResources().getText(android.R.string.cancel), new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		dialog = builder.create();
		dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
	}

	private void addItem(String itemName) {

		Item item = new Select().from(Item.class).where("name = ?", itemName).executeSingle();

		if ( item == null ) {
			item = new Item(itemName);
			item.save();						
			allItems.add(item);
			adapterList.notifyDataSetChanged();
		} else {
			Toast.makeText(this, "Item already exists!", Toast.LENGTH_LONG).show();
		}		
	}

}

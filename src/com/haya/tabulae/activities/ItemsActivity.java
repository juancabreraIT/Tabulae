package com.haya.tabulae.activities;

import java.util.ArrayList;
import java.util.Iterator;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.adapters.ItemAdapter;
import com.haya.tabulae.models.Item;
import com.haya.tabulae.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

public class ItemsActivity extends ListActivity implements OnItemClickListener {

	private ArrayList<Item> allItems;
	private ItemAdapter adapterList;
	
	private ActionMode actionMode;
	
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
		
		getListView().setOnItemClickListener(this);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		if ( actionMode != null ) {
			actionMode.finish();
		}
		
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			private int numSelected = 0;

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}			
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				adapterList.clearSelection();
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				numSelected = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_contextual, menu);
                actionMode = mode;
                return true;
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {

                case R.id.item_delete:
                	deleteItemsDialog(numSelected, mode);
                    numSelected = 0;
                    break;

                case R.id.item_select_all:
                	checkAllItems();
                	numSelected = allItems.size();
                	adapterList.selectAll(numSelected);
                	mode.setTitle(numSelected + " selected");
                	break;
            	}                
            	return false;
			}
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				if ( checked ) {
                    numSelected++;                    
                } else {
                    numSelected--;
                }				
				adapterList.setNewSelection(position, checked);
                mode.setTitle(numSelected + " selected");
            	mode.invalidate();                
			}

		});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				getListView().setItemChecked(position, !adapterList.isPositionSelected(position));
				return false;
			}
		});	
		
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
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {			
			case (Utils.ITEM_DETAIL) :
				if (resultCode == Activity.RESULT_OK) {
					adapterList.notifyDataSetChanged();
				}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Intent intent = new Intent(this, ItemDetailActivity.class);
		long idItem = allItems.get(position).getId();
		intent.putExtra("idItem", idItem);
		
		startActivityForResult(intent, Utils.ITEM_DETAIL);
	}
	
	private void checkAllItems() {
		
		for(int i = 0; i < getListView().getCount(); i++) {
			getListView().setItemChecked(i, true);
		}		
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

		
	private void deleteItemsDialog(int numItems, final ActionMode mode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;
		String title = getText(R.string.deleteItem).toString() + " " + numItems + " ";
		title += numItems > 1 ? getResources().getText(R.string.items) : getResources().getText(R.string.item); 
		builder.setTitle(title);

		// OK
		builder.setPositiveButton(getResources().getText(android.R.string.ok), new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	deleteItem(mode);
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
		dialog.show();		
	}

	private void deleteItem(ActionMode mode) {
		
		Iterator<Integer> it = adapterList.getCurrentSelectedPosition().iterator();
        ArrayList<Item> deletedItems = new ArrayList<Item>();
        
        while ( it.hasNext() ) {
        	int index = it.next().intValue();
        	Item temp = allItems.get(index);
        	deletedItems.add(temp);
        	temp.delete();
        }
        
        allItems.removeAll(deletedItems);
        adapterList.clearSelection();
        mode.finish();        
	}

}

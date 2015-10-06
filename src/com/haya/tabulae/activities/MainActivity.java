package com.haya.tabulae.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.adapters.ListedItemAdapter;
import com.haya.tabulae.models.Item;
import com.haya.tabulae.models.ListedItem;
import com.haya.tabulae.models.Market;
import com.haya.tabulae.models.Price;
import com.haya.tabulae.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnItemClickListener {

	// Mock
	final static String DEFAULT_LIST = "My quick list";
	private String[] mPlanetTitles = {"My items", "My markets", "Settings", "About"};

	@SuppressWarnings("unused")
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
		
	@SuppressWarnings("unused")
	private ActionMode actionMode;
	
	private Spinner marketSpinner;
	private TextView price;

	private ArrayList<ListedItem> listedItems;
	private ListedItemAdapter adapterListedItems;

	private ArrayList<Item> allItems = new ArrayList<Item>();

	private ArrayList<Market> markets;
	private ArrayAdapter<Market> adapterSpinner;

	private final int NEW_MARKET_RESULT = 100;
	private final int ITEM_DETAIL = 101;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Utils.setBackground(this, android.R.color.holo_purple);
		// Keep the screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		init();
		loadData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

		startItemDetail(position);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) { 
			case R.id.action_add:
				addItemDialog();
				return true;			
			
			case R.id.action_clear_list:
				Toast.makeText(this, "Clear list", Toast.LENGTH_SHORT).show();
				clearAllItems();
				break;
			case R.id.action_clear_checked_items:
				Toast.makeText(this, "Clear selected items", Toast.LENGTH_SHORT).show();
				clearCheckedItems();
				break;
		}		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {
			case (NEW_MARKET_RESULT) :
			      if (resultCode == Activity.RESULT_OK) {
			    	  loadMarkets();
			      }
			      break;
			case (ITEM_DETAIL) :
				if (resultCode == Activity.RESULT_OK) {
					recalculatePrice();
				}
		}
	}

	
	private void init() {

		marketSpinner = (Spinner) findViewById(R.id.marketSelector);
		price = (TextView) findViewById(R.id.price);		

		getListView().setOnItemClickListener(this);		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			private int numSelected = 0;
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				MenuItem item = menu.findItem(R.id.item_select_all);
	            if (numSelected == 1) {        	       
	            	item.setVisible(true);	        	   
	        	} else {
	        		item.setVisible(false);	        	      
	        	}   
            	return true;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				adapterListedItems.clearSelection();
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
                	Toast.makeText(getApplicationContext(), "Select all...", Toast.LENGTH_SHORT).show();
                	numSelected = listedItems.size();
                	adapterListedItems.selectAll(numSelected);
                	mode.setTitle(numSelected + " selected");
                	break;
            }                
            return false;
			}
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				if (checked) {
                    numSelected++;
                    adapterListedItems.setNewSelection(position, checked);
                } else {
                    numSelected--;
                    adapterListedItems.removeSelection(position);
                }
                mode.setTitle(numSelected + " selected");
                mode.invalidate();
			}
		});
	
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				getListView().setItemChecked(position, !adapterListedItems.isPositionSelected(position));
				return false;
			}
		});

		marketSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				adapterListedItems.notifyDataSetChanged();
				recalculatePrice();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
	}
	
	// Populate View
	private void loadData() {
		loadDrawer();
		loadItems();		
		loadMarkets();
		loadPrices();
		recalculatePrice();
	}	

	@SuppressLint("NewApi")
	private void loadDrawer() {
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mPlanetTitles));

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), mPlanetTitles[position] + " pushed", Toast.LENGTH_LONG).show();
			}        	
        });
	}		
	
	private void loadItems() {
		
		listedItems = new Select().from(ListedItem.class).execute();
		
		if ( listedItems.isEmpty() ) {
			Log.d("Tabulae", "Loading data base 1st time.");
			Item item = new Item("Tomate");			
			ListedItem temp = new ListedItem(DEFAULT_LIST, item);
			listedItems.add(temp);
			item.save();
			temp.save();
			
			item = new Item("Pan");			
			temp = new ListedItem(DEFAULT_LIST, item);
			listedItems.add(temp);
			item.save();
			temp.save();

			item = new Item("Detergente");
			temp = new ListedItem(DEFAULT_LIST, item);
			listedItems.add(temp);
			item.save();
			temp.save();
		} else {
			Log.d("Tabulae", "Data base was loaded.");
		}
		adapterListedItems = new ListedItemAdapter(this, R.layout.listed_item, R.id.ItemTitle, listedItems, marketSpinner);
		getListView().setAdapter(adapterListedItems);
		
	}

	private void loadMarkets() {
		
		markets = new Select().from(Market.class).execute();
		
		if ( markets.isEmpty() ) {
	
			Market market = new Market("Mercadona");
			markets.add(market);
			market.save();

			adapterSpinner = new ArrayAdapter<Market>(this, R.layout.spinner_item, markets);
			marketSpinner.setAdapter(adapterSpinner);
		} else {
			adapterSpinner = new ArrayAdapter<Market>(this, R.layout.spinner_item, markets);
			marketSpinner.setAdapter(adapterSpinner);
		}
	}

	private void loadPrices() {
		
		ArrayList<Price> prices = new Select().from(Price.class).execute();
		
		if ( prices.isEmpty() ) {
			
			for(ListedItem listedItem : listedItems) {
				Market market = (Market) marketSpinner.getSelectedItem();
				Price price = new Price(listedItem.getItem(), market, (float) Math.random() * 15);
				price.save();				
			}

			adapterSpinner = new ArrayAdapter<Market>(this, R.layout.spinner_item, markets);
			marketSpinner.setAdapter(adapterSpinner);
		}
	}
	
	private void recalculatePrice() {
		
		float totalPrice = 0.0f;
		Market selectedMarket = (Market) marketSpinner.getSelectedItem();
		ArrayList<Price> itemPrices;
				
		for(ListedItem listedItem : listedItems) {
			itemPrices = (ArrayList<Price>) listedItem.getItem().prices();
			
			for(Price price : itemPrices) {
				if ( price.getMarket().equals(selectedMarket) ) {
					totalPrice += price.getPrice();
				}
			}
		}		
		price.setText(totalPrice + "€");
	}	
	
	// Item
	private void addItemDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;
		builder.setTitle(getResources().getText(R.string.newItem));

		allItems = new Select().from(Item.class).execute();

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
			
			ListedItem listedItem = new ListedItem(DEFAULT_LIST, item);
			listedItems.add(listedItem);
			listedItem.save();
			
			startItemDetail(listedItems.size() - 1);
		} else {
			ListedItem listedItem = new ListedItem(DEFAULT_LIST, item);
			listedItems.add(listedItem);
			listedItem.save();
			adapterListedItems.notifyDataSetChanged();
		}
		recalculatePrice();
	}
	
	private void startItemDetail(int position) {
		
		Intent intent = new Intent(this, ItemDetailActivity.class);
		long idItem = listedItems.get(position).getItem().getId();
		intent.putExtra("idItem", idItem);
	
		startActivityForResult(intent, ITEM_DETAIL);
	}
	
	public void checkItem(View v) {

		adapterListedItems.setNewChecked(Integer.valueOf(v.getTag().toString()));
		adapterListedItems.notifyDataSetChanged();
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
				
		Iterator<Integer> it = adapterListedItems.getCurrentSelectedPosition().iterator();
        ArrayList<ListedItem> deletedItems = new ArrayList<ListedItem>();
        
        while ( it.hasNext() ) {
        	int index = it.next().intValue();
        	ListedItem temp = listedItems.get(index);
        	deletedItems.add(temp);
        	temp.delete();
        }
        
        listedItems.removeAll(deletedItems);
        adapterListedItems.clearSelection();
        recalculatePrice();
        mode.finish();        
	}
	
	private void clearAllItems() {
		
		for(ListedItem listedItem : listedItems) {
			listedItem.getItem().risePicks();
			listedItem.getItem().save();
			listedItem.delete();
		}
		
		listedItems.clear();
		adapterListedItems.clearChecks();
		adapterListedItems.notifyDataSetChanged();
	}
	
	private void clearCheckedItems() {
		
		ArrayList<Integer> checkedItems = adapterListedItems.getCurrentCheckedPosition();
		Iterator<Integer> it = checkedItems.iterator();
		ArrayList<ListedItem> deletedItems = new ArrayList<ListedItem>();				
		
		while(it.hasNext()) {
			int position = it.next();
			ListedItem listedItem = listedItems.get(position);					
			listedItem.getItem().risePicks();
			listedItem.getItem().save();
			listedItem.delete();
			deletedItems.add(listedItem);
		}

		adapterListedItems.clearChecks();
		listedItems.removeAll(deletedItems);
		adapterListedItems.notifyDataSetChanged();
	}
	
	// Market
	public void addMarket(View v) {
		
		Intent intent = new Intent(getApplicationContext(), NewMarketActivity.class);
		startActivityForResult(intent, NEW_MARKET_RESULT);
	}
		
}

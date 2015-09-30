package com.haya.tabulae.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.adapters.ListedItemAdapter;
import com.haya.tabulae.models.Item;
import com.haya.tabulae.models.ListedItem;
import com.haya.tabulae.models.Market;

public class ListActivity extends Activity {
	
	private ListView listView;
	private Spinner marketSelector;
	private TextView price;

	private ArrayList<ListedItem> listedItems;
	private ListedItemAdapter adapterListedItems;
	
	private ArrayList<Item> items = new ArrayList<Item>();

	private ArrayList<Market> markets;
	private ArrayAdapter<Market> adapterSpinner;
	
	// Mock
	final static String DEFAULT_LIST = "My quick list";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		getActionBar().setBackgroundDrawable(getDrawable(android.R.color.holo_purple));
		
		init();
		mock();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add) {
			addItemDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	// esto DEBERIA RECOGER EL INTENT DE VUELTA DEL ACTIVITY Y TAL
	@Override
	public void onResume() {
	    super.onResume(); 

	    loadMarkets();	    
	}
	
	private void init() {
		
		listView = (ListView) findViewById(android.R.id.list);
		marketSelector = (Spinner) findViewById(R.id.marketSelector);
		price = (TextView) findViewById(R.id.price);		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				Toast.makeText(getApplicationContext(), listedItems.get(position) + " picked!", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void loadMarkets() {
		
		markets = new Select().from(Market.class).execute();
		
		if ( markets.isEmpty() ) {
			
			Market market = new Market("Mercadona");
			markets.add(market);
			market.save();
						
			market = new Market("Aldi");
			markets.add(market);
			market.save();	
		}

		adapterSpinner = new ArrayAdapter<Market>(this, android.R.layout.simple_spinner_dropdown_item, markets);
		marketSelector.setAdapter(adapterSpinner);
	}
	
	private void addItemDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;
		builder.setTitle(getResources().getText(R.string.newItem));

		items = new Select().from(Item.class).execute();

		ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_dropdown_item_1line, items);

		final AutoCompleteTextView autoText = new AutoCompleteTextView(this);
		autoText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		autoText.requestFocus();
		autoText.setAdapter(adapter);
		autoText.setThreshold(2);

		builder.setView(autoText);

		// OK
		builder.setPositiveButton(getResources().getText(R.string.dialog_ok), new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	String itemName = autoText.getText().toString().trim();
		    	if ( !itemName.isEmpty() ) {		    		
		    		addItem(itemName);
		    	}
		    }
		});
		
		// CANCEL
		builder.setNegativeButton(getResources().getText(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
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
		}

		ListedItem listedItem = new ListedItem(DEFAULT_LIST, item);
		listedItems.add(listedItem);
		listedItem.save();
		adapterListedItems.notifyDataSetChanged();
	}
	
	public void checkItem(View v) {
		
		CheckBox checkB = (CheckBox) v;
		checkB.setChecked(false);	
		int pos = (Integer) v.getTag();
		listedItems.get(pos).delete();
		listedItems.remove(pos);
		
		adapterListedItems.notifyDataSetChanged();
	}

	public void addMarket(View v) {
		
		Intent intent = new Intent(getApplicationContext(), NewMarketActivity.class);		
		startActivity(intent);		
	}
	
	private void mock() {
		
		listedItems = new Select().from(ListedItem.class).execute();
		
		if ( listedItems.isEmpty() ) {
			Log.d("Tabulae", "Loading data base 1st time.");
			// LISTVIEW
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
		adapterListedItems = new ListedItemAdapter(this, R.layout.list_item, R.id.ItemTitle, listedItems);
		listView.setAdapter(adapterListedItems);	
		
		loadMarkets();
		
		// TEXTVIEW
		int num = (int)(Math.random() * 100);
		price.setText(num + "€");
		
	}
	
}

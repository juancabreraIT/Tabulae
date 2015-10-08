package com.haya.tabulae.activities;

import java.util.ArrayList;
import java.util.HashMap;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.models.Item;
import com.haya.tabulae.models.Market;
import com.haya.tabulae.models.Price;
import com.haya.tabulae.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class ItemDetailActivity extends Activity implements OnItemSelectedListener {

	private long idItem;
	private Item item;
	private EditText editName;
	private EditText editNotas;
	private Spinner marketSpinner;
	private EditText textPrice;

	private ArrayList<Market> markets;
	private ArrayAdapter<Market> adapterSpinner;

	private Market selectedMarket;	
	
	private HashMap<Long, Float> tempPrices = new HashMap<Long, Float>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		Utils.setActionBar(this, android.R.color.holo_purple, "Details");
		Utils.setActionBarBack(this);

		editName = (EditText) findViewById(R.id.editItemName);
		editNotas = (EditText) findViewById(R.id.editItemNotes);
		marketSpinner = (Spinner) findViewById(R.id.spinnerMarket);		
		textPrice = (EditText) findViewById(R.id.textPrice);

		marketSpinner.setOnItemSelectedListener(this);
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_item_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch(id) {
			case R.id.action_save:
				saveItem();
				break;
			case android.R.id.home:
				finish();
				break;
		}	
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		selectedMarket = (Market) marketSpinner.getSelectedItem();				
		populatePrice();
		storeTempPrice();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {	}
	
	
	private void init() {

		idItem = getIntent().getExtras().getLong("idItem");
		item = new Select().from(Item.class).where("id = ?", idItem).executeSingle();

		if ( item == null ) {
			Toast.makeText(this, "Ups.. something went wrong :_(", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		markets = new Select().from(Market.class).execute();
		populateNameNotes();
		populateMarkets();
		selectedMarket = (Market) marketSpinner.getSelectedItem();
		populatePrice();
	}

	private void populateNameNotes() {
		
		editName.setText(item.getName());

		if ( item.getNotes() != null ) {
			editNotas.setText(item.getNotes());
		}		
	}

	private void populateMarkets() {
		
		if ( markets.isEmpty() ) {

			Market market = new Market("No markets");
			ArrayList<Market> temp = new ArrayList<Market>(); 
			temp.add(market);
		
			adapterSpinner = new ArrayAdapter<Market>(this, android.R.layout.simple_spinner_dropdown_item, temp);
			marketSpinner.setAdapter(adapterSpinner);
		} else {
			adapterSpinner = new ArrayAdapter<Market>(this, android.R.layout.simple_spinner_dropdown_item, markets);
			marketSpinner.setAdapter(adapterSpinner);			
		}		
	}

	private void populatePrice() {

		String newPrice = "";
		Price price = new Select().from(Price.class).where("market = ? AND item = ?", selectedMarket.getId(), idItem).executeSingle();

		if ( price != null ) {
			newPrice = String.valueOf(price.getPrice());
		} 	

		textPrice.setText(newPrice);
	}

	private void storeTempPrice() {
		
		String tempPrice = textPrice.getText().toString().trim();
		if ( tempPrice != null && !tempPrice.isEmpty() ) {
			tempPrices.put(selectedMarket.getId(), Float.valueOf(tempPrice));	
		}
	}
	
	private void saveItem() {

		Item newItem = new Item(editName.getText().toString().trim());
		newItem.setNotes(editNotas.getText().toString().trim());
		
		if ( !item.isSimilar(newItem) ) {
			item.setName(newItem.getName());
			item.setNotes(newItem.getNotes());
			item.save();
		}

		savePrices();
		sendResponse();
		finish();
	}
	
	private void savePrices() {
		
		storeTempPrice();		
				
		for(Market market : markets) {
			Float tempPrice = tempPrices.get(market.getId());
			if ( null == tempPrice ) {
				continue;
			}
			Price newPrice = new Price(item, selectedMarket, tempPrice);
			Price storedPrice = new Select().from(Price.class).where("market = ? AND item = ?", market.getId(), item.getId()).executeSingle();
			
			if ( storedPrice == null ) {
				newPrice.save();
			} else if ( ((storedPrice.getPrice() - tempPrice) >= 0.01f) ||
					((storedPrice.getPrice() - tempPrice) <= -0.01f) ) {
				storedPrice.setPrice(tempPrice);
				storedPrice.save();
			}			
		}
	}

	private void sendResponse() {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
	}

	
	
}

package com.haya.tabulae.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
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

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.models.Item;
import com.haya.tabulae.models.Market;
import com.haya.tabulae.models.Price;
import com.haya.tabulae.utils.Utils;

public class ItemDetailActivity extends Activity {

	private long idItem;
	private Item item;
	private EditText editName;
	private EditText editNotas;
	private Spinner marketSpinner;
	private EditText textPrice;

	private ArrayList<Market> markets;
	private ArrayAdapter<Market> adapterSpinner;

	private Market selectedMarket;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		getActionBar().setTitle("Details");
		Utils.setBackground(this, android.R.color.holo_purple);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(R.drawable.ic_arrow_back);

		editName = (EditText) findViewById(R.id.editItemName);
		editNotas = (EditText) findViewById(R.id.editItemNotes);
		marketSpinner = (Spinner) findViewById(R.id.spinnerMarket);		
		textPrice = (EditText) findViewById(R.id.textPrice);

		marketSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				selectedMarket = (Market) marketSpinner.getSelectedItem();
				populatePrice();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.item_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_save) {
			saveItem();
		} else if ( id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	
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
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			newPrice = df.format(price.getPrice());
		} 	

		textPrice.setText(newPrice);
	}
	
	private void saveItem() {
		
		/* 
		 * TO DO
		 */
		finish();
	}

}

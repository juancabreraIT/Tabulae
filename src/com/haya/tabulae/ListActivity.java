package com.haya.tabulae;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ListActivity extends Activity {
	
	ListView list;
	Spinner marketSelector;
	TextView price;
	
	// Mock
	ArrayList<String> items = new ArrayList<String>();
	ArrayList<String> markets = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void init() {
		
		list = (ListView) findViewById(android.R.id.list);
		marketSelector = (Spinner) findViewById(R.id.marketSelector);
		price = (TextView) findViewById(R.id.price);
			
	}
	
	private void mock() {
		
		// LISTVIEW
		items.add("Tomate");
		items.add("Pan");
		items.add("Detergente");
		
		ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		list.setAdapter(adapterList);
		
		// SPINNER
		markets.add("Mercadona");
		markets.add("Aldi");
		markets.add("Super Sol");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, markets);				
		marketSelector.setAdapter(adapter);
		
		// TEXTVIEW
		price.setText("23.42€");
		
	}
}

package com.haya.tabulae;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ListActivity extends Activity {
	
	ListView list;
	Spinner marketSelector;
	TextView price;
	
	// Mock
	ArrayList<String> items = new ArrayList<String>();
	ArrayAdapter<String> adapterList;
	
	ArrayList<String> markets = new ArrayList<String>();
	ArrayAdapter<String> adapterSpinner;
	
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
		int id = item.getItemId();
		if (id == R.id.action_add) {
			addItemDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void addItemDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;
		builder.setTitle(getResources().getText(R.string.newItem));

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		input.requestFocus();
		builder.setView(input);

		// OK
		builder.setPositiveButton(getResources().getText(R.string.dialog_ok), new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	String itemName = input.getText().toString().trim();
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
	
	private void addItem(String item) {
		items.add(item);
		adapterList.notifyDataSetChanged();
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
		
		adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		list.setAdapter(adapterList);
		
		// SPINNER
		markets.add("Mercadona");
		markets.add("Aldi");
		markets.add("Super Sol");

		adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, markets);				
		marketSelector.setAdapter(adapterSpinner);
		
		// TEXTVIEW
		price.setText("23.42€");
		
	}
}

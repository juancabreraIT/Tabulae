package com.haya.tabulae.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.adapters.ItemAdapter;
import com.haya.tabulae.models.Item;

public class ListActivity extends Activity {
	
	ListView list;
	Spinner marketSelector;
	TextView price;
	
	// Mock
	ArrayList<Item> items;
	ItemAdapter adapterList;
	
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

	private void init() {
		
		list = (ListView) findViewById(android.R.id.list);
		marketSelector = (Spinner) findViewById(R.id.marketSelector);
		price = (TextView) findViewById(R.id.price);		
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				Toast.makeText(getApplicationContext(), items.get(position) + " picked!", Toast.LENGTH_SHORT).show();
			}
		});
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
		    		Item item = new Item(itemName);
		    		addItem(item);
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
	
	private void addItem(Item item) {
		items.add(item);
		item.save();
		adapterList.notifyDataSetChanged();
	}
	
	public void checkItem(View v) {
		
		CheckBox checkB = (CheckBox) v;
		checkB.setChecked(false);	
		int pos = (Integer) v.getTag();
		items.get(pos).delete();
		items.remove(pos);		
		adapterList.notifyDataSetChanged();
	}

	
	private void mock() {
		
		items = new Select().from(Item.class).execute();
		
		if ( items.isEmpty() ) {
			Log.d("Tabulae", "Loading data base 1st time.");
			// LISTVIEW
			Item temp = new Item("Tomate");			
			items.add(temp);
			temp.save();
			
			temp = new Item("Pan");
			items.add(temp);
			temp.save();

			temp = new Item("Detergente");
			items.add(temp);
			temp.save();
		} else {
			Log.d("Tabulae", "Data base was loaded.");
		}
		adapterList = new ItemAdapter(this, R.layout.list_item, R.id.ItemTitle, items);
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

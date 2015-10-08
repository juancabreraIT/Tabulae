package com.haya.tabulae.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.haya.tabulae.R;
import com.haya.tabulae.models.Market;
import com.haya.tabulae.utils.Utils;

public class NewMarketActivity extends Activity {

	EditText marketName;
	EditText marketNotes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_market);
		Utils.setActionBar(this, android.R.color.holo_purple, "New Market");
		Utils.setActionBarBack(this);
		
		marketName = (EditText) findViewById(R.id.editMarketName);
		marketNotes = (EditText) findViewById(R.id.editMarketNotes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_market, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
			case R.id.action_save:
				saveMarket();
				break;
			
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveMarket() {
		String name = marketName.getText().toString().trim();
		String notes = marketNotes.getText().toString().trim();
		
		if ( name.isEmpty() ) {
			Toast.makeText(this, "Market name is empty!", Toast.LENGTH_LONG).show();
			marketName.requestFocus();
			return;
		}
		
		Market market = new Market(name);
		
		if ( !notes.isEmpty() ) {
			market.setNotes(notes);
		}		
		
		market.save();
		
		sendResponse();
		finish();
	}
	
	private void sendResponse() {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
	}

}

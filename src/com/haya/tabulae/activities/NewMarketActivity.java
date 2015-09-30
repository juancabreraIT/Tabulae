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

public class NewMarketActivity extends Activity {

	EditText marketName;
	EditText marketNotes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_market);
		getActionBar().setTitle("New Market");
		getActionBar().setBackgroundDrawable(getDrawable(android.R.color.holo_purple));
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(R.drawable.ic_arrow_back);
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_market, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_save) {
			saveMarket();
		} else if ( id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void init() {
		
		marketName = (EditText) findViewById(R.id.editMarketName);
		marketNotes = (EditText) findViewById(R.id.editMarketNotes);
		
	}

	private void saveMarket() {
		String name = marketName.getText().toString();
		String notes = marketNotes.getText().toString();
		
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

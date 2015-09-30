package com.haya.tabulae;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ItemDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		getActionBar().setTitle("Details");
		setBackground(android.R.color.holo_purple);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(R.drawable.ic_arrow_back);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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

	private void saveItem() {
		
		finish();
	}
	
	@SuppressWarnings("deprecation")
	private void setBackground(int background) {
		
		Drawable draw;
        if(android.os.Build.VERSION.SDK_INT >= 21){
        	draw = this.getResources().getDrawable(background, this.getTheme());
        	this.getActionBar().setBackgroundDrawable(draw);
        } else {
        	draw = this.getResources().getDrawable(background);
        	this.getActionBar().setBackgroundDrawable(draw);
        }
	}

}

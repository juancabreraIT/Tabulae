package com.haya.tabulae.activities;

import com.haya.tabulae.R;
import com.haya.tabulae.adapters.DrawerItemsAdapter;
import com.haya.tabulae.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class DonateActivity extends Activity {

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerItemsAdapter adapterDrawer;
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donate);
		Utils.setActionBar(this, android.R.color.holo_purple);
		
		loadDrawer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_donate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public void onBackPressed() {

	    if ( mDrawerLayout.isDrawerOpen(Gravity.START) ) {
	    	mDrawerLayout.closeDrawer(Gravity.START);
	    } else {
	        super.onBackPressed();
	    }
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }
	
	@SuppressLint("NewApi")
	private void loadDrawer() {
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  			/* host Activity */
                mDrawerLayout,         			/* DrawerLayout object */
                R.drawable.ic_menu_white_24dp,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  			/* "open drawer" description */
                R.string.drawer_close  			/* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.donate);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.select_option);
                invalidateOptionsMenu();
            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);      
        adapterDrawer = new DrawerItemsAdapter(this, R.layout.drawer_item, R.id.drawerItemTitle, Utils.drawerList);
        mDrawerList.setAdapter(adapterDrawer);

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mDrawerLayout.closeDrawer(Gravity.START);
				switch(position) {
					case 0:
							Intent intent = new Intent(getApplicationContext(), MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);					
					break;
	
					case 1:
							Intent intent1 = new Intent(getApplicationContext(), ItemsActivity.class);
							intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent1);
					break;
					
					case 2:
						Intent intent3 = new Intent(getApplicationContext(), MarketsActivity.class);
						intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent3);
					break;					
					
					case 3:
						Intent intent2 = new Intent(getApplicationContext(), SettingsActivity.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent2);
					break;
				}				
			}        	
        });
	}	
	
}

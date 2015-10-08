package com.haya.tabulae.activities;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.haya.tabulae.R;
import com.haya.tabulae.adapters.DrawerItemsAdapter;
import com.haya.tabulae.adapters.MarketAdapter;
import com.haya.tabulae.models.Market;
import com.haya.tabulae.utils.Utils;

@SuppressWarnings("deprecation")
public class MarketsActivity extends ListActivity implements OnItemClickListener {

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerItemsAdapter adapterDrawer;
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    
    private ArrayList<Market> allMarkets;
	private MarketAdapter adapterList;
	
	@SuppressWarnings("unused")
	private ActionMode actionMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_markets);
		Utils.setActionBar(this, android.R.color.holo_purple);
		
		loadDrawer();
		populateList();
		
		getListView().setOnItemClickListener(this);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			private int numSelected = 0;

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}			
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				adapterList.clearSelection();
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				numSelected = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_contextual, menu);
                actionMode = mode;
                return true;
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {

                case R.id.item_delete:
                	deleteMarketDialog(numSelected, mode);
                    numSelected = 0;
                    break;

                case R.id.item_select_all:
                	checkAllItems();
                	numSelected = allMarkets.size();
                	adapterList.selectAll(numSelected);
                	mode.setTitle(numSelected + " selected");
                	break;
            	}                
            	return false;
			}
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				if ( checked ) {
                    numSelected++;                    
                } else {
                    numSelected--;
                }				
				adapterList.setNewSelection(position, checked);
                mode.setTitle(numSelected + " selected");
            	mode.invalidate();                
			}

		});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				getListView().setItemChecked(position, !adapterList.isPositionSelected(position));
				return false;
			}
		});	
		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_markets, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch(id) {
			case R.id.action_add:
				addMarket();
				break;
			case android.R.id.home:
				finish();
				break;
		}
		
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {			
			case (Utils.NEW_MARKET_RESULT) :
				if (resultCode == Activity.RESULT_OK) {
					populateList();
				}
		}
	}	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//		Intent intent = new Intent(this, MarketDetailActivity.class);
//		long idItem = allMarkets.get(position).getId();
//		intent.putExtra("idItem", idItem);
//		
//		startActivityForResult(intent, Utils.MARKET_DETAIL);
		
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
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_add).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

	private void checkAllItems() {
		
		for(int i = 0; i < getListView().getCount(); i++) {
			getListView().setItemChecked(i, true);
		}		
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
                getActionBar().setTitle(R.string.my_markets);
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
					
					case 3:
						Intent intent2 = new Intent(getApplicationContext(), SettingsActivity.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent2);
					break;
					
					case 4:
						Toast.makeText(getApplicationContext(), "In construction", Toast.LENGTH_LONG).show();
					break;
					
					case 5: 
						Intent intent5 = new Intent(getApplicationContext(), DonateActivity.class);
						intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent5);
					break;					
				}				
			}        	
        });
	}

	private void populateList() {
		
		allMarkets = new Select().from(Market.class).execute();		
		adapterList = new MarketAdapter(this, R.layout.item, R.id.ItemTitle, allMarkets);
		getListView().setAdapter(adapterList);
	}
	
	private void addMarket() {
		Intent intent = new Intent(getApplicationContext(), NewMarketActivity.class);
		startActivityForResult(intent, Utils.NEW_MARKET_RESULT);
	}

	@SuppressWarnings("unused")
	private void deleteMarketDialog(int numItems, final ActionMode mode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;
		String title = getText(R.string.deleteItem).toString() + " " + numItems + " ";
		title += numItems > 1 ? getResources().getText(R.string.markets) : getResources().getText(R.string.market); 
		builder.setTitle(title);

		// OK
		builder.setPositiveButton(getResources().getText(android.R.string.ok), new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	deleteMarket(mode);
		    }
		});
		
		// CANCEL
		builder.setNegativeButton(getResources().getText(android.R.string.cancel), new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		dialog = builder.create();
		dialog.show();		
	}
	
	private void deleteMarket(ActionMode mode) {
		
		Iterator<Integer> it = adapterList.getCurrentSelectedPosition().iterator();
        ArrayList<Market> deletedMarkets = new ArrayList<Market>();
        
        while ( it.hasNext() ) {
        	int index = it.next().intValue();
        	Market temp = allMarkets.get(index);
        	deletedMarkets.add(temp);
        	temp.delete();
        }
        
        allMarkets.removeAll(deletedMarkets);
        adapterList.clearSelection();
        mode.finish();
	}
	
}


package com.haya.tabulae.models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Price")
public class Price extends Model {

	@Column(name = "item", notNull = true)
	private Item item;
	
	@Column(name = "market", notNull = true)
	private Market market;
	
	@Column(name = "price", notNull = true)
	private float price;
	
	public Price() {
		super();
	}
	
	public Price(Item item, Market market, float price) {
		this.item = item;
		this.market = market;
		this.setPrice(price);
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		df.setMaximumFractionDigits(2);
		
		this.price = Float.valueOf(df.format(price));	
	}

	
	@Override
	public String toString() {
		return String.valueOf(price);
	}
	
	
}

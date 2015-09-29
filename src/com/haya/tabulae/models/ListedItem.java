package com.haya.tabulae.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ListedItem")
public class ListedItem extends Model {

	@Column(name = "list", notNull = true)
	private String list;
	
	@Column(name = "item", notNull = true)
	private Item item;
	
	@Column(name = "amount")
	private byte amount;
	
	public ListedItem() {
		super();
	}

	public ListedItem(String name, String itemName) {
		this.list = name;
		item = new Item(itemName);
	}
	
	public ListedItem(String name, Item item) {
		this.list = name;
		this.item = item;
	}
	
	public String getListName() {
		return list;
	}

	public void setListName(String name) {
		this.list = name;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public byte getAmount() {
		return amount;
	}

	public void setAmount(byte amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return getItem().getName();
	}
	
}

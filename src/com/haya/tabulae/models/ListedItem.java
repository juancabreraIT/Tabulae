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
	
	@Column(name = "amount", notNull = true)
	private byte amount;
	
	@Column(name = "checked", notNull = true)
	private boolean checked;
	
	public ListedItem() {
		super();
	}

	public ListedItem(String name, String itemName) {
		this.list = name;
		this.item = new Item(itemName);
		this.amount = 1;
		this.checked = false;
	}
	
	public ListedItem(String name, Item item) {
		this.list = name;
		this.item = item;
		this.amount = 1;
		this.checked = false;
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
	
	public void increaseAmount() {
		amount += 1;
	}
	
	public void decreaseAmount() {
		amount -= 1;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return getItem().getName();
	}
	
}

package com.haya.tabulae.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "List")
public class List extends Model {

	@Column(name = "name", notNull = true)
	private String name;
	
	@Column(name = "item", notNull = true)
	private Item item;
	
	@Column(name = "amount", notNull = true)
	private byte amount;
	
	public List() {
		super();
	}

	public List(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return getName();
	}
	
}

package com.haya.tabulae.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Item")
public class Item extends Model {
	
	@Column(name = "name", notNull = true)
	private String name;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "category")
	private Category category;
	
	public List<Price> prices() {
		return getMany(Price.class, "item");
	}
	
	public Item() {
		super();
	}
	
	public Item(String name) {
		this.name = name;
		this.notes = "";
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}

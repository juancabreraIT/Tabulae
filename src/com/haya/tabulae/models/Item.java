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
	
	@Column(name = "picked")
	private int picked;
	
	public List<Price> prices() {
		return getMany(Price.class, "item");
	}
	
	public Item() {
		super();
	}
	
	public Item(String name) {
		this.name = name;		
		this.notes = "";
		picked = 0;
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
		
	public int getPicked() {
		return picked;
	}

	public void setPicked(int picked) {
		this.picked = picked;
	}

	public void increasePicks() {
		picked += 1;
	}
	
	public boolean isSimilar(Item item) {
		return ( name.equals(item.getName()) && 
				notes.equals(item.getNotes()) );
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object object) {

		if ( object instanceof Item ) {
			Item newItem = (Item) object;
			return getId().equals(newItem.getId());
		}

		return false;
	}

}

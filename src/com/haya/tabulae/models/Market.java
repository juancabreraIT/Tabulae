package com.haya.tabulae.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Market")
public class Market extends Model {

	@Column(name = "name", notNull = true)
	private String name;
	
	@Column(name = "notes")
	private String notes;
	
	public List<Price> prices() {
		return getMany(Price.class, "item");
	}
	
	public Market() {
		super();
	}
	
	public Market(String name) {
		this.name = name;
		this.notes = "";
	}
	
	public Market(String name, String notes) {
		this.name = name;
		this.notes = notes;
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
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object object) {
		
		if ( object instanceof Market ) {
			Market newMarket = (Market) object;
			
			return getId().equals(newMarket.getId());
		}

		return false;
	}
}

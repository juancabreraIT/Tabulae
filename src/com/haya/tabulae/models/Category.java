package com.haya.tabulae.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Category")
public class Category extends Model {

	@Column(name = "name", notNull = true)
	private String name;
		
	public List<Item> items() {
		return getMany(Item.class, "category");
	}
	
	public Category() {
		super();
	}
	
	public Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}

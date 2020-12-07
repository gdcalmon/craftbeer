package com.beerhouse.request;

import java.math.BigDecimal;


public class BeerRequest {
	private String name;
	private String ingredients;
	private BigDecimal price;
	private String alcoholContent;
	private String category;
	
	public String getName() {
		return name;
	}
	public String getIngredients() {
		return ingredients;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public String getAlcoholContent() {
		return alcoholContent;
	}
	public String getCategory() {
		return category;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public void setAlcoholContent(String alcoholContent) {
		this.alcoholContent = alcoholContent;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}

package com.beerhouse.response;

import java.math.BigDecimal;

import com.beerhouse.entity.Beer;

public class BeerResponse {
	private Integer id;
	private String name;
	private String ingredients;
	private BigDecimal price;
	private String alcoholContent;
	private String category;
	
	public BeerResponse() {
	}
	
	public BeerResponse(Beer beer) {
		this.alcoholContent = beer.getAlcoholContent();
		this.category = beer.getCategory();
		this.id = beer.getId();
		this.ingredients = beer.getIngredients();
		this.name = beer.getName();
		this.price = beer.getPrice();
	}
	
	public final Integer getId() {
		return id;
	}
	public final String getName() {
		return name;
	}
	public final String getIngredients() {
		return ingredients;
	}
	public final BigDecimal getPrice() {
		return price;
	}
	public final String getAlcoholContent() {
		return alcoholContent;
	}
	public final String getCategory() {
		return category;
	}
	
	
}

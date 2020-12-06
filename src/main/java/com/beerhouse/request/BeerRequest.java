package com.beerhouse.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BeerRequest {
	@NotNull
	@Size(max=256)
	private String name;
	@NotNull
	@Size(max=65535)
	private String ingredients;
	@NotNull
	private BigDecimal price;
	@NotNull
	@Size(max=32)
	private String alcoholContent;
	@NotNull
	@Size(max=256)
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
}

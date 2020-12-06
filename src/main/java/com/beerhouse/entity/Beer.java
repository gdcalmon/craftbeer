package com.beerhouse.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.beerhouse.request.BeerRequest;
@Entity
@Table(name = "beer")
public class Beer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String name;
	@Column
	@Type(type="text")
	private String ingredients;
	@Column
	private BigDecimal price;
	@Column(name="alcohol_content")
	private String alcoholContent;
	@Column
	private String category;

	public Beer() {
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIngredients() {
		return ingredients;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getAlcoholContent() {
		return alcoholContent;
	}
	public void setAlcoholContent(String alcoholContent) {
		this.alcoholContent = alcoholContent;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}

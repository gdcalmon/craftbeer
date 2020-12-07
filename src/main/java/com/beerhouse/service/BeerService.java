package com.beerhouse.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.request.BeerRequest;

@Service
public class BeerService {
	@Autowired
	BeerRepository beerRepository;


	public Beer getById(Integer id){
		return beerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
	}
	
	public List<Beer> getAll(){
		return beerRepository.findAll();
	}
	
	public Beer create(BeerRequest request) {
		Beer beer = new Beer();
		setBeerFromRequest(beer, request);
		beerRepository.save(beer);
		return beer;
	}
	
	private void setBeerFromRequest(Beer beer, BeerRequest request) {
		beer.setAlcoholContent(request.getAlcoholContent());
		beer.setCategory(request.getCategory());
		beer.setIngredients(request.getIngredients());
		beer.setName(request.getName());
		beer.setPrice(request.getPrice());
	}

	public void deleteById(Integer id) {
		beerRepository.deleteById(id);
	}

	public void put(Integer id, BeerRequest request) {
		Beer beer = beerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		beer.setAlcoholContent(request.getAlcoholContent());
		beer.setCategory(request.getCategory());
		beer.setIngredients(request.getIngredients());
		beer.setName(request.getName());
		beer.setPrice(request.getPrice());
		beerRepository.save(beer);
	}

	public void patch(Integer id, BeerRequest request) {
		Beer beer = beerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		beer.setAlcoholContent(request.getAlcoholContent() == null ? beer.getAlcoholContent() : request.getAlcoholContent());
		beer.setCategory(request.getCategory() == null ? beer.getCategory() : request.getCategory());
		beer.setIngredients(request.getIngredients() == null ? beer.getIngredients() : request.getIngredients());
		beer.setName(request.getName() == null ? beer.getName() : request.getName());
		beer.setPrice(request.getPrice() == null ? beer.getPrice() : request.getPrice());
		beerRepository.save(beer);
	}
	
}

package com.beerhouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.request.BeerRequest;

@Service
public class BeerService {
	@Autowired
	BeerRepository beerRepository;


	public Beer getById(Integer id) {
		return beerRepository.findOne(id);
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
		beerRepository.delete(id);
	}
	
}

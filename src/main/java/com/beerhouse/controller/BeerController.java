package com.beerhouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.beerhouse.entity.Beer;
import com.beerhouse.request.BeerRequest;
import com.beerhouse.response.BeerResponse;
import com.beerhouse.service.BeerService;

@RestController
@RequestMapping("/beer")
public class BeerController {
	
	@Autowired
	BeerService beerService;
	
	@GetMapping
	public ResponseEntity<List<BeerResponse>> get(){
		List<Beer> beers = beerService.getAll();
		List<BeerResponse> response = beers.stream().map(beer -> {
			return new BeerResponse(beer);
		}).collect(Collectors.toList());
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BeerResponse> getById(@PathVariable Integer id){
		Beer beer = beerService.getById(id);
		return ResponseEntity.ok().body(new BeerResponse(beer));
	}
	
	@PostMapping
	public ResponseEntity<?> post(HttpServletRequest httpRequest, @RequestBody BeerRequest request){
		Beer beer = beerService.create(request);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromRequest(httpRequest)
				.path(String.format("/%s", beer.getId())).build().toUri()).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable Integer id, @RequestBody BeerRequest request){
		beerService.put(id, request);
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> patch(@PathVariable Integer id, @RequestBody BeerRequest request){
		beerService.patch(id, request);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		beerService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}

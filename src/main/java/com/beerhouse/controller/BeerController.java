package com.beerhouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.beerhouse.entity.Beer;
import com.beerhouse.request.BeerRequest;
import com.beerhouse.response.BeerResponse;
import com.beerhouse.service.BeerService;

import br.com.upcrm.controller.v1.entities.CustomerRequest;
import br.com.upcrm.dto.EnderecoDTO;

@RestController
@RequestMapping("/beer")
public class BeerController {
	
	@Autowired
	BeerService beerService;
	
	@GetMapping
	public String get() {
		return "Hello World!";
	}
	
	@GetMapping
	public ResponseEntity<List<BeerResponse>> getall(@PathVariable Integer id){
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
	public ResponseEntity<?> create(HttpServletRequest httpRequest, @Valid @RequestBody BeerRequest request){
		Beer beer = beerService.create(request);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromRequest(httpRequest)
				.path(String.format("/%s", beer.getId())).build().toUri()).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(HttpServletRequest httpRequest, @PathVariable Integer id){
		beerService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
}

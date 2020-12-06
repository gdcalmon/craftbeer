package com.beerhouse.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.Application;
import com.beerhouse.CraftbeerDatabases;
import com.beerhouse.controller.BeerController;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackageClasses = Application.class)
public class BeerServiceTest {
	
	@Autowired
	BeerController beerController;
	
	@Before
	public void startUp() {
		CraftbeerDatabases.INSTANCE.clean();
	}
	
	@Test
	public void itIs() throws Exception {
		assertNotNull(beerController);
	}
	
	@Test
	public void createBeer() throws Exception {
		JSONObject beerRequest = new JSONObject();
		beerRequest.put("name", "Calmon Beer");
		beerRequest.put("ingredients", "Grain, Hops, Yeast, Water");
		beerRequest.put("alcoholContent", "7,4%");
		beerRequest.put("price", new BigDecimal("7.95"));
		beerRequest.put("category", "Lager");
		
		
	}
}

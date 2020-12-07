package com.beerhouse.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.beerhouse.Application;
import com.beerhouse.request.BeerRequest;
import com.beerhouse.response.BeerResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackageClasses = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BeerServiceTest {
	String baseUrl;

	@Autowired
    private TestRestTemplate restTemplate;
    private RestTemplate patchRestTemplate;
	@LocalServerPort
    int localServerPort;
	
	@Before
	public void setUp() {
		this.baseUrl = String.format("http://localhost:%s/beer", localServerPort);
	}
	
	@Test
	public void allEmpty() throws Exception {
		ResponseEntity<List<BeerResponse>> response = this.restTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<BeerResponse>>(){});
		assertEquals(0, response.getBody().size());
	}
	
	@Test
	public void createBeerAndGetAll() throws Exception {
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
        this.restTemplate.postForEntity(baseUrl, request, String.class);
        ResponseEntity<List<BeerResponse>> response = this.restTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<BeerResponse>>(){});
		assertFalse(response.getBody().isEmpty());
	}
	
	@Test
	public void getBeerNotFound() throws Exception {
		String url = String.format("%s/100", baseUrl);
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createBeer() throws Exception {
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
        ResponseEntity<String> response = this.restTemplate.postForEntity(baseUrl, request, String.class);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createBeerThenGetByLocation() throws Exception {
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(baseUrl, request, String.class);
        ResponseEntity<BeerResponse> response = this.restTemplate.getForEntity(postResponse.getHeaders().getLocation(), BeerResponse.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody().getId());
        assertEquals("Calmon Beer", response.getBody().getName());
        assertEquals("Grain, Hops, Yeast, Water", response.getBody().getIngredients());
        assertEquals("7,4%", response.getBody().getAlcoholContent());
        assertEquals(new BigDecimal("7.95"), response.getBody().getPrice());
        assertEquals("Lager", response.getBody().getCategory());
	}
	
	@Test
	public void createThenPutComplete() throws Exception {
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest); 
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(baseUrl, request, String.class);
		BeerRequest beerPutRequest = getPutOrPatchRequest();
		HttpEntity<BeerRequest> putRequest = getRequest(beerPutRequest);
        ResponseEntity<String> putResponse = this.restTemplate.exchange(postResponse.getHeaders().getLocation(), HttpMethod.PUT, putRequest, String.class);
        ResponseEntity<BeerResponse> getResponse = this.restTemplate.getForEntity(postResponse.getHeaders().getLocation(), BeerResponse.class);
        assertEquals(HttpStatus.OK.value(), putResponse.getStatusCodeValue());
        assertNotNull(getResponse.getBody().getId());
        assertEquals("Beer Calmon", getResponse.getBody().getName());
        assertEquals("Water, Yeast, Hops, Grain", getResponse.getBody().getIngredients());
        assertEquals("4,7%", getResponse.getBody().getAlcoholContent());
        assertEquals(new BigDecimal("95.07"), getResponse.getBody().getPrice());
        assertEquals("IPA", getResponse.getBody().getCategory());
	}
	
	@Test
	public void putInvalid() throws Exception {
		String url = String.format("%s/100", baseUrl);
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createThenPutNull() throws Exception {
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(baseUrl, request, String.class);
		BeerRequest beerPutRequest = new BeerRequest();
		HttpEntity<BeerRequest> putRequest = getRequest(beerPutRequest);
        ResponseEntity<String> putResponse = this.restTemplate.exchange(postResponse.getHeaders().getLocation(), HttpMethod.PUT, putRequest, String.class);
        ResponseEntity<BeerResponse> getResponse = this.restTemplate.getForEntity(postResponse.getHeaders().getLocation(), BeerResponse.class);
        assertEquals(HttpStatus.OK.value(), putResponse.getStatusCodeValue());
        assertEquals(HttpStatus.OK.value(), getResponse.getStatusCodeValue());
        assertNull(getResponse.getBody().getName());
        assertNull(getResponse.getBody().getAlcoholContent());
        assertNull(getResponse.getBody().getCategory());
        assertNull(getResponse.getBody().getIngredients());
        assertNull(getResponse.getBody().getPrice());
	}
	
	@Test
	public void patchInvalid() throws Exception {
		this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
		String url = String.format("%s/100", baseUrl);
        BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
		ResponseEntity<String> response = this.patchRestTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createThenPatchComplete() throws Exception {
		this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(baseUrl, request, String.class);
		BeerRequest beerPatchRequest = getPutOrPatchRequest();
		HttpEntity<BeerRequest> patchRequest = getRequest(beerPatchRequest);
        ResponseEntity<String> patchResponse = this.patchRestTemplate.exchange(postResponse.getHeaders().getLocation(), HttpMethod.PATCH, patchRequest, String.class);
        ResponseEntity<BeerResponse> getResponse = this.restTemplate.getForEntity(postResponse.getHeaders().getLocation(), BeerResponse.class);
        assertEquals(HttpStatus.OK.value(), patchResponse.getStatusCodeValue());
        assertEquals(HttpStatus.OK.value(), getResponse.getStatusCodeValue());
        assertNotNull(getResponse.getBody().getId());
        assertEquals("Beer Calmon", getResponse.getBody().getName());
        assertEquals("Water, Yeast, Hops, Grain", getResponse.getBody().getIngredients());
        assertEquals("4,7%", getResponse.getBody().getAlcoholContent());
        assertEquals(new BigDecimal("95.07"), getResponse.getBody().getPrice());
        assertEquals("IPA", getResponse.getBody().getCategory());
	}
	
	@Test
	public void createThenPatchNull() throws Exception {
		this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(baseUrl, request, String.class);
		BeerRequest beerPatchRequest = new BeerRequest();
		HttpEntity<BeerRequest> patchRequest = getRequest(beerPatchRequest);
        ResponseEntity<String> patchResponse = this.patchRestTemplate.exchange(postResponse.getHeaders().getLocation(), HttpMethod.PATCH, patchRequest, String.class);
        ResponseEntity<BeerResponse> getResponse = this.restTemplate.getForEntity(postResponse.getHeaders().getLocation(), BeerResponse.class);
        assertEquals(HttpStatus.OK.value(), patchResponse.getStatusCodeValue());
        assertEquals(HttpStatus.OK.value(), getResponse.getStatusCodeValue());
        assertEquals("Calmon Beer", getResponse.getBody().getName());
        assertEquals("Grain, Hops, Yeast, Water", getResponse.getBody().getIngredients());
        assertEquals("7,4%", getResponse.getBody().getAlcoholContent());
        assertEquals(new BigDecimal("7.95"), getResponse.getBody().getPrice());
        assertEquals("Lager", getResponse.getBody().getCategory());
	}
	
	@Test
	public void deleteInvalid() throws Exception {
		String url = String.format("%s/100", baseUrl);
		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createBeerThenDeleteByLocation() throws Exception {
		BeerRequest beerRequest = getBeerCreatedRequest();
        HttpEntity<BeerRequest> request = getRequest(beerRequest);
        ResponseEntity<String> postResponse = this.restTemplate.postForEntity(baseUrl, request, String.class);
        ResponseEntity<String> deleteResponse = this.restTemplate.exchange(postResponse.getHeaders().getLocation(), HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.getStatusCodeValue());
	}

	private BeerRequest getBeerCreatedRequest() throws JSONException {
		BeerRequest beerRequest = new BeerRequest();
		beerRequest.setName("Calmon Beer");
		beerRequest.setIngredients("Grain, Hops, Yeast, Water");
		beerRequest.setAlcoholContent("7,4%");
		beerRequest.setPrice(new BigDecimal("7.95"));
		beerRequest.setCategory("Lager");
		return beerRequest;
	}

	private BeerRequest getPutOrPatchRequest() {
		BeerRequest beerPutRequest = new BeerRequest();
		beerPutRequest.setName("Beer Calmon");
		beerPutRequest.setIngredients("Water, Yeast, Hops, Grain");
		beerPutRequest.setCategory("IPA");
		beerPutRequest.setAlcoholContent("4,7%");
		beerPutRequest.setPrice(new BigDecimal("95.07"));
		return beerPutRequest;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private HttpEntity<BeerRequest> getRequest(BeerRequest beerRequest) {
		HttpHeaders headers = getHeaders();
		HttpEntity<BeerRequest> patchRequest = new HttpEntity<BeerRequest>(beerRequest, headers);
		return patchRequest;
	}
}

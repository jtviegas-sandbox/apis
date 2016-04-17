package org.aprestos.labs.ee.ws.bankaccounts.endpoints;

import java.math.BigDecimal;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static java.lang.String.format;

public class AccountIntegrationTest {
    
    private Client client;
    private WebTarget target;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
    }

    
    @Test
    public void createAccount() {
	
	 URI builder = UriBuilder.fromPath("bankaccounts/api/")
	            .scheme("http").host("localhost").port(9080).build();
	BigDecimal amount = new BigDecimal("12.03");
	this.target = this.client.target(builder);
	final JsonObject expected = Json.createObjectBuilder()
		.add("balance", amount.doubleValue())
		.build();
	
	Response response = this.target.request(MediaType.APPLICATION_JSON).put(Entity.entity(expected, MediaType.APPLICATION_JSON));
	JsonObject actual = response.readEntity(JsonObject.class);
	Assert.assertEquals(actual.getJsonNumber("balance").bigDecimalValue(), amount);
    }
    
    @Test
    public void createAccountBad() {
	
	 URI creationUri = UriBuilder.fromPath("bankaccounts/api/1")
	            .scheme("http").host("localhost").port(9080).build();
	    
	this.target = this.client.target(creationUri);
	final JsonObject expected = Json.createObjectBuilder()
		.add("balance", 12.03d)
		.build();
	
	Response response = this.target.request(MediaType.APPLICATION_JSON).put(Entity.entity(expected, MediaType.APPLICATION_JSON));
	Assert.assertFalse(response.getStatus() == Response.Status.OK.getStatusCode());
    }
    
    @Test
    public void createTransfer() {
	
	 URI transferUri = UriBuilder.fromPath("bankaccounts/api/transfer")
	            .scheme("http").host("localhost").port(9080).build();
	 URI creationUri = UriBuilder.fromPath("bankaccounts/api/")
	            .scheme("http").host("localhost").port(9080).build();
	 
	 // create accounts in server
	 this.target = this.client.target(creationUri);
	 JsonObject accountTo = Json.createObjectBuilder()
			.add("balance", new BigDecimal("12.03").doubleValue())
			.build();
	JsonObject accountFrom = Json.createObjectBuilder()
			.add("balance", new BigDecimal("99.73").doubleValue())
			.build();
	
	
	Response response = this.target.request(MediaType.APPLICATION_JSON).put(Entity.entity(accountTo, MediaType.APPLICATION_JSON));
	accountTo = response.readEntity(JsonObject.class);
	
	response = this.target.request(MediaType.APPLICATION_JSON).put(Entity.entity(accountFrom, MediaType.APPLICATION_JSON));
	accountFrom = response.readEntity(JsonObject.class);
	
	BigDecimal expectedTo = new BigDecimal("21.06");
	BigDecimal expectedFrom = new BigDecimal("90.70");
	BigDecimal transferAmount = new BigDecimal("9.03");

	// do the transfer
	this.target = this.client.target(transferUri);
	Form form = new Form();
	form.param("idFrom", Integer.toString(accountFrom.getInt("id")));
	form.param("idTo", Integer.toString(accountTo.getInt("id")));
	form.param("amount", transferAmount.toString());
	
	response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	Assert.assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
	
	// check the final balance of the source account
	URI getAccountFromUri = UriBuilder.fromPath(format("bankaccounts/api/%d", accountFrom.getInt("id")))
	            .scheme("http").host("localhost").port(9080).build();
	this.target = this.client.target(getAccountFromUri);
	accountFrom = this.target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
	Assert.assertEquals(accountFrom.getJsonNumber("balance").bigDecimalValue(), expectedFrom);
	
	URI getAccountToUri = UriBuilder.fromPath(format("bankaccounts/api/%d", accountTo.getInt("id")))
	            .scheme("http").host("localhost").port(9080).build();
	this.target = this.client.target(getAccountToUri);
	accountTo = this.target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
	Assert.assertEquals(accountTo.getJsonNumber("balance").bigDecimalValue(), expectedTo);
	
    }
    
}

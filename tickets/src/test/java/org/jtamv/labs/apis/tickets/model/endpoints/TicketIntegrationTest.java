package org.jtamv.labs.apis.tickets.model.endpoints;

import static java.lang.String.format;

import java.net.URI;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jtamv.labs.apis.tickets.model.Ticket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TicketIntegrationTest {
    
    private static final int PORT = 9080;
    
    private static URI getUri(String path){
	return UriBuilder.fromPath(format("tickets/api/%s", path))
        	.scheme("http").host("localhost").port(PORT).build();
    }
    
    private Client client;
    private WebTarget target;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
    }

    
    @Test
    public void testTicketGeneration() {
	 
	int numOfLines = 3;
	this.target = this.client.target(getUri("generate"));
	Form form = new Form();
	form.param("numlines", Integer.toString(numOfLines));
	
	Response response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	JsonObject actual = response.readEntity(JsonObject.class);
	Ticket ticket = Ticket.fromJson(actual);

	Assert.assertNotNull(ticket);
	Assert.assertEquals(numOfLines, ticket.getLines().size());
	
    }
    
    
    @Test
    public void testStatusCheck() {
	 
	int numOfLines = 3;
	this.target = this.client.target(getUri("generate"));
	
	Form form = new Form();
	form.param("numlines", Integer.toString(numOfLines));
	
	Response response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	JsonObject actual = response.readEntity(JsonObject.class);
	Ticket initialTicket = Ticket.fromJson(actual);
	
	this.target = this.client.target(getUri(format("status/%d", initialTicket.getId())));
	
	response = this.target.request(MediaType.APPLICATION_JSON).get();
	actual = response.readEntity(JsonObject.class);
	Ticket checkedTicket = Ticket.fromJson(actual);
	
	Assert.assertEquals(checkedTicket.checkStatus(), initialTicket.checkStatus());
    }
    
    
    @Test
    public void testAmendedStatusCheck() {
	
	int numOfLines = 3;
	this.target = this.client.target(getUri("generate"));
	
	Form form = new Form();
	form.param("numlines", Integer.toString(numOfLines));
	
	Response response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	JsonObject actual = response.readEntity(JsonObject.class);
	Ticket initialTicket = Ticket.fromJson(actual);
	
	this.target = this.client.target(getUri(format("amend/%d", initialTicket.getId())));
	response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	actual = response.readEntity(JsonObject.class);
	Ticket amendedTicket = Ticket.fromJson(actual);
	Assert.assertEquals(amendedTicket.getLines().size(), initialTicket.getLines().size() + numOfLines);
	
	this.target = this.client.target(getUri(format("status/%d", initialTicket.getId())));
	
	response = this.target.request(MediaType.APPLICATION_JSON).get();
	actual = response.readEntity(JsonObject.class);
	Ticket checkedTicket = Ticket.fromJson(actual);
	
	Assert.assertEquals(checkedTicket.checkStatus(), amendedTicket.checkStatus());
    }
    
    @Test
    public void testAmendedAfterStatusCheck() {
	
	int numOfLines = 3;
	this.target = this.client.target(getUri("generate"));
	
	Form form = new Form();
	form.param("numlines", Integer.toString(numOfLines));
	
	Response response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	JsonObject actual = response.readEntity(JsonObject.class);
	Ticket initialTicket = Ticket.fromJson(actual);
	
	this.target = this.client.target(getUri(format("status/%d", initialTicket.getId())));
	
	response = this.target.request(MediaType.APPLICATION_JSON).get();
	actual = response.readEntity(JsonObject.class);
	Ticket checkedTicket = Ticket.fromJson(actual);

	this.target = this.client.target(getUri(format("amend/%d", checkedTicket.getId())));
	response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	
	Assert.assertTrue(response.getStatus() == Response.Status.METHOD_NOT_ALLOWED.getStatusCode());
    }
    
    @Test
    public void testUnknownTicket() {
	
	int unknowndId = 90987;
	int numOfLines = 3;
	
	this.target = this.client.target(getUri(format("status/%d", unknowndId)));
	Response response = this.target.request(MediaType.APPLICATION_JSON).get();
	Assert.assertTrue(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
	
	Form form = new Form();
	form.param("numlines", Integer.toString(numOfLines));
	this.target = this.client.target(getUri(format("amend/%d", unknowndId)));
	response = this.target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
	Assert.assertTrue(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());

    }
    
}

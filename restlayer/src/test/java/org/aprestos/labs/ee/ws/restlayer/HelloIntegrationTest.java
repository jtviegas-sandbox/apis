package org.aprestos.labs.ee.ws.restlayer;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.aprestos.labs.ee.ws.serializer.BaseSerializer;
import org.aprestos.labs.ee.ws.serializer.Serializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HelloIntegrationTest {

    private static final String URL = "http://localhost:9080/restlayer/api/hello";
    
    private Client client;
    private WebTarget tut;
    private Serializer serializer;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.serializer = new BaseSerializer();
    }

    @Test
    public void getHelloBytes() throws IOException {
	this.tut = this.client.target(URL + "/get/23");
	final JsonObject expected = Json.createObjectBuilder()
		.add("hello", "Hello! you've just asked forid: 23")
		.build();
	JsonObject actual = this.serializer.deserialize(this.tut.request(MediaType.TEXT_PLAIN_TYPE).get(byte[].class));
	Assert.assertEquals(expected, actual);

    }
    
    @Test
    public void putHelloBytes() throws IOException {
	this.tut = this.client.target(URL + "/put");
	final JsonObject expected = Json.createObjectBuilder()
		.add("hello", "Hello! you've just asked forid: 23")
		.build();
	
	Response response = this.tut.request(MediaType.TEXT_PLAIN_TYPE).post(Entity.entity(this.serializer.serialize(expected), MediaType.TEXT_PLAIN));
	JsonObject actual = this.serializer.deserialize(response.readEntity(byte[].class));
	Assert.assertEquals(expected, actual);
    }
    
}

package org.aprestos.labs.ee.ws.restlayer;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EchoIntegrationTest {

    private static final int PORT = 9080;
    private static final String API_PATH = "restlayer/api";

    private Client client;
    private WebTarget tut;

    private static URI getUri(String path) {
	return UriBuilder.fromPath(format("%s/%s", API_PATH, path))
		.scheme("http").host("localhost").port(PORT).build();
    }

    @Before
    public void init() {
	this.client = ClientBuilder.newClient();
    }

    @Test
    public void getEcho() throws IOException {
	this.tut = this.client.target(getUri("echo"));

	final JsonObject expected = Json.createObjectBuilder()
		.add("msg", "Hello!").build();
	JsonObject actual = this.tut.request().post(
		Entity.entity(expected, MediaType.APPLICATION_JSON),
		JsonObject.class);
	Assert.assertEquals(expected, actual);
    }

}

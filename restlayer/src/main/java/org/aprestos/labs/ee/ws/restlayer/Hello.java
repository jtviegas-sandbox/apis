package org.aprestos.labs.ee.ws.restlayer;

import static java.lang.String.format;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.aprestos.labs.ee.ws.serializer.BaseSerializer;
import org.aprestos.labs.ee.ws.serializer.Serializer;

@Path("/hello")
public class Hello {

    private Serializer serializer = new BaseSerializer();

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/put")
    public byte[] postHello(byte[] in) {

	try {
	    JsonObject o = serializer.deserialize(in);
	    return serializer.serialize(o);

	} catch (IOException e) {
	    throw new WebApplicationException(
		    Response.Status.INTERNAL_SERVER_ERROR);
	}

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/get/{id}")
    public byte[] getHello(@PathParam("id") String id) {

	final JsonObject obj = Json.createObjectBuilder()
		.add("hello", format("Hello! you've just asked forid: %s", id))
		.build();
	try {
	    return serializer.serialize(obj);
	} catch (IOException e) {
	    throw new WebApplicationException(
		    Response.Status.INTERNAL_SERVER_ERROR);
	}

    }

}

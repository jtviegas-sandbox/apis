package org.aprestos.labs.ee.ws.restlayer;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class Echo {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/echo")
    public JsonObject echo(JsonObject input) {

	try {
	    return input;
	} catch (Exception e) {
	    throw new WebApplicationException(
		    Response.Status.INTERNAL_SERVER_ERROR);
	}

    }

}

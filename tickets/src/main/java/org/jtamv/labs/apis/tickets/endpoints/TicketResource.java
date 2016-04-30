package org.jtamv.labs.apis.tickets.endpoints;

import static java.lang.String.format;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jtamv.labs.apis.tickets.App;
import org.jtamv.labs.apis.tickets.TicketException;
import org.jtamv.labs.apis.tickets.model.Ticket;
import org.jtamv.labs.apis.tickets.persistence.StoreSingleton;


@Path("/")
public class TicketResource {

    private static final Logger logger = Logger.getLogger(TicketResource.class);
    private static final String MSG_UNKNOWN_TICKET = "!!! unknown ticket !!!";

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generate")
    public JsonObject generate(@FormParam(value = "numlines") String numlines) {

	logger.trace(format("<in> generate (%s)", numlines));
	JsonObject result = null;
	try {
        	Ticket ticket = Ticket.generateTicket(Integer.parseInt(numlines), 
        		Integer.parseInt(App.getConfig().getProperty("line.size")), 
        		Integer.parseInt(App.getConfig().getProperty("min.value")),
        		Integer.parseInt(App.getConfig().getProperty("max.value"))
        		);
        	
        	StoreSingleton.INSTANCE.get().set(ticket.getId(), ticket);
        	result = Ticket.toJson(ticket);
	}
	catch (Exception e) {
	    logger.error(e);
	    throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
	} 
	
	logger.trace("<out> generate");
	return result;
    }
    
    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject status(@PathParam("id") Integer id) {
	logger.trace(format("<in> status"));
	JsonObject result = null;
	
	if(logger.isDebugEnabled())
    		logger.debug(format("status received id: %d", id));
	
	try {
	    Ticket ticket = StoreSingleton.INSTANCE.get().get(id);
	    if(null == ticket)
		throw new WebApplicationException(MSG_UNKNOWN_TICKET, Response.Status.NOT_FOUND);
	    
	    //going to send the ticket with the outcomes calculated
	    ticket.checkStatus();
	    
	    result = Ticket.toJson(ticket);
	}
	catch (Exception e) {
	    logger.error(e);
	    if(e instanceof WebApplicationException)
		throw e;
	    else
		throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
	}   

	logger.trace(format("<out> status"));
	return result;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/amend/{id}")
    public JsonObject amend(@PathParam("id") Integer id, @FormParam(value = "numlines") String numlines) {
	logger.trace(format("<in> amend"));
	JsonObject result = null;

	if(logger.isDebugEnabled())
		logger.debug(format("amend received id %d and numlines %s", id, numlines));
	
	try {
	    
	    Ticket ticket = StoreSingleton.INSTANCE.get().get(id);
	    if(null == ticket)
		throw new WebApplicationException(MSG_UNKNOWN_TICKET, Response.Status.NOT_FOUND);
	    
	    try {
		ticket.amend(Integer.parseInt(numlines));
	    } catch (TicketException te) {
		throw new WebApplicationException(te.getMessage(), Response.Status.METHOD_NOT_ALLOWED);
	    }
	    result = Ticket.toJson(ticket);
	}
	catch (Exception e) {
	    logger.error(e);
	    if(e instanceof WebApplicationException)
		throw e;
	    else
		throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
	}
	
	logger.trace("<out> amend");
	return result;
    }
    
    


}

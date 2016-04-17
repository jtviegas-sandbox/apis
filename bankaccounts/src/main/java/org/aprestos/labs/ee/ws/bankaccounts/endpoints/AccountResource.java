package org.aprestos.labs.ee.ws.bankaccounts.endpoints;

import static java.lang.String.format;

import java.math.BigDecimal;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.aprestos.labs.ee.ws.bankaccounts.model.Account;
import org.aprestos.labs.ee.ws.bankaccounts.model.AccountException;
import org.aprestos.labs.ee.ws.bankaccounts.persistence.StoreSingleton;

@Path("/")
public class AccountResource {

    private static final Logger logger = Logger.getLogger(AccountResource.class);
    private static final long LOCK_TIMEOUT_MILLIS = 10000;
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transfer")
    public Response transfer(@FormParam(value = "idFrom") String from,@FormParam(value = "idTo") String to, @FormParam(value = "amount") String transferAmount) {

	logger.trace(format("<in> transfer"));

	try {
	    
	    int idFrom = Integer.parseInt(from);
	    int idTo = Integer.parseInt(to);
	    BigDecimal amount =  new BigDecimal(transferAmount);
	    
	    if (logger.isDebugEnabled())
		logger.debug(
			format("transfer received idFrom: %d   idTo: %d   amount: %f",
				idFrom, idTo, amount));

	    Account accountFrom = StoreSingleton.INSTANCE.get().get(idFrom);
	    Account accountTo = StoreSingleton.INSTANCE.get().get(idTo);
	    if (null == accountFrom || null == accountTo) {
		String message = format("could not find acount with id: %d",
			null == accountFrom ? idFrom : idTo);
		logger.warn(message);
		throw new AccountException(message);
	    }

	    if (logger.isDebugEnabled()) {
		logger.debug(format("get found accountFrom: %s",
			accountFrom.toString()));
		logger.debug(format("get found accountTo: %s",
			accountTo.toString()));
	    }
	    accountFrom.transfer(amount, accountTo);

	    boolean fromLock = false, toLock = false;
	    try {
		if ((fromLock = accountFrom.lock(LOCK_TIMEOUT_MILLIS))
			&& (toLock = accountTo.lock(LOCK_TIMEOUT_MILLIS))) {
		    // simplifying here, we are presuming everything will turn
		    // out ok while persisting
		    StoreSingleton.INSTANCE.get().set(idFrom, accountFrom);
		    StoreSingleton.INSTANCE.get().set(idTo, accountTo);
		}
	    } catch (InterruptedException e) {
		if (fromLock) {
		    accountFrom.unlock();
		    throw new AccountException(format(
			    "could not lock accountTo with id: %d", idTo));
		}
		if (toLock) {
		    accountTo.unlock();
		    throw new AccountException(format(
			    "could not lock accountFrom with id: %d", idFrom));
		}

	    }

	} catch (AccountException e) {
	    throw new WebApplicationException(
		    Response.Status.INTERNAL_SERVER_ERROR);
	}
	
	logger.trace("<out> transfer");
	return Response.ok().build();
    }
    
    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject put(JsonObject in) {
	
	logger.trace(format("<in> put"));
	JsonObject result = null;
	
        try {
           
            if(logger.isDebugEnabled())
        	logger.debug(format("put received: %s", in.toString()));
            
	    Account account = Account.fromJson(in, true);
	    
	    if(logger.isDebugEnabled())
        	logger.debug(format("put received an account: %s", account.toString()));
	   storeAccount(account);
	   result = Account.toJson(account);

	} catch (AccountException e) {
	    throw new WebApplicationException(
		    Response.Status.INTERNAL_SERVER_ERROR);
	}
        logger.trace(format("<out> put"));
	return result;
    }

    
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject get(@PathParam("id") Integer id) {
	logger.trace(format("<in> get"));
	JsonObject result = null;
	
	if(logger.isDebugEnabled())
    		logger.debug(format("get received id: %d", id));
	
	Account account = StoreSingleton.INSTANCE.get().get(id);
	if (null == account){
	    String message = format("could not find acount with id: %d", id);
	    logger.warn(message);
	    throw new WebApplicationException(
		    message,
		    Response.Status.NOT_FOUND); 
	}
	if(logger.isDebugEnabled())
		logger.debug(format("get found account: %s", account.toString()));
	
	try {
	    result = Account.toJson(account);
	} catch (AccountException e) {
	    logger.error(e);
	    throw new WebApplicationException(
		    Response.Status.INTERNAL_SERVER_ERROR);
	}    

	logger.trace(format("<out> get"));
	return result;
    }
    
    /**
     * store account
     * 
     * @param account
     * @throws AccountException
     */
    private void storeAccount(Account account) throws AccountException {
	
	logger.trace(format("<in> storeAccount"));
	boolean lockedAccount = false;
	if(logger.isDebugEnabled())
    		logger.debug(format("going to store account: %s", account.toString()));
	
	 try {
		if(lockedAccount = account.lock(LOCK_TIMEOUT_MILLIS)){
		    
		    if(logger.isDebugEnabled())
			logger.debug(format("locked account %s", account.toString()));
		    StoreSingleton.INSTANCE.get().set(account.getId(), account);
		    if(logger.isDebugEnabled())
			logger.debug(format("stored account %s", account.toString()));
		    
		}
		else
		    throw new AccountException(format("timeout when trying to lock account %s", account.toString())); 
		
	    } catch (InterruptedException e) {
		throw new AccountException(format("could not lock account %s", account.toString()), e);
	    }
	    finally {
		if(lockedAccount){
		    account.unlock();
		    if(logger.isDebugEnabled())
			logger.debug(format("unlocked account %s", account.toString()));
		}
	    }
	 
	logger.trace(format("<out> storeAccount"));
    }

}

package org.jtamv.labs.apis.tickets.persistence;

import org.jtamv.labs.apis.tickets.model.Ticket;

public enum StoreSingleton {

	    INSTANCE;
	    
    		
	    private Store<Ticket, Integer> store = Store.getInstance();
	    
	    public Store<Ticket, Integer> get(){
		if(null == store)
		    store = Store.getInstance();
		    
		return store;
	    }

    
}

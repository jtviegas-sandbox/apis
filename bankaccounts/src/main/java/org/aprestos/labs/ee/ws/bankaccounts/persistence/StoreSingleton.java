package org.aprestos.labs.ee.ws.bankaccounts.persistence;

import org.aprestos.labs.ee.ws.bankaccounts.model.Account;

public enum StoreSingleton {

	    INSTANCE;
	    
    		
	    private Store<Account, Integer> store = Store.getInstance();
	    
	    public Store<Account, Integer> get(){
		if(null == store)
		    store = Store.getInstance();
		    
		return store;
	    }

    
}

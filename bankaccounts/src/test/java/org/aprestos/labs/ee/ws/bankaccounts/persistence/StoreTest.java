package org.aprestos.labs.ee.ws.bankaccounts.persistence;

import org.aprestos.labs.ee.ws.bankaccounts.model.Account;
import org.aprestos.labs.ee.ws.bankaccounts.model.AccountException;
import org.junit.Assert;
import org.junit.Test;

public class StoreTest {


    /**
     * test store persistence
     * 
     * @throws AccountException
     */
    @Test
    public void testJson() throws AccountException {

	Store<Account, Integer> store = Store.getInstance();
	
	String val = "{\"balance\":12.11,\"id\":1}";
	Account o = Account.fromJsonString(val, false);
	store.set(o.getId(), o);

	Assert.assertTrue(o.equals(store.get(o.getId())));
    }



}

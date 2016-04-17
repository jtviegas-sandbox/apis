package org.aprestos.labs.ee.ws.bankaccounts.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class AccountTest {

    /**
     * test creation of account and simple subtract operation
     * 
     * @throws AccountException
     */
    @Test
    public void testSimpleSubtract() throws AccountException {

	BigDecimal opVal = new BigDecimal("0.06"),
		expected = new BigDecimal("12.05");

	Account o = new Account("12.11");
	o.debit(opVal);

	Assert.assertEquals(o.getBalance().compareTo(expected), 0);
    }

    /**
     * test creation of account and simple add operation
     * 
     * @throws AccountException
     */
    @Test
    public void testSimpleAdd() throws AccountException {

	BigDecimal opVal = new BigDecimal("0.06"),
		expected = new BigDecimal("12.17");

	Account o = new Account("12.11");
	o.credit(opVal);

	Assert.assertEquals(o.getBalance().compareTo(expected), 0);
    }

    /**
     * test creation of account and simple add operation from json to json
     * 
     * @throws AccountException
     */
    @Test
    public void testJson() throws AccountException {

	BigDecimal opVal = new BigDecimal("0.06");
	String val = "{\"balance\":12.11,\"id\":1}";
	String expected = "{\"balance\":12.17,\"id\":1}";

	Account o = Account.fromJsonString(val, false);
	o.credit(opVal);

	Assert.assertTrue(Account.toJsonString(o).equals(expected));
    }

    /**
     * test creation of account with json and on class
     * 
     * @throws AccountException
     */
    @Test
    public void testAccountCreation() throws AccountException {

	String val = "{\"balance\":12.1104,\"id\":1}";
	Account one = new Account("12.1104");

	Account two = Account.fromJsonString(val, false);

	Assert.assertTrue(one.getBalance().compareTo(two.getBalance()) == 0);
    }

    /**
     * test wrong amount format
     * 
     * @throws AccountException
     */
    @Test(expected = AccountException.class)
    public void testAccountCreationWithAmountInWrongString()
	    throws AccountException {
	Account one = new Account("12,1104");
	Assert.assertNotNull(one);
    }

    /**
     * test sound funds transfer
     * 
     * @throws AccountException
     */
    @Test
    public void testSoundTransfer() throws AccountException {

	String val = "{\"balance\":12.1104,\"id\":1}";
	Account one = new Account("12.1104");
	Account two = Account.fromJsonString(val, false);

	BigDecimal subtractValue = new BigDecimal("3.11");
	BigDecimal expected = one.getBalance().subtract(subtractValue);

	two.transfer(subtractValue, one);

	Assert.assertTrue(two.getBalance().compareTo(expected) == 0);
    }

    /**
     * test not sufficient funds
     * 
     * @throws AccountException
     */
    @Test(expected = UnsufficientBalanceException.class)
    public void testUnsufficentBalanceTransfer() throws AccountException {

	String val = "{\"balance\":12.1104,\"id\":1}";
	Account one = new Account("12.1104");
	Account two = Account.fromJsonString(val, false);

	BigDecimal subtractValue = new BigDecimal("23.11");
	BigDecimal expected = one.getBalance().subtract(subtractValue);

	two.transfer(subtractValue, one);

	Assert.assertTrue(two.getBalance().compareTo(expected) == 0);
    }

    /**
     * test big load of transfers
     *     
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testMadness() throws InterruptedException, ExecutionException {

	int numOfTransfers = 1000;
	final Account one = new Account(new BigDecimal(17));
	final Account two = new Account(new BigDecimal(7));
	final Random random = new Random();
	final AtomicInteger locksFound = new AtomicInteger(0), 
		unsufficentBalancesFound = new AtomicInteger(0),
			transfers = new AtomicInteger(0);
	ExecutorService pool = Executors.newCachedThreadPool();
	List<Future<Boolean>> outcomes =  new ArrayList<Future<Boolean>>();
	boolean result = true;
	
	for (int i = 0; i < numOfTransfers; i++) {
	    outcomes.add(pool.submit(new Callable<Boolean>() {
		@Override
		public Boolean call() throws Exception {
		    boolean result = false;
		    try {
			if (random.nextBoolean())
			    one.transfer(new BigDecimal(random.nextDouble()),
				    two);
			else
			    two.transfer(new BigDecimal(random.nextDouble()),
				    one);
			
			transfers.incrementAndGet();
			
		    } catch (AccountLockedException e) {
			locksFound.incrementAndGet();
		    } catch (UnsufficientBalanceException e) {
			unsufficentBalancesFound.incrementAndGet();
		    }
		    result = (one.getBalance().compareTo(BigDecimal.ZERO) > 0)
			    && (two.getBalance()
				    .compareTo(BigDecimal.ZERO) > 0);
		    return result;
		}
	    }));
	}
	
	
	for(Future<Boolean> outcome:outcomes){
	    result &= outcome.get();
	}
	System.out.println(String.format("transfers: %d   AccountLockedException's: %d   UnsufficientBalanceException's: %d", transfers.get(), locksFound.get(), unsufficentBalancesFound.get()));
	System.out.println(String.format("final balances - one: %s   two: %s", one.getBalance().toString(), two.getBalance().toString()));
	Assert.assertTrue(result);
    }

}

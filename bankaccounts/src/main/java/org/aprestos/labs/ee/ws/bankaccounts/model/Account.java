package org.aprestos.labs.ee.ws.bankaccounts.model;

import static java.lang.String.format;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

public class Account {
    private static final String toStringPattern = "Account[ id: %d	balance: %f ]";
    private static final int SCALE = 2;
    private static final long lockTimeoutInMillis = 1000;
    private static final AtomicInteger idGenerator = new AtomicInteger();
    private int id;
    private BigDecimal balance;
    private final Lock lock = new ReentrantLock();
    
    public boolean lock(long timeoutMillis) throws InterruptedException{
	return lock.tryLock(timeoutMillis, TimeUnit.MILLISECONDS);
    }
    
    public void unlock() {
	lock.unlock();
    }
    
    
    public Account() {
	this.id = idGenerator.incrementAndGet();
	this.balance = new BigDecimal("0");
	this.balance.setScale(SCALE, RoundingMode.HALF_EVEN);
    }
    
    public Account(BigDecimal amount) {
	this.id = idGenerator.incrementAndGet();
	this.balance = new BigDecimal(amount.unscaledValue(), amount.scale());
	this.balance.setScale(SCALE, RoundingMode.HALF_EVEN);
    }
    
    public Account(String amount) throws AccountException {
	this.id = idGenerator.incrementAndGet();
	try {
	    this.balance = new BigDecimal(amount);
	    this.balance.setScale(SCALE, RoundingMode.HALF_EVEN);
	} catch (Exception e) {
	    throw new AccountException(e);
	}
	this.balance.setScale(SCALE, RoundingMode.HALF_EVEN);
    }
    
    private Account(int id, BigDecimal balance) {
	this.id = id;
	this.balance = balance;
	this.balance.setScale(SCALE, RoundingMode.HALF_EVEN);
    }
    
    public static final Account fromJsonString(String json, boolean generateId) throws AccountException {
	Account result = null;
	try {
	    JsonReader reader = Json.createReader(new StringReader(json));
	    JsonObject jObj = reader.readObject();
	    result = Account.fromJson(jObj, generateId);
	} 
	catch (AccountException x){
	    throw x;
	}
	catch (Exception e) {
	    throw new AccountException(e);
	}
	return result;
    }
    
    public static final Account fromJson(JsonObject json, boolean generateId) throws AccountException {
	Account result = null;
	try {
	    BigDecimal o = json.getJsonNumber("balance").bigDecimalValue();
	    
	    if(generateId){
		result = new Account(o);
	    }
	    else {
		int id = json.getInt("id");
		result = new Account(id, o);
	    }
	    
	} catch (Exception e) {
	    throw new AccountException(e);
	}
	return result;
    }
    
    public static final String toJsonString(Account account) throws AccountException {
	String result = null;
	try {
	    JsonObject personObject = Account.toJson(account);
	    StringWriter stringWriter = new StringWriter();
	    JsonWriter writer = Json.createWriter(stringWriter);
	    writer.writeObject(personObject);
	    writer.close();
	    result = stringWriter.getBuffer().toString();
	}
	catch (AccountException x){
	    throw x;
	}
	catch (Exception e) {
	    throw new AccountException(e);
	}
	return result;
    }
    
    public static final JsonObject toJson(Account account) throws AccountException {
	JsonObject result = null;
	try {
	    result = Json.createObjectBuilder()
		    .add("balance", account.balance)
		    .add("id", account.id)
		    .build();
	} catch (Exception e) {
	    throw new AccountException(e);
	}
	return result;
    }
    
    public int getId(){
	return this.id;
    }
    
    public BigDecimal getBalance(){
	return this.balance.round(MathContext.UNLIMITED);
    }
    
    public boolean transfer(BigDecimal amount, Account toAccount) throws AccountException {
	boolean result = false;
	boolean gotLocks = false;
	try {
	    if(gotLocks = synchLocks(toAccount)){
	        this.remove(amount);
	        toAccount.add(amount);
	        result = true;
	    }
	    else
	        throw new AccountLockedException();
	} catch (UnsufficientBalanceException e) {
	    throw e;
	}
	finally {
	    if(gotLocks){
		 this.lock.unlock();
		 toAccount.lock.unlock();
	    }
	}
	return result;
    }
    
    public boolean credit(BigDecimal amount) throws AccountException {
	boolean result = false;
	boolean lockHeld = false;
	try { lockHeld = this.lock.tryLock(lockTimeoutInMillis, TimeUnit.MILLISECONDS);} catch (InterruptedException e) {}
	if(lockHeld){
	    this.balance = this.balance.add(amount);
	    this.lock.unlock();
	    result = true;
	}
	else
	    throw new AccountLockedException();
	
	return result;
    }
    
    public boolean debit(BigDecimal amount) throws AccountException {
	boolean result = false;
	boolean lockHeld = false;
	try { lockHeld = this.lock.tryLock(lockTimeoutInMillis, TimeUnit.MILLISECONDS);} catch (InterruptedException e) {}
	

	    if(lockHeld){
	        this.remove(amount);
	        this.lock.unlock();
	        result = true;
	    }
	    else
	        throw new AccountLockedException();
	    
	    return result;
    }
    
    public String toString(){
	return format(toStringPattern, id, balance.floatValue());
    }
    
    private boolean synchLocks(Account other){
	boolean myLock = false, otherLock = false;
	try{
	    myLock = this.lock.tryLock(lockTimeoutInMillis, TimeUnit.MILLISECONDS);
	    otherLock = other.lock.tryLock(lockTimeoutInMillis, TimeUnit.MILLISECONDS);
	} catch (InterruptedException e) {}
	finally {
	    if( !(myLock && otherLock) ){
		// one lock has failed
		if(myLock)
		    this.lock.unlock();
		if(otherLock)
		    other.lock.unlock();
	    }
	}
	return myLock && otherLock;
    }
    
    private void add(BigDecimal amount){
	this.balance = this.balance.add(amount);
    }
    
    private void remove(BigDecimal amount) throws UnsufficientBalanceException{
	BigDecimal v = this.balance.subtract(amount);
	if( v.compareTo(BigDecimal.ZERO) < 0 ){
	    throw new UnsufficientBalanceException();
	}
	this.balance = v;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((balance == null) ? 0 : balance.hashCode());
	result = prime * result + id;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Account other = (Account) obj;
	if (balance == null) {
	    if (other.balance != null)
		return false;
	} else if (!balance.equals(other.balance))
	    return false;
	if (id != other.id)
	    return false;
	return true;
    }
    
    

}

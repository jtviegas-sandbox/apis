package org.jtamv.labs.apis.tickets.persistence;

public abstract class Store<T, ID> {
    
    
    public static final <T, ID> Store<T, ID> getInstance(){
	// we can read some config here and decide which impl to use
	// not having to change client code
	return new InMemoryStoreImpl<T, ID>();
    }
    
    public abstract T get(ID id);
    public abstract void set(ID id,T o);

}

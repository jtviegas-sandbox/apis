package org.jtamv.labs.apis.tickets.persistence;

import java.util.HashMap;
import java.util.Map;

class InMemoryStoreImpl<T, ID> extends Store<T, ID> {
    
    private final Map<ID,T> map;
    
    public InMemoryStoreImpl() {
	map = new HashMap<ID,T>();
    }

    @Override
    public T get(ID id) {
	T result = null;
	synchronized(map){
	    result = map.get(id);
	}
	return result;
    }

    @Override
    public void set(ID id, T o) {
	synchronized(map){
	    map.put(id, o);
	}
    }

}

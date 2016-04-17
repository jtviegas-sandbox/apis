package org.aprestos.labs.ee.ws.bankaccounts;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.aprestos.labs.ee.ws.bankaccounts.endpoints.AccountResource;

@ApplicationPath("/api")
public class App extends Application {
	
	 @Override
     public Set<Class<?>> getClasses()
     {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(AccountResource.class);
        return classes;
     }
	 
	
}

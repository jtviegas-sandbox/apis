package org.jtamv.labs.apis.tickets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jtamv.labs.apis.tickets.endpoints.TicketResource;

@ApplicationPath("/api")
public class App extends Application {

    private static Properties configuration;
    
    
    public static Properties getConfig() throws IOException{
	if(null == configuration){
	    configuration = new Properties();
	    configuration.load(App.class.getResourceAsStream(App.class.getSimpleName() + ".properties"));
	}
	return configuration;
    }
    
	 @Override
     public Set<Class<?>> getClasses()
     {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(TicketResource.class);
        return classes;
     }
	 
	
}

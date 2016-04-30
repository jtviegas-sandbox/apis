package org.jtamv.labs.apis.tickets.model.model;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.jtamv.labs.apis.tickets.App;
import org.jtamv.labs.apis.tickets.TicketException;
import org.jtamv.labs.apis.tickets.model.Line;
import org.jtamv.labs.apis.tickets.model.Ticket;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TicketTest {
    
    private static Properties config;
    
    @BeforeClass
    public static void setUp() throws IOException{
	config =  App.getConfig();
    }
   
    /**
     * test lines outcome
     */
    @Test
    public void testLinesOutcome() {

	Ticket ticket = Ticket.generateTicket(6, Integer.parseInt(config.getProperty("line.size")), 
		Integer.parseInt(config.getProperty("min.value")),
		Integer.parseInt(config.getProperty("max.value")));
	//get lines before checking, unsorted
	List<Line> lines = ticket.getLines();
	//get lines after checking, sorted
	List<Line> outcomes = ticket.checkStatus();

	//the items have already an outcome there so we can now sort them
	//as the list is different but the elements are exactly the same
	Collections.sort(lines, new Comparator<Line>(){
	    @Override
	    public int compare(Line o1, Line o2) {
		// TODO Auto-generated method stub
		return Integer.compare(o1.getOutcome(), o2.getOutcome());
	    }
	});
	
	//ArrayList equals tests also for index equality so the sorting should be equal as well
	Assert.assertEquals(lines, outcomes);


    }
    
    @Test
    public void testAmendedLinesOutcome() throws TicketException {

	Ticket ticket = Ticket.generateTicket(6, Integer.parseInt(config.getProperty("line.size")), 
		Integer.parseInt(config.getProperty("min.value")),
		Integer.parseInt(config.getProperty("max.value")));
	ticket.amend(3);
	
	//get lines before checking, unsorted
	List<Line> lines = ticket.getLines();
	//get lines after checking, sorted
	List<Line> outcomes = ticket.checkStatus();

	//the items have already an outcome there so we can now sort them
	//as the list is different but the elements are exactly the same
	Collections.sort(lines, new Comparator<Line>(){
	    @Override
	    public int compare(Line o1, Line o2) {
		// TODO Auto-generated method stub
		return Integer.compare(o1.getOutcome(), o2.getOutcome());
	    }
	});
	
	//ArrayList equals tests also for index equality so the sorting should be equal as well
	Assert.assertEquals(lines, outcomes);


    }

    @Test(expected = TicketException.class)
    public void testAmendLinesAfterStatus() throws TicketException {

	Ticket ticket = Ticket.generateTicket(6, Integer.parseInt(config.getProperty("line.size")), 
		Integer.parseInt(config.getProperty("min.value")),
		Integer.parseInt(config.getProperty("max.value")));
	
	ticket.checkStatus();
	ticket.amend(3);

    }
    
    

}

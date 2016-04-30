package org.jtamv.labs.apis.tickets.model;

public abstract class CalculatorVisitor {

    public enum CalculationType {
	lineOf3, //......
    }
    
    /**
     * 	factory method
     * 
     * @param type
     * @return
     */
    public static CalculatorVisitor getInstance(CalculationType type) {
	CalculatorVisitor result = null;
	switch(type){
            	case lineOf3:
            	    result = new  LineOf3CalculatorVisitorImpl();
            	    break;
            	default:
            	    result = new  LineOf3CalculatorVisitorImpl();
	}
	return result;
    }
    
    public abstract int calculate(Line line);

}

package org.jtamv.labs.apis.tickets.model.model;

import org.jtamv.labs.apis.tickets.model.CalculatorVisitor;
import org.jtamv.labs.apis.tickets.model.CalculatorVisitor.CalculationType;
import org.jtamv.labs.apis.tickets.model.Line;
import org.junit.Assert;
import org.junit.Test;

public class LineTest {

    private static final CalculatorVisitor visitor = CalculatorVisitor.getInstance(CalculationType.lineOf3);
    
    
    /**
     * test first calculation rule
     */
    @Test
    public void testFirstCalculationRule() {
	
	int[] numbers = new int[] {0, 2, 0};
	Line line = new Line(numbers);
	int expected = 10;
	
	Assert.assertEquals(line.calculateOutcome(visitor), expected);
    }

    /**
     * test second calculation rule
     */
    @Test
    public void testSecondCalculationRule() {
	
	int[] numbers = new int[] {1, 1, 1};
	Line line = new Line(numbers);
	int expected = 5;
	
	Assert.assertEquals(line.calculateOutcome(visitor), expected);
    }
   
    /**
     * test third calculation rule
     */
    @Test
    public void testThirdCalculationRule() {
	
	int[] numbers = new int[] {2, 1, 1};
	Line line = new Line(numbers);
	int expected = 1;
	
	Assert.assertEquals(line.calculateOutcome(visitor), expected);
    }
    
    /**
     * test default calculation rule
     */
    @Test
    public void testDefaultCalculationRule() {
	
	int[] numbers = new int[] {2, 2, 1};
	Line line = new Line(numbers);
	int expected = 0;
	
	Assert.assertEquals(line.calculateOutcome(visitor), expected);
    }
    
    

}

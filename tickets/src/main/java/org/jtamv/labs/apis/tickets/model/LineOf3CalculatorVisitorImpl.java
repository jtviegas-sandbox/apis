package org.jtamv.labs.apis.tickets.model;

class LineOf3CalculatorVisitorImpl extends CalculatorVisitor {

    @Override
    public int calculate(Line line){
	int result = 0;
	
	int[] numbers = line.getNumbers();
	
	int sum = 0;
	boolean same = true;
	boolean different = true;
	int first = -1;
	
	for( int i = 0; i < numbers.length; i++ ){
	    sum += numbers[i];
	    if( i > 0 ){
		same &= (numbers[i-1] == numbers[i]);
		different &= ( numbers[i] != first );
	    }
	    else
		first = numbers[i];
	}
	
	if(2 == sum)
	    result = 10;
	else if (same)
	    result = 5;
	else if (different)
	    result = 1;
	
	return result;
    }

}

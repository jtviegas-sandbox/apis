package org.jtamv.labs.apis.tickets.model;

import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class Line {

    private final int[] numbers;
    private int outcome = -1;

    public Line(int n){
	this.numbers = new int[n];
    }
    
    public Line(int[] numbers){
	this.numbers = new int[numbers.length];
	for(int i = 0 ; i < numbers.length; i++)
	    this.numbers[i] = numbers[i];
    }
    
    public int[] getNumbers(){
	int[] result = new int[this.numbers.length];
	System.arraycopy(this.numbers, 0, result, 0, this.numbers.length);
	return result;
    }

    public int calculateOutcome(CalculatorVisitor visitor) {
	this.outcome = visitor.calculate(this);
	return this.outcome;
    }

    public int getOutcome() {
	return this.outcome;
    }

    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(numbers);
	result = prime * result + outcome;
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Line other = (Line) obj;
	if (!Arrays.equals(numbers, other.numbers))
	    return false;
	if (outcome != other.outcome)
	    return false;
	return true;
    }

    
    public static Line fromJson(JsonObject json) {
	Line result = null;

	JsonArray nums	 = json.getJsonArray("numbers");
	int[] numbers = new int[nums.size()];
	for(int i = 0; i < nums.size(); i++)
	    numbers[i]= nums.getInt(i);
	
	result = new Line(numbers);
	result.outcome = json.getInt("outcome");

	return result;
    }
    
    public static JsonObject toJson(Line line) {
	return toJsonBuilder(line).build();
    }
    
    public static JsonObjectBuilder toJsonBuilder(Line line) {

	JsonArrayBuilder numberArrayBuilder = Json.createArrayBuilder();
	for (int number : line.numbers)
	    numberArrayBuilder.add(number);

	return Json.createObjectBuilder()
		.add("numbers", numberArrayBuilder)
		.add("outcome", line.outcome);
    }
    
}

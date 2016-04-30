package org.jtamv.labs.apis.tickets.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.jtamv.labs.apis.tickets.TicketException;
import org.jtamv.labs.apis.tickets.model.CalculatorVisitor.CalculationType;

public class Ticket {
    
    private static final Random random = new Random();
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private static final CalculatorVisitor visitor = CalculatorVisitor.getInstance(CalculationType.lineOf3);
    
    private int id;
    private final int lineSize;
    private final int minValue;
    private final int maxValue;
    private final List<Line> lines ;
    private Boolean checked = new Boolean(false);
    
    public static Ticket generateTicket(int numOfLines, int lineSize, int minValue, int maxValue){
	return new Ticket(numOfLines, lineSize, minValue, maxValue);
    }
    
    private Ticket(int numOfLines, int lineSize, int minValue, int maxValue){
	this.lineSize = lineSize;
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.lines = generateLines(numOfLines, lineSize, minValue, maxValue);
	this.id = idGenerator.incrementAndGet();
    }
    
    Ticket(List<Line> lines, int lineSize, int minValue, int maxValue, int id, boolean checked){
	this.lineSize = lineSize;
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.lines = lines;
	this.id = id;
	this.checked = checked;
    }
    
    
    
    public int getId(){
	return this.id;
    }
    
    public void amend(int additionalLines) throws TicketException{
	synchronized(checked){
	    if(checked)
		    throw new TicketException("ticket has already been checked, can't amend it no more!");
	    else
		this.lines.addAll(generateLines(additionalLines, this.lineSize, this.minValue, this.maxValue));
	}
    }

    public List<Line> checkStatus(){
	
	synchronized(checked){
	    if(!checked){
		Collections.sort(lines, 
		new Comparator<Line>(){
		    
		    @Override
		    public int compare(Line o1, Line o2) {
			return Integer.compare(o1.calculateOutcome(visitor), o2.calculateOutcome(visitor));
		    }});
		checked = true;
	    }
	}
	
	return new ArrayList<Line>(lines);
    }
    
    public List<Line> getLines() {
	return new ArrayList<Line>(lines);
    }
    
    
    
    public static Ticket fromJson(JsonObject json) {
	
	Ticket result = null;
	List<Line> lines = new ArrayList<Line>();
	
	JsonArray jsonLines = json.getJsonArray("lines");
	for (int i = 0; i < jsonLines.size(); i++)
	    lines.add(Line.fromJson(jsonLines.getJsonObject(i)));
	
	int min = json.getInt("minValue");
	int max = json.getInt("maxValue");
	int lineSize = json.getInt("lineSize"); 
	int id = json.getInt("id");
	boolean checked = json.getBoolean("checked");
	
	result = new Ticket(lines, lineSize, min, max, id, checked);

	return result;
    }

    public static JsonObject toJson(Ticket ticket) {
	JsonObject result = null;

	JsonArrayBuilder linesArrayBuilder = Json.createArrayBuilder();
	for (Line line : ticket.lines)
	    linesArrayBuilder.add(Line.toJsonBuilder(line));

	result = Json.createObjectBuilder()
		.add("lines", linesArrayBuilder)
		.add("minValue", ticket.minValue)
		.add("maxValue", ticket.maxValue)
		.add("id", ticket.id)
		.add("checked", ticket.checked)
		.add("lineSize", ticket.lineSize).build();

	return result;
    }
    
    private static final Line generateLine(int size, int min, int max){
	Line result = null;
	int[] numbers = new int[size];
	
	for(int i = 0; i < size; i++)
	    numbers[i] = min + random.nextInt(max - min + 1);

	result = new Line(numbers);
	return result;
    }

    private static List<Line> generateLines(int numOfLines, int lineSize, int minValue, int maxValue){
	List<Line> lines = new ArrayList<Line>();
	for(int i = 0; i < numOfLines; i++)
	    lines.add(generateLine(lineSize, minValue, maxValue));
	
	return lines;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((checked == null) ? 0 : checked.hashCode());
	result = prime * result + id;
	result = prime * result + lineSize;
	result = prime * result + ((lines == null) ? 0 : lines.hashCode());
	result = prime * result + maxValue;
	result = prime * result + minValue;
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
	Ticket other = (Ticket) obj;
	if (checked == null) {
	    if (other.checked != null)
		return false;
	} else if (!checked.equals(other.checked))
	    return false;
	if (id != other.id)
	    return false;
	if (lineSize != other.lineSize)
	    return false;
	if (lines == null) {
	    if (other.lines != null)
		return false;
	} else if (!lines.equals(other.lines))
	    return false;
	if (maxValue != other.maxValue)
	    return false;
	if (minValue != other.minValue)
	    return false;
	return true;
    }
    
    

}

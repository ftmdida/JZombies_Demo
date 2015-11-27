package jzombies;



import java.util.ArrayList;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;


/***
 * 
 * This class is a subclass of 
 * Agent class that represents a user
 * who sends normal requests to the defender
 *
 */
public class User extends Agent {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	Defender defender;
	ArrayList<Request> listOfRequests;
	int sentCounter = 0;

	/***
	 * 
	 * @param space is ContinuousSpace
	 * @param grid is Grid
	 * @param sourceIP is ip address of the user
	 */
	public User(ContinuousSpace<Object> space, Grid<Object> grid, String sourceIP) {
		super(sourceIP);
		this.space = space;
		this.grid = grid;
		listOfRequests=new ArrayList<Request>();
		
	}

	/**
	 *
	 * This is the step behavior.
	 * step here you can check your variables for statistics.
	 * Schedule the step method for agents.  
	 * The method is scheduled starting at tick one with an interval of 1 tick.  
	 * Specifically, the step starts at 1, and
	 * and recurs at 1,2,3,...etc. This method also helps to simulate sending request. 
	 */
	@ScheduledMethod(start=1,interval=1)
	public void step() {
		if(sentCounter < listOfRequests.size()){
			send(listOfRequests.get(sentCounter++));
		}
	}

	@Override
	public void send(Request request) {
		defender.receive(request);
	}

	@Override
	public void receive(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connect(Agent agent) {
		// TODO Auto-generated method stub
		defender = (Defender) agent;
		agent.connect(this);
	}
	
	/***
	 * This function will add the user request
	 * into the sending list.
	 * @param req is Request
	 */
	public void addRequest(Request req){
		listOfRequests.add(req);
	}

}

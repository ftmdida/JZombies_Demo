package jzombies;

import java.util.ArrayList;

import jzombies.defender.SecurityPolicyList;
import jzombies.defender.action.Action;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;


/***
 * 
 * This class is a subclass of 
 * Agent class that represents a defender
 * who receives the request from user and attacker,
 * and verifies request against the security policies and
 * take the appropriate action. 
 */

public class Defender extends Agent{
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;
	ArrayList<Agent> connections;
	SecurityPolicyList securityPolicList;
	
	/***
	 * 
	 * @param space is ContinuousSpace
	 * @param grid is Grid
	 * @param sourceIP is ip address of the defender
	 */
	public Defender(ContinuousSpace<Object> space, Grid<Object> grid, String sourceIP) {
		super(sourceIP);
		connections = new ArrayList<Agent>();
		this.space = space;	
		this.grid = grid;
	}

	@Watch(watcheeClassName = "jzombies.Zombie", watcheeFieldNames = "moved", 
			query = "within_vn 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void run() {

	}
	
	/***
	 * This function will set the security policy list
	 * into the defender.
	 * @param secpls is SecurityPolicyList
	 */
	public void setSecurityPolicyList(SecurityPolicyList secpls){
		this.securityPolicList = secpls; 
	}
	
	
	@Override
	public void connect(Agent agent) {
		connections.add(agent);
	}

	@Override
	public void send(Request request) {
		
		for (Agent agent : connections) {
			if(agent.getSourceIP() == request.getSourceIP()){
				agent.receive(request);
				break;
			}
		}
	}

	@Override
	public void receive(Request request) {
			
		ArrayList<Action> actions = securityPolicList.verify(request);
	
		for (Action action : actions) {
			action.execute(request);
		}
		
	}
}

	

		
		
	


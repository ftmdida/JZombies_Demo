package jzombies;

/**
 * 
 *This class represents the base for
 *all the agents; user, defender, and attacker. 
 */

public abstract class Agent implements Connection {
	protected String sourceIP;
	
	/***
	 * Parameterized constructor
	 * @param sourceIP is the agent's ip address
	 */
	public Agent(String sourceIP) {
		this.sourceIP=sourceIP;
	}


	public String getSourceIP(){
		return sourceIP;
	}
	
}

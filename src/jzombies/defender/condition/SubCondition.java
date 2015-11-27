package jzombies.defender.condition;

import jzombies.Request;


public abstract class SubCondition {
	/**
	 * This function will verify if a request meet certain condition  
	 * @param r The request
	 * @return result of the condition
	 */
	public abstract boolean check(Request r) ;
	
}

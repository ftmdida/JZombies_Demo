package jzombies.defender.action;

import jzombies.Request;

/**
 * This Class defined the action that will be fired in case the incoming
 * request meet or violate security policy 
 * @author User-Zero
 *
 */
public abstract class  Action {
	public abstract void execute(Request r);
	
	@Override
	public abstract boolean equals(Object obj);
}

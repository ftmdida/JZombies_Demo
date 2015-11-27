package jzombies.defender.action;

import jzombies.Request;
/**
 * This action will allow the incoming request to reach it's destination. 
 * @author User-Zero
 *
 */
public class PassRequst extends Action{

	@Override
	public void execute(Request r) {
		// TODO Auto-generated method stub
		System.out.println("Defender: Passed incoming request from " +r.getSourceIP());
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof PassRequst)
			return true;
		return false;
	}
}

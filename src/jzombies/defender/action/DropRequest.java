package jzombies.defender.action;

import jzombies.Request;

/**
 * This action will drop the incoming request and send error code to the 
 * sender.
 * @author User-Zero
 *
 */
public class DropRequest extends Action{

	@Override
	public void execute(Request r) {
		// TODO Auto-generated method stub
		System.out.println("Defender: Dropped incoming request from " +r.getSourceIP());
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof DropRequest)
			return true;
		return false;
	}
}

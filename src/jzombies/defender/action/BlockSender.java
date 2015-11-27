package jzombies.defender.action;

import jzombies.Request;
/**
 * This action will block the sender and prevent any other incoming requests 
 * from the same sender. 
 * @author User-Zero
 *
 */
public class BlockSender extends Action{

	@Override
	public void execute(Request r) {
		// TODO Auto-generated method stub
		System.out.println("Defender: Blocked incoming request from " +r.getSourceIP());
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof BlockSender)
			return true;
		return false;
	}

}

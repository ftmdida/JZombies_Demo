package jzombies.defender.condition;

import jzombies.Protocol;
import jzombies.Request;

/**
 *This class will check if a request uses certain protocol.  
 * @author User-Zero
 *
 */
public class ProtocolCheck extends SubCondition {
	Protocol protocol;
	
	
	public ProtocolCheck(Protocol protocol) {
		this.protocol = protocol;
	}
	
	
	@Override
	public boolean check(Request r) {
		if(protocol.getStatusCode() == r.getProtocol().getStatusCode()){
			return true;
		}
		return false;
	}

}

package jzombies.defender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jzombies.Protocol;
import jzombies.Request;
import jzombies.defender.action.Action;
import jzombies.defender.action.BlockSender;
import jzombies.defender.action.DropRequest;
import jzombies.defender.action.PassRequst;
import jzombies.defender.condition.SubCondition;
import jzombies.defender.condition.ProtocolCheck;
import jzombies.util.SecurityPolicyListParser;

/**
 * Represent core component for the defender class, this class will contain 
 * all the security policies and conditions used in the defender to be enforced
 * on the incoming requests.
 * @author User-Zero
 *
 */
public class SecurityPolicyList {
	List<SecurityPolicy> securityPolicyList;
	List<Condition>  conditionSubList;
	
	public SecurityPolicyList(){
		securityPolicyList = new ArrayList<SecurityPolicy>();
		conditionSubList = new ArrayList<Condition>();
		//buildSecurityPolicyList();
	}
	
	public List<SecurityPolicy> getSecurityPolicy() {
		return securityPolicyList;
	}
	public void setSecurityPolicy(List<SecurityPolicy> securityPolicy) {
		this.securityPolicyList = securityPolicy;
	}
	public List<Condition> getBehavior() {
		return conditionSubList;
	}
	public void setConditions(List<Condition> condition) {
		this.conditionSubList = condition;
	}

	public void addSecurityPolicy(SecurityPolicy sp) {
		securityPolicyList.add(sp);
		
	}
	/**
	 * 
	 * This method will make a call to defender xml file parser
	 * It will get the security policies and insert it into
	 * security policy list. In other words, this method will help to 
	 * build the security policy list for this project.
	 * 
	 */
	public void buildSecurityPolicyList(){
		
	}
	/**
	 * This function will verify if the incoming request meet all 
	 * security policies enforced on the same end point. 
	 * @param r incoming request
	 * @return True if request meet all the security policies otherwise false.
	 */
	public ArrayList<Action> verify(Request r){
		
		ArrayList<Action> list = new ArrayList<Action>();
		ArrayList<Action> temp ;
		
		for (SecurityPolicy securityPolicy : securityPolicyList) {
			temp = securityPolicy.enforce(r);
			for (Action action : temp) {// check this out
				if(!list.contains(action)){
					if(action instanceof DropRequest || action instanceof BlockSender){
						list.remove(new PassRequst());
						list.add(action);
					}
					else {
						if(!list.contains(new BlockSender()) && !list.contains(new DropRequest()) )
							list.add(action);
					}
				}
			}
		}
		return list;
		}

	
		
	
}

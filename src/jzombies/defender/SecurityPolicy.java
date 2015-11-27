package jzombies.defender;

import java.util.ArrayList;
import java.util.List;

import jzombies.Request;
import jzombies.defender.action.Action;
/**
 * Security policy represent a rule that enforced on the incoming requests 
 * for a specific end point.
 * @author User-Zero
 *
 */
public class SecurityPolicy {
	EndPoint endPoint;
	String policy;
	ArrayList<Condition> conditionList;
	ArrayList<String> operationList;

	ArrayList<Action> truePortionActionList;
	ArrayList<Action> falsePortionActionList;

	/**
	 * Parameterized constructor 
	 * @param endPoint The end point that this security policy will be 
	 * enforced on. 
	 * @param truePortionActionList The action(s) in case the request met
	 *  the security policy.
	 * @param falsePortionActionList The action(s) in case the request 
	 *  doesn't meet the security policy.
	 */
	public SecurityPolicy(EndPoint endPoint,
			ArrayList<Action> truePortionActionList,
			ArrayList<Action> falsePortionActionList) {
		this.endPoint = endPoint;
		this.truePortionActionList = truePortionActionList;
		this.falsePortionActionList = falsePortionActionList;
		conditionList= new ArrayList<Condition>();
	}

	public EndPoint getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(EndPoint endPoint) {
		this.endPoint = endPoint;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public List<Condition> getCondition() {
		return conditionList;
	}

	public void setConditon(ArrayList<Condition> condition) {
		this.conditionList = condition;
	}

	/**
	 * Using this function you can create security rule with multiple 
	 * conditions, but you have to send list of operations in case more
	 * than one condition get used. 
	 * @param condition Condition
	 */
	public void addCondition(Condition condition) {
		this.conditionList.add(condition);
	}

	/**
	 * This function will verify it certain request meet a security policy
	 * @param req In coming request
	 * @return return True if the request met the rule(s) otherwise false.
	 * 
	 */
	public ArrayList<Action> enforce(Request req) {
		boolean result = true;
		
		/*if(req.getProtocol() != endPoint.getProtocol() || req.getPort() != endPoint.getPort())
			return new ArrayList<Action>();
		*/
		if (operationList.size() > 0) {
			result = conditionList.get(0).getResultsOfAllSubConditions(req);
			for (int i = 1; i < conditionList.size(); i++) {
				if (operationList.get(i - 1).contains("&")) {
					result &= conditionList.get(i)
							.getResultsOfAllSubConditions(req);
				} else {
					result |= conditionList.get(i)
							.getResultsOfAllSubConditions(req);
				}
			}

		} else {
			result = conditionList.get(0).getResultsOfAllSubConditions(req);
		}

		return result ? truePortionActionList : falsePortionActionList;
	}
	
	/**
	 * In case of multiple conditions, list of the operators must be given 
	 * @param op List of operators [&amp; or |]
	 */
	public void setOperationList(ArrayList<String> op) {
		this.operationList = op;
	}

}

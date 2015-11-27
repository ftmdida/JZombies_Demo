package jzombies.defender;

import java.util.ArrayList;
import jzombies.Request;
import jzombies.defender.condition.SubCondition;


/***
 * 
 * This class is a core components for building 
 * security policies which compose of one or more subconditions
 *
 */
public class Condition {
	ArrayList<SubCondition> subConditions;
	
	public Condition(){
		this.subConditions= new ArrayList<SubCondition> ();
	}

	public ArrayList<SubCondition> getCondition() {
		return subConditions;
	}

	public void setSubCondition(ArrayList<SubCondition> scondition) {
		this.subConditions = scondition;
	}
	
/***
 * This method helps to add the subconditions for the corresponding condition. 
 * @param cond is SubCondition
 */
	public void addSubCondition(SubCondition cond){
		subConditions.add(cond);
	}
	
	/***
	 * This function will check all the subconditions
	 * and return the result of them. 
	 * @param r is Resulst
	 * @return boolean variable : true or false 
	 */
	public boolean getResultsOfAllSubConditions(Request r){
		boolean results = true;
		
		for (SubCondition scondition : subConditions) {
			results &= scondition.check(r);
		}
		return results;
	}


}

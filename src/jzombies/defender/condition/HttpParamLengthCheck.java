package jzombies.defender.condition;

import java.util.Iterator;
import java.util.Map;

import jzombies.Request;
import jzombies.util.HttpDataParser;
import jzombies.util.HttpDataParser.HttpData;

/**
 * This Class will check the length of the parameter(s) in a http request
 * if they are less or greater or equals to a certain length
 * @author User-Zero
 *
 */
public class HttpParamLengthCheck extends SubCondition {

	OpCode oc;
	int size;
	/**
	 * 
	 * @param opc Operation code [lessThan or greaterThan or equalsTo]
	 * @param size length 
	 */
	public HttpParamLengthCheck(OpCode opc, int size){
		this.oc = opc;
		this.size = size;
	}
	
	public enum OpCode{
		LessThan,
		GreaterThan,
		EqualsTo
	}
	
	@Override
	public boolean check(Request r) {
		// TODO Auto-generated method stub
		HttpDataParser d = new HttpDataParser();
		HttpData dd = d.parse(r.getData());
		
		 Iterator it = dd.getParam().entrySet().iterator();
		 while (it.hasNext()) {
		     Map.Entry pair = (Map.Entry)it.next();
		     int ca = ((String) pair.getValue()).length();
		     
		     switch(oc){
		     case EqualsTo: 
		    	 if( ca == size) break;
		    	 else
		    		 return false;
		     case LessThan: 
		    	 if ( ca  < size ) break;
		    	 else
		    		 return false;
		     case GreaterThan:
		    	 if( ca  > size ) break ;
		    	 else
		    		 return false;
		     }
		     
		     it.remove(); // avoids a ConcurrentModificationException
		 }
		
		return true;
	}

}

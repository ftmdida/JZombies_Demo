package jzombies.defender.condition;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jzombies.Request;
import jzombies.util.HttpDataParser;
import jzombies.util.HttpDataParser.HttpData;

/**
 * This class will check if the parameters of http request match to 
 * a certain regular expression or not.
 * @author User-Zero
 *
 */
public class HttpRegExpMatchCheck extends SubCondition {

	String regExp;
	
	public HttpRegExpMatchCheck(String regExp) {
		// TODO Auto-generated constructor stub
		
		this.regExp = regExp;
	}
	
	@Override
	public boolean check(Request r) {
		// TODO Auto-generated method stub
		// Create a Pattern object
	      Pattern patt = Pattern.compile(regExp);

	    // Now create matcher object.
	    Matcher m ;
	      
	    HttpDataParser d = new HttpDataParser();
		HttpData dd = d.parse(r.getData());
		
		 Iterator it = dd.getParam().entrySet().iterator();
		 while (it.hasNext()) {
		     Map.Entry pair = (Map.Entry)it.next();
		     m = patt.matcher(pair.getValue().toString());
		     
		     if(m.find())
		    	 return true;
		     it.remove();
		 }
	
		return false;
	}

}

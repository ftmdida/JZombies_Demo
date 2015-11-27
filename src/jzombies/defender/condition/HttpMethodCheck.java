package jzombies.defender.condition;

import jzombies.HttpMethod;
import jzombies.Request;
import jzombies.util.HttpDataParser;
import jzombies.util.HttpDataParser.HttpData;

/**
 * This class will check the method used in http request if it match 
 * certain method or not.
 * @author User-Zero
 *
 */
public class HttpMethodCheck extends SubCondition{

	HttpMethod httpMethod;
	/**
	 * 
	 * @param method Http Method it can be [GET,POST,HEAD,PUT,DELETE]
	 */
	public HttpMethodCheck(HttpMethod method) {
		// TODO Auto-generated constructor stub
		this.httpMethod = method;
	}
	
	@Override
	public boolean check(Request r) {
		HttpDataParser d = new HttpDataParser();
		HttpData dd = d.parse(r.getData());
		HttpMethod httpMethod2 = dd.getMethod();
		
		if(httpMethod2 == this.httpMethod)
			return true;
		
		return false;
	}

}

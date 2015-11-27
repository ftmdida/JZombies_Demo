package jzombies.util;

import java.util.HashMap;

import jzombies.HttpMethod;

/***
 * This class parse the data portion of the request
 * to extract the HTTP headers, and body values
 *
 */
public class HttpDataParser {

	/***
	 * This method will parse the data portion
	 * of incoming HTTP request.
	 * @param data 
	 * @return HttpData
	 */
	public HttpData parse(String data){
		HttpData hd = new HttpData();
		
		if(data.length() == 0){
			return null;
		}
		
		

			String[] lines = data.split("\n");
			for (String line : lines) {
				line = line.replace("\t", "");
				if(line.toLowerCase().startsWith("get")){
					hd.setMethod(HttpMethod.GET);
					line = line.replace("GET ", "");
					line = line.substring(line.indexOf("?") + 1 , line.indexOf("HTTP/"));
					
					hd.setParam(getParam(line));
					break;
				}else if(line.toLowerCase().startsWith("post")){
					hd.setMethod(HttpMethod.POST);
					String str = lines[lines.length - 1].replace("\t", "");
					hd.setParam(getParam(str));
					break;
				}

			
		}
		
		return hd;
	}
	/***
	 * This method will return all the parameters
	 * from the body portion in HTTP requests.
	 * @param par is String
	 * @return HashMap 
	 */
	public HashMap<String, String> getParam(String par){
		HashMap<String, String> params = new HashMap<String, String>();
		String[] lines = par.split("&");
		for (String line : lines) {
			String[] parts = line.split("=");
			if(parts.length == 2 )
			params.put(parts[0], parts[1]);
			else if(parts.length > 2  ){
				String temp = "";
				for (int i = 1; i < parts.length; i++) {
					if(i+1 < parts.length){
						temp += parts[i] + "=";
					}else{
						temp += parts[i];
					}
				}
				
				params.put(parts[0],temp);
			}
		}
		
		return params;
	}
	/***
	 * 
	 * This class contains HTTP header's information
	 * and the body. 
	 *
	 */
	public class HttpData{
		HttpMethod method;
		HashMap<String, String> param;
		String host;
		String agent;
		
		public HttpMethod getMethod() {
			return method;
		}
		public void setMethod(HttpMethod method) {
			this.method = method;
		}
		public HashMap<String, String> getParam() {
			return param;
		}
		public void setParam(HashMap<String, String> param) {
			this.param = param;
		}
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getAgent() {
			return agent;
		}
		public void setAgent(String agent) {
			this.agent = agent;
		}
		
	}
	
}

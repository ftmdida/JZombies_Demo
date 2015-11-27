package jzombies;


/***
 * 
 * This is an interface that helps to 
 * make the connection between the agents. 
 *
 */
public interface Connection {
	/***
	 * This function will connect the source agent 
	 * to defender.
	 * @param agent is Agent 
	 */
	void connect(Agent agent);
	
	/***
	 * This function will send the request 
	 * from the sender agent to defender.
	 * @param request
	 */
	void send(Request request);
	
	/***
	 * This function will receive the response 
	 * from the defender.
	 * @param request
	 */
	void receive(Request request);
}

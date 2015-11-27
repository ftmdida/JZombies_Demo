package jzombies.defender;

import jzombies.Protocol;
/**
 * End point represent a port and protocol 
 * @author User-Zero
 *
 */
public class EndPoint {
	int port;
	Enum<Protocol> protocol;
	
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	public Enum<Protocol> getProtocol() {
		return protocol;
	}
	public void setProtocol(Enum<Protocol> protocol) {
		this.protocol = protocol;
	}
	

}

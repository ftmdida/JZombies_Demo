package jzombies;


/***
 * This is an enumeration for Protocols
 * that represents a protocol which can be either
 * HTTP, HTTPS, FTP, SSH
 *
 */
public enum Protocol {
	
	HTTP(80), HTTPS(443), FTP(21) , SSH(22);
	
	private int protocol;

	private Protocol(int s) {
		protocol = s;
	}

	public int getStatusCode() {
		return protocol;
	}
}

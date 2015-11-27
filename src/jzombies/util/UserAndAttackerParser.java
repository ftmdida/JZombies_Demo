package jzombies.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jzombies.Agent;
import jzombies.Attacker;
import jzombies.Protocol;
import jzombies.Request;
import jzombies.User;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * This class will parse the Users.xml file,
 * to find the user and attacker
 */
public class UserAndAttackerParser{


	/**
	 * 
	 * This method will parse the Users.xml file,
	 * look for the defender
	 * @param space ContinuousSpace
	 * @param grid Grid
	 * @return an ArrayList of agents that include instance
	 * of User and Attacker
	 */
	public ArrayList<Agent> parseUsers(ContinuousSpace<Object> space,
			Grid<Object> grid)
					throws ParserConfigurationException, SAXException, IOException {
		ArrayList<Agent> agentsList = new ArrayList<Agent>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document dom = db.parse("src/s3_Users.xml");
		dom.getDocumentElement().normalize();
		Element docEle = dom.getDocumentElement();


		NodeList nl = docEle.getChildNodes();
		boolean isUser = false;

		if(nl!=null && nl.getLength()>0){
			for (int i = 0; i < nl.getLength(); i++) {
				if(nl.item(i).getNodeType() == Node.ELEMENT_NODE){
					Element el=(Element) nl.item(i);
					if(el.getNodeName().toUpperCase().contains("USER")){

						isUser = el.getAttribute("type").toUpperCase().contains("USER");

						ArrayList<Request> rl = new ArrayList<Request>();

						for (int j = 0; j < el.getElementsByTagName("request").getLength(); j++) {
							String protocol=el.getElementsByTagName("protocol").item(j).getTextContent().toUpperCase();

							Protocol pro = getProtocol(protocol);
							rl.add(new Request(el.getElementsByTagName("source").item(j).getTextContent().replace("\t", ""), el.getElementsByTagName("destination").item(j).getTextContent().replace("\t", ""), Integer.parseInt(el.getElementsByTagName("port").item(j).getTextContent()), el.getElementsByTagName("data").item(j).getTextContent().replace("\t", ""), pro));
						}
						if(isUser){
							User u = new User(space,grid,el.getAttribute("sourceIP"));
							for (Request request : rl) {
								u.addRequest(request);
							}

							agentsList.add(u);
						}
						else{
							Attacker u = new Attacker(space,grid,el.getAttribute("sourceIP"));
							for (Request request : rl) {
								u.addRequest(request);
							}
							//u.print();
							agentsList.add(u);

						}
					}
				}
			}

		}
		return agentsList;
	}

	/**
	 * 
	 * This method will find the protocol,
	 * @param value the string value found within the xml file
	 * @return the matched protocol
	 * 
	 */
	private static Protocol getProtocol(String value) {
		Protocol pro = null;		
		for (Protocol b : Protocol.values()) {
			if (value.equals((b.toString().toUpperCase()))) {
				pro= b;
				break;
			}
		}
		return pro;
	}


}

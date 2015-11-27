package jzombies.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jzombies.Defender;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * This class will parse the defender.xml file,
 * to find the defender
 */
public class DefenderParser{
	
	/**
	 * 
	 * This method will parse the defender.xml file,
	 * look for the defender
	 * @param space ContinuousSpace
	 * @param grid Grid
	 * @return an instance of Defender agent
	 * 
	 */
	public Defender parseDefender(ContinuousSpace<Object> space, Grid<Object> grid) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom2 = db.parse("src/s3_defender.xml");
		dom2.getDocumentElement().normalize();
		Element docEle2 = dom2.getDocumentElement();
		Defender defender= null;
		NodeList nl = docEle2.getChildNodes();
		String sourceIP = null;
		if(nl!=null && nl.getLength()>0){
			for (int i = 0; i < nl.getLength(); i++) {
				if(nl.item(i).getNodeType() == Node.ELEMENT_NODE){
					Element el=(Element) nl.item(i);
					sourceIP= el.getAttribute("sourceIP").toString();
					
				}
			}
		}
		
		defender= new Defender(space, grid, sourceIP);
		defender.setSecurityPolicyList(SecurityPolicyListParser.getSecurityPolicyList());
		
		return defender;
	}
}

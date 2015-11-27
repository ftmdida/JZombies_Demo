package jzombies.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jzombies.HttpMethod;
import jzombies.Protocol;
import jzombies.defender.Condition;
import jzombies.defender.EndPoint;
import jzombies.defender.SecurityPolicy;
import jzombies.defender.SecurityPolicyList;
import jzombies.defender.action.Action;
import jzombies.defender.action.BlockSender;
import jzombies.defender.action.DropRequest;
import jzombies.defender.action.PassRequst;
import jzombies.defender.condition.FrequencyCheck;
import jzombies.defender.condition.HttpMethodCheck;
import jzombies.defender.condition.HttpParamLengthCheck;
import jzombies.defender.condition.HttpParamLengthCheck.OpCode;
import jzombies.defender.condition.HttpRegExpMatchCheck;
import jzombies.defender.condition.ProtocolCheck;
import jzombies.defender.condition.SubCondition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 
 * This class will parse the defender.xml file,
 * and find the security policies 
 */
public class SecurityPolicyListParser {
	
	/**
	 * 
	 * This method will find the SecurityPolicies within the defender.xml file,
	 * @return an instance of SecurityPolicy
	 */
public static SecurityPolicyList getSecurityPolicyList() throws ParserConfigurationException, SAXException, IOException {
		
	    SecurityPolicyList spList= new SecurityPolicyList();
		SecurityPolicy sp=null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom2 = db.parse("src/s3_defender.xml");
		dom2.getDocumentElement().normalize();
		Element docEle2 = dom2.getDocumentElement();

		NodeList nl2 = docEle2.getElementsByTagName("securityPolicyList");
		EndPoint ep = null;
		ArrayList<Action> tc = new ArrayList<Action>();
		ArrayList<Action> fc = new ArrayList<Action>();
		
		ArrayList<Condition> conditions= new ArrayList<Condition>();
		ArrayList<String> op=new ArrayList<String>();
		for (int i = 0; i < nl2.getLength(); i++) {
			if(nl2.item(i) instanceof Element){
	
				NodeList children = ((Element) nl2.item(i)).getElementsByTagName("securityPolicy");
				
				for (int j = 0; j < children.getLength(); j++) {
					if(children.item(j) instanceof Element){
						ep= getEndPoint(children, j);
						
						tc=getTrueActionPortion(children, j);

						fc=getFalseActionPortion(children, j);
						
						sp = new SecurityPolicy(ep,tc,fc);
						
						conditions=getPolicies(docEle2, children, j);
						
						for (Condition condition : conditions) {
							sp.addCondition(condition);
						}

						op=getOperators(children, j);
						sp.setOperationList(op);
						
						spList.addSecurityPolicy(sp);
					}

				}
				
			}
		}
		return spList;
	}
	
	/**
	 * 
	 * This method will find the port and protocol for the security policy,
	 * @param children NodeList 
	 * @param j index
	 * @return an instance of Endpoint
	 */
	private static EndPoint getEndPoint(NodeList children, int j) {
		
		EndPoint ep = new EndPoint();
		NodeList endPointChildren= ((Element)children.item(j)).getElementsByTagName("endpoint");
		HashMap<String, Integer> hmap= new HashMap<String,Integer>();
		Protocol protocol = null; 
		Integer port = 0;
		for (int k = 0; k < endPointChildren.getLength(); k++) {
			if (endPointChildren.item(k) instanceof Element) {
				Element e= (Element) endPointChildren.item(k);
				String value=e.getElementsByTagName("protocol").item(k).getTextContent().toUpperCase();
				protocol = getProtocol(value);
				port= Integer.parseInt(e.getElementsByTagName("port").item(k).getTextContent());
				hmap.put("protocol", protocol.getStatusCode());
				hmap.put("port", Integer.parseInt(e.getElementsByTagName("port").item(k).getTextContent()));
			}       
		}
		ep.setProtocol(protocol);
		ep.setPort(port);
		return ep;
	}
	
	/**
	 * 
	 * This method will find the allowed behaviors for the policies,
	 * @param children NodeList 
	 * @param j index
	 * @return an array list of Action
	 */
	private static ArrayList<Action> getTrueActionPortion(NodeList children, int j) {
		ArrayList<Action> tc= new ArrayList<Action>();
		NodeList trueActionChildren= ((Element)children.item(j)).getElementsByTagName("trueAction");
		for (int k = 0; k < trueActionChildren.getLength(); k++) {
			if (trueActionChildren.item(k) instanceof Element) {
				Element actionElement= (Element)trueActionChildren.item(k);
				for (int l = 0; l < actionElement.getElementsByTagName("action").getLength(); l++) {
					
					String actionName=actionElement.getElementsByTagName("action").item(l).getAttributes().getNamedItem("type").getNodeValue();
					if(actionName.equalsIgnoreCase("pass")){ 
						tc.add(new PassRequst());
					}
					else if(actionName.equalsIgnoreCase("drop")){
						tc.add(new DropRequest());
					}else{
						tc.add(new BlockSender());
					}
				}
			}
		}
		return tc;
	}
	
	/**
	 * 
	 * This method will find the disallowed behaviors for the policies,
	 * @param children NodeList 
	 * @param j index
	 * @return an array list of Action
	 */
	private static ArrayList<Action> getFalseActionPortion(NodeList children, int j) {
		ArrayList<Action> fc= new ArrayList<Action>();
		NodeList falseActionChildren= ((Element)children.item(j)).getElementsByTagName("falseAction");
		for (int k = 0; k < falseActionChildren.getLength(); k++) {
			if (falseActionChildren.item(k) instanceof Element) {
				Element falseAction= (Element)falseActionChildren.item(k);
				for (int l = 0; l < falseAction.getElementsByTagName("action").getLength(); l++) {
					String actionName=falseAction.getElementsByTagName("action").item(l).getAttributes().getNamedItem("type").getNodeValue();
					if(actionName.equalsIgnoreCase("pass")){
						fc.add(new PassRequst());
					}
					else if(actionName.equalsIgnoreCase("drop")){
						fc.add(new DropRequest());
					}else{
						fc.add(new BlockSender());
					}
				}
			}
		}
		return fc;
	}
	
	/**
	 * 
	 * This method will find the security policies within the defender.xml,
	 * @param docEle2 SecurityPolicyList element under the defender node
	 * @param children NodeList 
	 * @param j index
	 * @return a hash map of conditions for each security policy
	 * 
	 */
	private static ArrayList<Condition> getPolicies(Element docEle2, NodeList children, int j) {

		ArrayList<Condition> conditionsList= new ArrayList<Condition>();
		HashMap<String, Condition> conditionHashMap = getConditions(children, j);	
	
		HashMap<String, ArrayList<SubCondition>> subConditionHashMap=getSubConditions(docEle2);
		
	
		
				Iterator iter = conditionHashMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry mEntry = (Map.Entry) iter.next();//conditions
					
					
					Iterator iter2 = subConditionHashMap.entrySet().iterator();
					while (iter2.hasNext()) {
						Map.Entry mEntry2 = (Map.Entry) iter2.next();//subconditions
						if(mEntry.getKey().toString().equals(mEntry2.getKey().toString())){
							
							for (SubCondition sub: (ArrayList<SubCondition>)mEntry2.getValue() ){
								((Condition)mEntry.getValue()).addSubCondition(sub);
							}
						
							//conditionsList.add(((Condition)mEntry.getValue()));
						}				
					}
					conditionsList.add(((Condition)mEntry.getValue()));
				}
				return conditionsList;

	}
	
	/**
	 * 
	 * This method will find the conditions for each security policy 
	 * within the defender.xml,
	 * @param children NodeList 
	 * @param j index
	 * @return a hash map of conditions for each security policy
	 * 
	 */
	private static HashMap<String, Condition> getConditions(NodeList children,
			int j) {
		HashMap<String, Condition> conditionHashMap= new HashMap<String, Condition>();
		NodeList policy= ((Element)children.item(j)).getElementsByTagName("policy");
		for (int k = 0; k < policy.getLength(); k++) {
			if (policy.item(k) instanceof Element) {
				Element cond= (Element)policy.item(k);
				String policyName;
				//System.out.println(cond.getElementsByTagName("condition").getLength());
				for (int l = 0; l < cond.getElementsByTagName("condition").getLength(); l++) {
					policyName=cond.getElementsByTagName("condition").item(l).getAttributes().getNamedItem("name").getNodeValue();

					Condition c1= new Condition();

					conditionHashMap.put(policyName, c1);
				}
			}
		}
		return conditionHashMap;
	}
	
	/**
	 * 
	 * This method will find the subconditions within the defender.xml,
	 * @param docEle2 SecurityPolicyList element under the defender node
	 * @return a hash map of subconditions list for each condition
	 * 
	 */
	private static HashMap<String, ArrayList<SubCondition> > getSubConditions(Element docEle2) {
		
		
		
		HashMap<String, ArrayList<SubCondition> > subconditionHashMap= new HashMap<String, ArrayList<SubCondition> >();
		NodeList conditionListNodes = docEle2.getElementsByTagName("conditionList");
		for (int m = 0; m < conditionListNodes.getLength(); m++) {
			if(conditionListNodes.item(m) instanceof Element){
				NodeList conditions = ((Element)conditionListNodes.item(m)).getElementsByTagName("condition");
				for (int i = 0; i < conditions.getLength(); i++) {
					if(conditions.item(i) instanceof Element){
						String conditionName= conditions.item(i).getAttributes().getNamedItem("name").getNodeValue();
						
						NodeList subConditions= ((Element) conditions.item(i)).getElementsByTagName("subcondition");
						for (int k = 0; k < subConditions.getLength(); k++) {
							if(subConditions.item(k) instanceof Element){
								String subConditionsName= subConditions.item(k).getAttributes().getNamedItem("name").getNodeValue();
								String subConditionsValue= subConditions.item(k).getTextContent().toString().trim();
								//System.out.println("conditionName:" +conditionName +"--> " +"subConditionsName: " + subConditionsName +"subConditionsValue: "  + subConditionsValue);
								if(subConditionsName.equals("isRequestProtocolEqualsTo")){
									if(subconditionHashMap.containsKey(conditionName)){
										subconditionHashMap.get(conditionName).add(new ProtocolCheck(getProtocol(subConditionsValue)));
									}else{
										ArrayList<SubCondition> templist = new ArrayList<SubCondition>();
										
										subconditionHashMap.put(conditionName, templist);
										templist.add(new ProtocolCheck(getProtocol(subConditionsValue)));
									}
									
								}
								else if(subConditionsName.equals("isHTTPMethodEqualsTo")){				
									//subconditionHashMap.put(conditionName, new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									
									if(subconditionHashMap.containsKey(conditionName)){
										subconditionHashMap.get(conditionName).add(new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									}else{
										ArrayList<SubCondition> templist = new ArrayList<SubCondition>();
										
										subconditionHashMap.put(conditionName, templist);
										templist.add(new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									}
									
								}
								else if(subConditionsName.equals("isParaContentMatchRegExp")){				
									//subconditionHashMap.put(conditionName, new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									
									if(subconditionHashMap.containsKey(conditionName)){
										subconditionHashMap.get(conditionName).add(new HttpRegExpMatchCheck(subConditionsValue));
									}else{
										ArrayList<SubCondition> templist = new ArrayList<SubCondition>();
										
										subconditionHashMap.put(conditionName, templist);
										templist.add(new HttpRegExpMatchCheck(subConditionsValue));
									}
									
								}
								
								else if(subConditionsName.equals("isRequestFrequencyLessThan")){				
									//subconditionHashMap.put(conditionName, new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									
									if(subconditionHashMap.containsKey(conditionName)){
										subconditionHashMap.get(conditionName).add(new FrequencyCheck(Integer.parseInt(subConditionsValue)));
									}else{
										ArrayList<SubCondition> templist = new ArrayList<SubCondition>();
										
										subconditionHashMap.put(conditionName, templist);
										templist.add(new FrequencyCheck(Integer.parseInt(subConditionsValue)));
									}		
								}
								else if(subConditionsName.equals("isParaLenghtLessThan")){				
									//subconditionHashMap.put(conditionName, new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									
									if(subconditionHashMap.containsKey(conditionName)){
										subconditionHashMap.get(conditionName).add(new HttpParamLengthCheck(OpCode.LessThan, Integer.parseInt(subConditionsValue)));
									}else{
										ArrayList<SubCondition> templist = new ArrayList<SubCondition>();
										
										subconditionHashMap.put(conditionName, templist);
										templist.add(new HttpParamLengthCheck(OpCode.LessThan, Integer.parseInt(subConditionsValue)));
									}		
								}
								
								else if(subConditionsName.equals("isParaLenghtGreaterThan")){				
									//subconditionHashMap.put(conditionName, new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									
									if(subconditionHashMap.containsKey(conditionName)){
										subconditionHashMap.get(conditionName).add(new HttpParamLengthCheck(OpCode.GreaterThan, Integer.parseInt(subConditionsValue)));
									}else{
										ArrayList<SubCondition> templist = new ArrayList<SubCondition>();
										
										subconditionHashMap.put(conditionName, templist);
										templist.add(new HttpParamLengthCheck(OpCode.GreaterThan, Integer.parseInt(subConditionsValue)));
									}		
								}
								
								else if(subConditionsName.equals("isParaLenghtEqualsTo")){				
									//subconditionHashMap.put(conditionName, new HttpMethodCheck(getHTTPMethod(subConditionsValue)));
									
									if(subconditionHashMap.containsKey(conditionName)){
										subconditionHashMap.get(conditionName).add(new HttpParamLengthCheck(OpCode.EqualsTo, Integer.parseInt(subConditionsValue)));
									}else{
										ArrayList<SubCondition> templist = new ArrayList<SubCondition>();
										
										subconditionHashMap.put(conditionName, templist);
										templist.add(new HttpParamLengthCheck(OpCode.EqualsTo, Integer.parseInt(subConditionsValue)));
									}		
								}
							}
						}
						
						
					}
				}
				
			}				

		}
		return subconditionHashMap;
	}

	/**
	 * 
	 * This method will find the operators for the conditions,
	 * @param children NodeList 
	 * @param j index
	 * @return the string[operators] of arraylist
	 * 
	 */
	private static ArrayList<String> getOperators(NodeList children, int j) {
		ArrayList<String> operatorList= new ArrayList<String>();
		NodeList operatorListChildren= ((Element)children.item(j)).getElementsByTagName("operationList");
		for (int k = 0; k < operatorListChildren.getLength(); k++) {
			if (operatorListChildren.item(k) instanceof Element) {
				Element actionElement= (Element)operatorListChildren.item(k);
				for (int l = 0; l < actionElement.getElementsByTagName("operator").getLength(); l++) {
					operatorList.add(actionElement.getElementsByTagName("operator").item(l).getTextContent());
					
				}
			}
		}
		return operatorList;
	}
	
	/**
	 * 
	 * This method will find the protocol,
	 * @param value the string value found within the xml file
	 * @return the matched protocol [HTTP(80) HTTPS(443) FTP(21) SSH(22)]
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
	
	/**
	 * 
	 * This method will find the http method,
	 * @param value the string value found within the xml file
	 * @return the matched http method; [GET,POST,HEAD,DELETE,PUT]
	 * 
	 */
	private static HttpMethod getHTTPMethod(String value) {
		HttpMethod method = null;		
		for (HttpMethod m : HttpMethod.values()) {
			if (value.toUpperCase().equals((m.toString().toUpperCase()))) {
				method= m;
				break;
			}
		}
		return method;
	}
		
}

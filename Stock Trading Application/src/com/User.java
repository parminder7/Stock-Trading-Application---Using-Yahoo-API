package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.io.File;



import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class User {
	
	private String username;
	
	private Map<String, UserAttributes> userDB;
	
	public User(){}
	//constructor
	public User(String username){
		this.username = username;
	}
	
	/**
	 * load dictionary from USER file 
	 * @param filename
	 * @return true on success
	 */
	private boolean loadUserdb(String filename){
		
		try {
			 
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("User");
		 
			System.out.println("----------------------------");
			System.out.println(nList.getLength());
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				org.w3c.dom.Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					//add to userDB
					userDB = new HashMap<String, UserAttributes>();
					UserAttributes auser = new UserAttributes();
					String uname = eElement.getAttribute("name");
					String avstocks = eElement.getElementsByTagName("stocks").item(0).getTextContent();
					String balance = eElement.getElementsByTagName("balance").item(0).getTextContent();
					String purstocks = eElement.getElementsByTagName("purchasedstock").item(0).getTextContent();
					
					//put stocks into object of UserAttribute
					String stocks[] = avstocks.split("\\s+");
					
					for(int c=0; c<stocks.length; c++)
						auser.stockTrackerSet.add(stocks[c]);
					System.out.println("Done1");
					
					//put cash balance
					auser.cashbalance = Double.parseDouble(balance.trim());
					System.out.println("Done2");
					
					//put purchased stock
					String pstocks[] = purstocks.split(",");
					for (int c=0; c<pstocks.length; c++){
						System.out.println(c);
						String qtys[] = pstocks[c].split(" ");
						auser.purchasedStock.put(qtys[0], Integer.parseInt(qtys[1]));
					}
					System.out.println("Done3");
					
					System.out.println("Staff id : " + eElement.getAttribute("name"));
					System.out.println("First Name : " + eElement.getElementsByTagName("stocks").item(0).getTextContent());
					System.out.println("Last Name : " + eElement.getElementsByTagName("balance").item(0).getTextContent());
					System.out.println("Nick Name : " + eElement.getElementsByTagName("purchasedstock").item(0).getTextContent());
					
					//load the value
					userDB.put(uname, auser);
				}
			}
		    /*
			String line = "";
			String delimiter = "/";
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			System.out.println("File found");
			
			userDB = new HashMap<String, UserAttributes>();
			
			while ((line = reader.readLine()) != null){
				UserAttributes auser = new UserAttributes();
				String uname = line.trim();
				for(int i=1; i<4; i++){
					line = reader.readLine();
					switch(i){
					case 1:	
							String stocks[] = line.split("\\s+");
						
							for(int c=0; c<stocks.length; c++)
								auser.stockTrackerSet.add(stocks[c]);
							System.out.println("Done1");
							break;
							
					case 2:
							System.out.println(line);
							auser.cashbalance = Double.parseDouble(line.trim());
							System.out.println("Done2");
							break;
							
					case 3:
							String pstocks[] = line.split(",");
							for (int c=0; c<pstocks.length; c++){
								System.out.println(c);
								String qtys[] = pstocks[c].split(" ");
								auser.purchasedStock.put(qtys[0], Integer.parseInt(qtys[1]));
							}
							System.out.println("Done3");
							break;
					}
				}
				
				//adding to userDB
				userDB.put(uname, auser);
				//check for delimiter
				if((line = reader.readLine()).equals("/")){
					System.out.println("Done 4");
					continue;
				}else{
					System.out.println("File content missing....");
					return false;
				}
			
			}
			*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("User-> loadUserdb() method says--->"+e.getMessage());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("User-> loadUserdb() method says--->"+e.getMessage());
			return false;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}//loadUserdb method
	
	/**
	 * 
	 * @param username
	 * @param userattributes
	 * @return true on success else false
	 */
	public boolean writeToUserdb(String username, UserAttributes userattributes){
	
		return true;
	}

	/**
	 * 
	 */
	public void writeToXML(String username, UserAttributes ua){
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.parse("file.xml");
			
			NodeList rootList = doc.getElementsByTagName("User");
			org.w3c.dom.Node root = rootList.item(0);
			/*
			Element rootElement = doc.createElement("Users");
			doc.appendChild(rootElement);
			 */
			// a user elements
			Element user = doc.createElement("User");
			root.appendChild(user);
	 
			// set attribute name to user element
			Attr addname = doc.createAttribute("name");
			addname.setValue(username);
			user.setAttributeNode(addname);
	 	 
			// stock elements
			Element addstock = doc.createElement("stocks");
			//----------------------
			String stocks="";
			for (String s : ua.stockTrackerSet) {
			    stocks = s+" ";
			}
			
			addstock.appendChild(doc.createTextNode(stocks));
			user.appendChild(addstock);
	 
			// current balance elements
			Element addbal = doc.createElement("balance");
			addbal.appendChild(doc.createTextNode(String.valueOf(ua.cashbalance)));
			user.appendChild(addbal);
	 
			// purchasedstock elements
			Element addpurstock = doc.createElement("purchasedstock");
			//---------------------
			String pstocks = "";
			for (Map.Entry<String, Integer> entry : ua.purchasedStock.entrySet())
			{
			    pstocks = pstocks +entry.getKey() + " " + entry.getValue()+",";
			}
			
			addpurstock.appendChild(doc.createTextNode(pstocks));
			user.appendChild(addpurstock);
	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult("file.xml");
	 
			// Output to console for testing
	 
			transformer.transform(source, result);
	 
			System.out.println("File saved!");
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  } catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	
	/**
	 * Display the userdb
	 */
	public void displayUserdb(){
			
		for (Map.Entry<String, UserAttributes> entry : userDB.entrySet()){
			System.out.println("Key: "+entry.getKey());
			UserAttributes ua = new UserAttributes();
			ua = entry.getValue();
			System.out.println("Values:"+ua.cashbalance);
			System.out.println(ua.stockTrackerSet);
		}
	}
	
	public static void main(String args[]){
		User user = new User();
		User user1 = new User();
		System.out.println(user1.loadUserdb("file.xml"));
		user1.displayUserdb();
		System.out.println("----------------------------------");
		UserAttributes ua = new UserAttributes();
		ua.setCashbalance(1999.0);
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("GOOG", 2);
		ua.setPurchasedStock(m);
		
		HashSet s = new HashSet();
		s.add("GOOG");
		s.add("F");
		ua.setStockTrackerSet(s);
		user.writeToXML("Parminder", ua);
	}
}

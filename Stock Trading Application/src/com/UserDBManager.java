package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserDBManager {
	private Map<String, UserBean> userDB = null;
	/**
	 * load dictionary from USER file 
	 * @param filename
	 * @return true on success
	 */
	public Map<String, UserBean> loadFromUserDB(){
		String filename = "userfile.xml";
		userDB = new HashMap<String, UserBean>();
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
					
					UserBean auser = new UserBean();
					String uname = eElement.getAttribute("name");
					String password = eElement.getElementsByTagName("password").item(0).getTextContent();
					String avstocks = eElement.getElementsByTagName("stocks").item(0).getTextContent();
					String balance = eElement.getElementsByTagName("balance").item(0).getTextContent();
					String purstocks = eElement.getElementsByTagName("purchasedstock").item(0).getTextContent();
					HashSet<String> temp_stockTrackerSet = new HashSet<String>();
					
					//put password into user
					auser.setPassword(password);
					
					//put stocks into object of UserAttribute
					String stocks[] = avstocks.split("\\s+");
					
					for(int c=0; c<stocks.length; c++)
						temp_stockTrackerSet.add(stocks[c]);
					//set the stocks
					auser.setStockTrackerSet(temp_stockTrackerSet);
					System.out.println("Done1");
					
					//put cash balance
					auser.setCashbalance(Double.parseDouble(balance.trim()));
					System.out.println("Done2");
					
					//put purchased stock
					Map<String, Integer> temp_purchasedStock = new HashMap<String, Integer>();
					String pstocks[] = purstocks.split(",");
					for (int c=0; c<pstocks.length; c++){
						System.out.println(c);
						String qtys[] = pstocks[c].split(" ");
						temp_purchasedStock.put(qtys[0], Integer.parseInt(qtys[1]));
					}
					auser.setPurchasedStock(temp_purchasedStock);
					System.out.println("Done3");
					
					System.out.println("User Name : " + eElement.getAttribute("name"));
					System.out.println("Stocks : " + eElement.getElementsByTagName("stocks").item(0).getTextContent());
					System.out.println("Balance : " + eElement.getElementsByTagName("balance").item(0).getTextContent());
					System.out.println("Purchased Stocks : " + eElement.getElementsByTagName("purchasedstock").item(0).getTextContent());
					
					//load the value
					userDB.put(uname, auser);
					System.out.println("Puuuuttttttttttt");
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
				System.out.println("UserDBManager-> loadFromUserDB() method says--->"+e.getMessage());
				return userDB;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("UserDBManager-> loadFromUserDB() method says--->"+e.getMessage());
				return userDB;
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				System.out.println("UserDBManager-> loadFromUserDB() method says--->"+e.getMessage());
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				System.out.println("UserDBManager-> loadFromUserDB() method says--->"+e.getMessage());
			}
			return userDB;
		}//loadUserdb method
	
	/**
	 * 
	 * @param username
	 * @param ua
	 * @return
	 */
	public boolean updateUserDB(Map<String, UserBean> userDB){
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			//Document doc = docBuilder.parse("file.xml");
			Document doc = docBuilder.newDocument();
			
			//NEED TO RESOLVE THE PROBLEM WHEN NO CONTENT EXISTS IN FILE
			/*
			NodeList rootList = doc.getElementsByTagName("Users");
			System.out.println("rootlist"+rootList);
			org.w3c.dom.Node root = rootList.item(0);
			*/
			//System.out.println(root.hasAttributes());
				
			
			Element rootElement = doc.createElement("Users");
			doc.appendChild(rootElement);
			
			for (Map.Entry<String, UserBean> entry : userDB.entrySet()){
				// 	a user elements
				Element user = doc.createElement("User");
				rootElement.appendChild(user);
	 
				// 	set attribute name to user element
				Attr addname = doc.createAttribute("name");
				addname.setValue(entry.getKey());
				user.setAttributeNode(addname);
	 	 
				//add password element
				Element addpassword = doc.createElement("password");
				addpassword.appendChild(doc.createTextNode(entry.getValue().getPassword()));
				user.appendChild(addpassword);
			
				// stock elements
				Element addstock = doc.createElement("stocks");
				//----------------------
				String stocks="";
				for (String s : entry.getValue().getStockTrackerSet()) {
					stocks = stocks + s + " ";
				}
			
				addstock.appendChild(doc.createTextNode(stocks));
				user.appendChild(addstock);
	 
				// current balance elements
				Element addbal = doc.createElement("balance");
				addbal.appendChild(doc.createTextNode(String.valueOf(entry.getValue().getCashbalance())));
				user.appendChild(addbal);
	 
				// purchasedstock elements
				Element addpurstock = doc.createElement("purchasedstock");
				//---------------------
				String pstocks = "";
				
				for (Map.Entry<String, Integer> entry1 : entry.getValue().getPurchasedStock().entrySet())
				{
					pstocks = pstocks +entry1.getKey() + " " + entry1.getValue()+",";
				}
			
				addpurstock.appendChild(doc.createTextNode(pstocks));
				user.appendChild(addpurstock);
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult("userfile.xml");
	 
			// Output to console for testing
	 
			transformer.transform(source, result);
	 
			System.out.println("File saved!");
			return true;
	 
		  } catch (ParserConfigurationException pce) {
			System.out.println("UserDBManager-> updateUserDB method says-->"+pce.getMessage());
		  } catch (TransformerException tfe) {
			  System.out.println("UserDBManager->  updateUserDB method says-->"+tfe.getMessage());
		  } 
		return false;
	}
	
}
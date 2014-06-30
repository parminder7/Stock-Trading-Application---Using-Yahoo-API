package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
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

public class StockDBManager {
	
	HashMap<String, StockBean> stockDB;
	
	/**
	 * This loadFromStockDB method loads the stock database to the dictionary
	 * @return	HashMap <Stockname, (no of available stocks, price)>
	 */
	public Map<String, StockBean> loadFromStockDB(){
		String filename = "stockfile.xml";
		stockDB = new HashMap<String, StockBean>();
		try {
			
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("Stock");
		
			System.out.println(nList.getLength());
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				org.w3c.dom.Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					//add to stockDB
					
					StockBean stock = new StockBean();
					String sname = eElement.getAttribute("name");
					int stocks = Integer.parseInt(eElement.getElementsByTagName("totalStocks").item(0).getTextContent());
					float price = Float.parseFloat(eElement.getElementsByTagName("currentPrice").item(0).getTextContent());
					
					
					//put totalstocks into stock
					stock.setNoOfStocks(stocks);
					
					//put price into stock
					stock.setStockprice(price);
					
					System.out.println("Stock Name : " + eElement.getAttribute("name"));
					System.out.println("No of Stocks : " + eElement.getElementsByTagName("totalStocks").item(0).getTextContent());
					System.out.println("Price : " + eElement.getElementsByTagName("currentPrice").item(0).getTextContent());
					
					
					//load the value
					stockDB.put(sname, stock);
					System.out.println("Put new stock");
					}
				}
		} catch (FileNotFoundException e) {
			System.out.println("StockDBManager-> loadFromStockDB() method says--->"+e.getMessage());
		} catch (IOException e) {
			System.out.println("StockDBManager-> loadFromStockDB() method says--->"+e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("StockDBManager-> loadFromStockDB() method says--->"+e.getMessage());
		} catch (SAXException e) {
			System.out.println("StockDBManager-> loadFromStockDB() method says--->"+e.getMessage());
		}
		return stockDB;
	}//loadStockdb method

	/**
	 * This updateStockDB method updates the stock database with given hash map
	 * @param stockDB
	 * @return	true on success else false
	 */
	public boolean updateStockDB(Map<String, StockBean> stockDB){
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("Stocks");
			doc.appendChild(rootElement);
			
			for (Map.Entry<String, StockBean> entry : stockDB.entrySet()){
				// 	a stock elements
				Element stock = doc.createElement("Stock");
				rootElement.appendChild(stock);
	 
				// 	set attribute name to stock element
				Attr addname = doc.createAttribute("name");
				addname.setValue(entry.getKey());
				stock.setAttributeNode(addname);
	 	 
				//add totalStocks element
				Element addstocks = doc.createElement("totalStocks");
				addstocks.appendChild(doc.createTextNode(String.valueOf(entry.getValue().getNoOfStocks())));
				stock.appendChild(addstocks);
			
				//add currentPrice element
				Element addprice = doc.createElement("currentPrice");
				addprice.appendChild(doc.createTextNode(String.valueOf(entry.getValue().getStockprice())));
				stock.appendChild(addprice);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult("stockfile.xml");
	 
			// Output to console for testing
	 
			transformer.transform(source, result);
	 
			System.out.println("Stock File saved!");
			return true;
	 
		  } catch (ParserConfigurationException pce) {
			System.out.println("StockDBManager-> updateStockDB method says-->"+pce.getMessage());
		  } catch (TransformerException tfe) {
			  System.out.println("StockDBManager->  updateStockDB method says-->"+tfe.getMessage());
		  } 
		return false;
	}
	
}

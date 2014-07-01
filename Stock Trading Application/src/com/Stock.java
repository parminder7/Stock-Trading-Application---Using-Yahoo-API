package com;

import java.util.HashMap;
import java.util.Map;

import YahooAPI.yahooStock;

public class Stock {
	private Map<String, StockBean> stockDB = new HashMap<String, StockBean>();
	StockDBManager manager = new StockDBManager();
	yahooStock fin;
	StockBean stock = new StockBean();
	
	/**
	 * constructor
	 */
	public Stock(){
		stockDB = manager.loadFromStockDB();
	}
	
	/**
	 * This addStockToStockDB method skips to add the stock price if its available to the server
	 * @param stockname
	 * @return true 
	 */
	public boolean addStockToStockDB(String stockname){	
		if (!(stockDB.containsKey(stockname))){
			stock.setNoOfStocks(1000);
		}
		
		return true;	
	}
	
	/**
	 * This getStockQuote method returns the stock price currently available to the server
	 * else fetch the record from the yahoo finance api and add to the stock database
	 * @param stockname
	 * @return	error string or amount
	 */
	public String getStockQuote(String stockname){
		System.out.println("Stock name requested:"+stockname);
		
		if (stockDB.containsKey(stockname)){
			System.out.println("It exists");
			System.out.println(stockDB.get(stockname).getStockprice());
		}
		else{
			fin = new yahooStock();
			float stockprice = fin.getQuote(stockname);
			if (stockprice < 0){
				return("ENOTAVAILABLE");
			}else if (stockprice == 0){
				return("ENOTAPPLICABLE");
			}
			
			stock.setStockprice(stockprice);
			stockDB.put(stockname, stock);
			addStockToStockDB(stockname);
		}
		return String.valueOf(stockDB.get(stockname).getStockprice());
	}
	
	/**
	 * This getAvailableShares method returns the shares available for stock 
	 * @param stockname
	 * @return	no of shares
	 */
	public int getAvailableShares(String stockname){
		return stockDB.get(stockname).getNoOfStocks();
	}
	
	/**
	 * This generateBill method shows the bill amount of requested stock and shares
	 * @param stockname
	 * @param number
	 * @return	price
	 */
	public double generateBill(String stockname, int number){
		double amount = stockDB.get(stockname).getStockprice() * number;
		return amount;
	}
	
	/**
	 * This buyStock method subtracts the shares from the shares of corresponding stock name
	 * @param stockname
	 * @param number
	 * @return	error string or amount
	 */
	public String buyStock(String stockname, int number){
		if (!stockDB.containsKey(stockname)){
			return "ENOTEXIST";
		}
		
		int tot = stockDB.get(stockname).getNoOfStocks();
		if ((tot <= 0) || ((tot-number) < 0)){
			return "ENOTAVAILABLE";
		}
		
		stockDB.get(stockname).setNoOfStocks(tot-number);
		double amount = stockDB.get(stockname).getStockprice() * number;
		return String.valueOf(amount);
	}
	
	/**
	 * This sellStock method adds the shares to the corresponding stock name
	 * @param stockname
	 * @param number
	 * @return error string or amount of shares
	 */
	public String sellStock(String stockname, int number){
		if (!stockDB.containsKey(stockname)){
			return "ENOTEXIST";
		}
		
		int tot = stockDB.get(stockname).getNoOfStocks();
				
		stockDB.get(stockname).setNoOfStocks(tot+number);
		double amount = stockDB.get(stockname).getStockprice() * number;
		return String.valueOf(amount);
	}
	
	/**
	 * This getStockList method return the list of stock and their corresponding values
	 * @return	hash map
	 */
	public Map<String, StockBean> getStockList(){
		return stockDB;
	}
	
	/**
	 * This saveStockDB method updates the stock list to the database
	 * @return	true on success else false
	 */
	public boolean saveStockDB(){
		return manager.updateStockDB(stockDB); 
	}
	
	/**
	 * for testing
	 * @param args - not used
	 */
	public static void main(String args[]){
		Stock sd = new Stock();
		sd.addStockToStockDB("YHOO");
		System.out.println(sd.getStockQuote("YHOO"));
		sd.addStockToStockDB("F");
		System.out.println(sd.getStockQuote("F"));
		System.out.println("Result: "+sd.saveStockDB());
	}
}

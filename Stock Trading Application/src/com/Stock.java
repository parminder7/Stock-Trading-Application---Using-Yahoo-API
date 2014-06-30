package com;

import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;

import YahooAPI.yahooStock;

public class Stock {
	private Map<String, StockBean> stockDB = new HashMap<String, StockBean>();
	StockDBManager manager = new StockDBManager();
	yahooStock fin;
	StockBean stock = new StockBean();
	
	public Stock(){
		stockDB = manager.loadFromStockDB();
	}
	
	public boolean addStockToStockDB(String stockname){	
		if (!(stockDB.containsKey(stockname))){
			stock.setNoOfStocks(1000);
			fin = new yahooStock();
			stock.setStockprice(fin.getQuote(stockname));
			stockDB.put(stockname, stock);
			System.out.println("-----------"+stockname+stockDB.containsKey(stockname));
			System.out.println("Entered");
		}
		
		return true;	
	}
	
	public float getStockQuote(String stockname){
		System.out.println("Stock name requested:"+stockname);
		System.out.println("-----------"+stockDB.containsKey(stockname));
		System.out.println(stockDB.get(stockname).getStockprice());
		if (stockDB.containsKey(stockname)){
			System.out.println("It exists");
			System.out.println(stockDB.get(stockname).getStockprice());
		}
		return stockDB.get(stockname).getStockprice();
	}
	
	public int getAvailableShares(String stockname){
		return stockDB.get(stockname).getNoOfStocks();
	}
	
	public double generateBill(String stockname, int number){
		double amount = stockDB.get(stockname).getStockprice() * number;
		return amount;
	}
	
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
	
	public Map<String, StockBean> getStockList(){
		return stockDB;
	}
	
	public boolean saveStockDB(){
		return manager.updateStockDB(stockDB); 
	}
	
	public static void main(String args[]){
		Stock sd = new Stock();
		sd.addStockToStockDB("YHOO");
		System.out.println(sd.getStockQuote("YHOO"));
		sd.addStockToStockDB("F");
		System.out.println(sd.getStockQuote("F"));
		System.out.println("Result: "+sd.saveStockDB());
	}
}

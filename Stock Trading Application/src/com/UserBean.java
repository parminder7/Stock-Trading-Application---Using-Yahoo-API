package com;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserBean {
	private String password;
	private HashSet<String> stockTrackerSet = new HashSet<String>();
	private double cashbalance;
	private Map<String, Integer> purchasedStock = new HashMap<String, Integer>();
	
	public HashSet<String> getStockTrackerSet() {
		return stockTrackerSet;
	}

	public void setStockTrackerSet(HashSet<String> stockTrackerSet) {
		this.stockTrackerSet = stockTrackerSet;
	}

	public double getCashbalance() {
		return cashbalance;
	}

	public void setCashbalance(double cashbalance) {
		this.cashbalance = cashbalance;
	}

	public Map<String, Integer> getPurchasedStock() {
		return purchasedStock;
	}

	public void setPurchasedStock(Map<String, Integer> purchasedStock) {
		this.purchasedStock = purchasedStock;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void addStocks(String stock){
		stockTrackerSet.add(stock);
	}
	
	public void addPurchasedstock(String stock, int num){
		purchasedStock.put(stock, num);
	}
}

package com;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserAttributes {
	protected HashSet<String> stockTrackerSet = new HashSet<String>();
	protected double cashbalance;
	protected Map<String, Integer> purchasedStock = new HashMap<String, Integer>();
	
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
}

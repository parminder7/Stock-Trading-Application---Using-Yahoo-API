package com;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class User {
	
	private Map<String, UserAttributes> userDB;
	PassHash ph = new PassHash();
	UserDBManager manager = new UserDBManager();
	
	//constructor
	public User(){
		UserDBManager mgr = new UserDBManager();
		userDB = mgr.loadFromUserDB();
	}
	
		
	public UserAttributes addORgetUser(String username, String password, int flag){
		// if user already exists in database get the object
		UserAttributes ua = null;
		if (isUserExist(username)){
			ua = userDB.get(username);
			System.out.println("Exixt");
			if (checkPassword(ua, password)){
				System.out.println("pass");
				return ua;
			}
			else{
				//incorrect password
				return null;
			}
		}else{
			flag = 1;
			ua = new UserAttributes();
			//Password Security; entering hashed value in database
			System.out.println("hasddd");
			String hashpass = ph.getHashedPassword(password);
			System.out.println(hashpass);
			ua.setPassword(hashpass);
			ua.setCashbalance(1000.0);
			//Pass 'D' as default value or say initial value
			ua.addStocks("D");
			ua.addPurchasedstock("D", 0);
			manager.updateUserDB(username, ua);
			return ua;
		}
	}
	
	private boolean isUserExist(String username){
		return (userDB.containsKey(username));
	}

	private boolean checkPassword(UserAttributes userdetails, String password){
		String storedHash = userdetails.getPassword();
		
		return (ph.validatePassword(password, storedHash));
	}
	
	public static void main(String args[]){

		UserDBManager user1 = new UserDBManager();
		//System.out.println(user1.loadFromUserDB());		
		System.out.println("----------------------------------");
		UserAttributes ua = new UserAttributes();
		ua.setCashbalance(1999.0);
		ua.setPassword("xyz");
		//Map<String, Integer> m = new HashMap<String, Integer>();
		//m.put("GOOG", 2);
		ua.addPurchasedstock("CME", 2);
		ua.addPurchasedstock("JO", 2);
		//ua.setPurchasedStock(m);
		
		//HashSet<String> s = new HashSet<String>();
		//s.add("GOOG");
		//s.add("F");
		//ua.setStockTrackerSet(s);
		ua.addStocks("CME");
		ua.addStocks("F");
		//user1.updateUserDB("Gurman", ua);
		
		Map<String, UserAttributes> xx = user1.loadFromUserDB();
		
		System.out.println(xx.containsKey("kookoo"));
		
	}
}

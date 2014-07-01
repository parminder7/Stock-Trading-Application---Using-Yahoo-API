package com;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class User {
	
	private Map<String, UserBean> userDB;
	PassHash ph = new PassHash();
	Stock s = new Stock();
	UserDBManager manager = new UserDBManager();
	
	//constructor
	public User(){
		manager = new UserDBManager();
		userDB = manager.loadFromUserDB();
	}
	
	/**
	 * This addORgetUser method looks for the given user name (or client) record
	 * if it is already in the database, then it returns the object with recorded value
	 * else new user record get added
	 * @param username
	 * @param password
	 * @param flag
	 * @return	UserAttributes object
	 */
	public UserBean addORgetUser(String username, String password, int flag){
		// if user already exists in database get the object
		UserBean ua = null;
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
			ua = new UserBean();
			//Password Security; entering hashed value in database
			System.out.println("hashed");
			String hashpass = ph.getHashedPassword(password);
			System.out.println(hashpass);
			ua.setPassword(hashpass);
			ua.setCashbalance(1000.0);
			//Pass 'D' as default value or say initial value
			ua.addStocks("D");
			ua.addPurchasedstock("D", 0);
			userDB.put(username, ua);
	//		manager.updateUserDB(username, ua);
			return ua;
		}
	}
	
	/**
	 * This addStockToUserRecord method adds requested stocks into user's stock tracker list
	 * and respond with stock value
	 * @param username
	 * @param userrecord
	 * @param stock
	 * @return	string with stock price for requested stock
	 */
	public String addStockToUserRecord(String username, UserBean userrecord, String stock){
		System.out.println(userrecord.getStockTrackerSet());
		userrecord.addStocks(stock);
		System.out.println(userrecord.getStockTrackerSet());
		
		//if record already exists, update that
		/*if (userDB.containsKey(username)){
			userDB.get(username).addStocks(stock);
		}*/
		
		//also update the stock list
		//s.addStockToStockDB(stock);
		String result = s.getStockQuote(stock);
		
		if (result.equals("ENOTAVAILABLE")){
			return "Not available";
		}else if (result.equals("ENOTAPPLICABLE")){
			return "Not applicable";
		}
		
		userDB.put(username, userrecord);
		System.out.println("User record updated!");
		return result;
	}
	
	/**
	 * This getUserStockTrackerList method returns the list of stocks the user is maintaining
	 * @param username
	 * @return	stock list
	 */
	public HashSet<String> getUserStockTrackerList(String username){
		return userDB.get(username).getStockTrackerSet();
	}
	
	/**
	 * This getUserBalance gives the available balance of user
	 * @param username
	 * @return	balance in double
	 */
	public double getUserBalance(String username){
		return userDB.get(username).getCashbalance();
	}
	
	/**
	 * This getUserPurchasedStockList gives the list of purchased stock by a user
	 * @param username
	 * @return	HashMap <Stock, # of stocks>
	 */
	public HashMap<String, Integer> getUserPurchasedStockList(String username){
		return (HashMap<String, Integer>) userDB.get(username).getPurchasedStock();
	}
	
	/**
	 * This userBuyShares method takes users request related to purchase share
	 * and returns the deducted amount or some error response
	 * @param user
	 * @param stockname
	 * @param number
	 * @return amount or type of errors
	 */
	public String userBuyShares(String username, String stockname, int number){
		Stock s = new Stock();
		double bill = s.generateBill(stockname, number);
		double total = userDB.get(username).getCashbalance();
		
		if (total < bill){
			return "You don't have sufficient amount";
		}
		
		String answer = s.buyStock(stockname, number);
		
		if (answer.equals("ENOTEXIST")){
			return "Stock doesn't exist";
		}else if (answer.equals("ENOTAVAILABLE")){
			return "Server don't have sufficient shares for you";
		}
		
		double val = Double.parseDouble(answer);
		DecimalFormat f = new DecimalFormat("##.00");
	    answer = f.format(val);
	    
		userDB.get(username).setCashbalance(total - Double.parseDouble(answer));
		Map<String, Integer> ps = new HashMap<String, Integer>();
		
		try{
			int num = userDB.get(username).getPurchasedStock().get(stockname);
			ps.put(stockname, num+number);
		}
		catch (NullPointerException ex){
			ps.put(stockname, number);
		}
		userDB.get(username).setPurchasedStock(ps);
		
		return answer+" amount has been deducted from your account";
	}
	
	/**
	 * This userSellShares method sells the requested shares and added to the server list
	 * amount get transferred to user account
	 * @param username
	 * @param stockname
	 * @param number
	 * @return	error string or added amount 
	 */
	public String userSellShares(String username, String stockname, int number){
		Stock s = new Stock();
		
		double total = userDB.get(username).getCashbalance();
		System.out.println("Total shares:"+userDB.get(username).getPurchasedStock().get(stockname));
		int totalshare = userDB.get(username).getPurchasedStock().get(stockname);
		
		
		if (totalshare < number){
			return "You have less shares than total selling units";
		}
		
		String answer = s.buyStock(stockname, number);
		
		if (answer.equals("ENOTEXIST")){
			return "Stock doesn't exist";
		}
		
		double val = Double.parseDouble(answer);
		DecimalFormat f = new DecimalFormat("##.00");
	    answer = f.format(val);
	    
		userDB.get(username).setCashbalance(total + Double.parseDouble(answer));
		Map<String, Integer> ps = new HashMap<String, Integer>();

		int num = userDB.get(username).getPurchasedStock().get(stockname);
		ps.put(stockname, num-number);

		userDB.get(username).setPurchasedStock(ps);
		return answer+" amount has been added to your account";
	}
	
	/**
	 * This isUserExists method checks whether user record already exists in database
	 * @param username
	 * @return	true on success else false
	 */
	private boolean isUserExist(String username){
		return (userDB.containsKey(username));
	}

	/**
	 * This checkPassword method validate user's given credentials 
	 * @param userdetails
	 * @param password
	 * @return	true on success else false
	 */
	private boolean checkPassword(UserBean userdetails, String password){
		String storedHash = userdetails.getPassword();
		
		return (ph.validatePassword(password, storedHash));
	}
	
	/**
	 * This saveUserDB method saves the user record on exit
	 * @return	return true on success else false
	 */
	public boolean saveUserDB(){
		
		boolean res = manager.updateUserDB(userDB);
		
		return res;
	}
		
}

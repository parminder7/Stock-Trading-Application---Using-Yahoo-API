package com;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class User {
	
	private Map<String, UserBean> userDB;
	PassHash ph = new PassHash();
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
	 * @param username
	 * @param userrecord
	 * @param stock
	 * @return	true on success else false
	 */
	public boolean addStockToUserRecord(String username, UserBean userrecord, String stock){
		System.out.println(userrecord.getStockTrackerSet());
		userrecord.addStocks(stock);
		System.out.println(userrecord.getStockTrackerSet());
		
		//if record already exists, update that
		/*if (userDB.containsKey(username)){
			userDB.get(username).addStocks(stock);
		}*/
		userDB.put(username, userrecord);
		System.out.println("User record updated!");
		return true;
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

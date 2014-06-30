package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;

import YahooAPI.yahooStock;

import com.User;
import com.UserBean;

/*
 * This 'AThread' class will be used by MyServer to run a thread
 * corresponding to every client request
 * */
public class AThread implements Runnable{
	
	private Socket clientSocket = null;
	private BufferedReader inputStr = null;
	private PrintStream outputStr = null;
	private AThread[] threads;
	int maxCount;
	
	public AThread(Socket clientSocket, AThread[] threads){
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxCount = threads.length;
	}
	
	@Override
	public void run() {
		/*
		 * Create input/output stream to send/receive message from a client
		 * */
		
		try {
			
			inputStr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outputStr = new PrintStream(clientSocket.getOutputStream());
			outputStr.println("USERNAME : \n"+"END");
			String username = inputStr.readLine().trim();
			outputStr.println("PASSWORD : \n"+"END");
			String password = inputStr.readLine().trim();
			
			/*Create User class with bool authenticateUser() method*/
			int oldORnew = 0;	//0 for old 1 for new
			User user = new User();
			UserBean userrecord = user.addORgetUser(username, password, oldORnew);
			
			if( userrecord == null ){
				outputStr.println("Invaild username/password\n"+"END");
				return;
			} 
			if (oldORnew == 1){
				outputStr.println("Hello "+username+", You became our member"+" !\n");
			}else{
				outputStr.println("Hello "+username+", Welcome back"+" !\n");
			}
			
			int flag = 0;
			
			do{
				outputStr.println("Available commands:-\n"
									+"QUERY <tickername>\t to query for stock price\n"
									+"BUY <stockname> <no. of shares>\t to purchase the shares\n"
									+"SELL <stockname> <no. of shares>\t to sell your stock\n"
									+"GETINFO\t to retrive your account information\n"
									+"QUIT\n"
									+"END");
				
				String request = inputStr.readLine();
				String[] requestQ = request.split("\\s+");
			
				switch(requestQ[0].trim()){
				
				case "QUERY":	
				case "query":
					//Create stockQuery() method in User class
					if(requestQ.length != 2){
						outputStr.println("Incomplete QUERY command\n");
					}
					else{
						//double value = stockQuery(requestQ[1]);
						boolean response = false;
						yahooStock finapi = new yahooStock();
						float value = finapi.getQuote(requestQ[1]);
						
						//Tell user if value is not available
						if (value < 0){
							response = user.addStockToUserRecord(username, userrecord, requestQ[1]);
							outputStr.println("Stock: "+requestQ[1]+"\tStock Value: "+"Not available"+"\n");
						}else if (value == 0){
							outputStr.println("Stock: "+requestQ[1]+"\tStock Value: "+"Sorry! Not applicable"+"\n");
						}else{
							response = user.addStockToUserRecord(username, userrecord, requestQ[1]);
							outputStr.println("Stock: "+requestQ[1]+"\tStock Value: "+value+"\n");
						}
						
						if(response){
							System.out.println(username+" stock tracker list updated!");
						}
					}
					break;
					
				case "BUY":
				case "buy":
					outputStr.println("Service not available for BUY\n");
					break;
				
				case "SELL":
				case "sell":
					outputStr.println("Service not available for SELL\n");
					break;
				
				case "GETINFO":
				case "getinfo":
					outputStr.println("****************************************************************");
					outputStr.println("\t User -> "+username+"\t\t Balance -> "+user.getUserBalance(username));
					
					HashSet<String> stocks = user.getUserStockTrackerList(username);
					yahooStock finapi = new yahooStock();
					outputStr.print("\t Stocks tracking -> ");
					for (String str : stocks){
						if (str.equals("D"))
							continue;
						outputStr.print(str+" : "+finapi.getQuote(str)+"  |");
					}
					
					outputStr.println("\n\t****************************************************************");
					break;
				
				case "QUIT":
				case "quit":
					boolean res = user.saveUserDB();
					System.out.println(res+": "+username+" data has successfully stored.");
					outputStr.println("Thankyou for using this application.\n"+"OFF");
					flag = 1;
					break;
				
				default:
					outputStr.println("Invalid command...\n");
					break;
				}
			}while(flag != 1);
			outputStr.print("exit");
			
			int maxCount = this.maxCount;
			AThread[] threads = this.threads;
			
			for (int i=0; i<maxCount; i++){
				if (threads[i] == this){
					threads[i] = null;
				}
			}
			/*
			 * Close the streams
			 * */
			inputStr.close();
			outputStr.close();
			clientSocket.close();
			System.out.println("closeddddd");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

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
			if( !username.equals("user") && !password.equals("pass") ){
				System.out.println("Invaild username/password\n"+"END");
				return;
			} else{
				outputStr.println("Hello "+username+" !\n");
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
						outputStr.println("Service not available for QUERY\n"+ "Try again...");
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
					outputStr.println("Service not available for GETINFO\n");
					break;
				
				case "QUIT":
				case "quit":
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

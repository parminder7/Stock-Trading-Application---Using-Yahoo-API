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
	
	public AThread(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		/*
		 * Create input/output stream to send/receive message from a client
		 * */
		
		try {
			
			inputStr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outputStr = new PrintStream(clientSocket.getOutputStream());
			outputStr.println("USERNAME : ");
			String username = inputStr.readLine().trim();
			outputStr.println("PASSWORD : ");
			String password = inputStr.readLine().trim();
			
			/*Create User class with bool authenticateUser() method {Dictionary}*/
			if( !username.equals("user") && !password.equals("pass") ){
				System.out.println("Invaild username/password");
				return;
			} else{
				outputStr.println("Hello "+username+" !");
			}
			
			int flag = 0;
			
			do{
				outputStr.println("Available commands:"
									+"QUERY <tickername>"
									+"BUY <stockname> <no. of shares>"
									+"SELL <stockname> <no. of shares>"
									+"GETINFO");
				
				String request = inputStr.readLine();
				String[] requestQ = request.split("\\s+");
			
				switch(requestQ[0].trim()){
				case "QUERY":	
				case "query":
					//Create stockQuery() method in User class
					if(requestQ.length != 2){
						outputStr.println("Incomplete QUERY command");
					}
					else{
						//double value = stockQuery(requestQ[1]);
						outputStr.println("Service not available for QUERY");
					}
					break;
					
				case "BUY":
				case "buy":
					outputStr.println("Service not available for BUY");
					break;
				case "SELL":
				case "sell":
					outputStr.println("Service not available for SELL");
					break;
				case "GETINFO":
				case "getinfo":
					outputStr.println("Service not available for GETINFO");
					break;
				case "QUIT":
				case "quit":
					outputStr.println("Thankyou for using this application.");
					break;
				default:
					outputStr.println("Oops! Invalid command");
					flag = 1;
					break;
				}
			}while(flag != 1);
			outputStr.print("exit");
			
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

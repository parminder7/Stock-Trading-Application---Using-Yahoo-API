package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
	//Client socket
	private static Socket clientSocket = null;
	private static PrintStream output = null;
	private static BufferedReader in = null;
	private static BufferedReader stdIn = null;
	
	public static void main(String args[]){
		String host = "localhost";
		int portNum = 1234;
		
		if(args.length < 2){
			System.out.println("Usage: java Client <host> <portNum>");
			System.out.println("Using default host: "+host+" port: "+portNum+"\n");
		}else{
			host = args[0];
			portNum = Integer.valueOf(args[1]);
		}
		
		/*
		 * Open socket at given/default host and port number
		 * */
		try {
			clientSocket = new Socket(host, portNum);
			output = new PrintStream(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer = "";
			String fromUser;
 
			//Read from socket and write back the response to server. 
			while (true) {
				String line = "";
				fromServer = "";
				//Reading from server
				while(true){
					if((line = in.readLine()).equals("END")){
						break;
					}
					//To sign off the session
					else if(line.equals("OFF")){
						return;
					}
					
					fromServer = fromServer + line +"\n";
				}
				
				System.out.println("Server - " + fromServer);
				
				//Reading client input and forward it to server using print stream
				
				System.out.print(">>>> ");
				fromUser = stdIn.readLine();
				output.println(fromUser);
				System.out.println();
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Client ->"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client ->"+e.getMessage());
		}
	}
}

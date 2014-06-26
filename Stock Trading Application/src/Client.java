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
		int portNum = 3322;
		
		if(args.length < 2){
			System.out.println("Usage: java Client <host> <portNum>");
			System.out.println("Using default host: "+host+" port: "+portNum);
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
			String fromServer;
			String fromUser;
 
			//Read from socket and write back the response to server. 
			while (true) {
				fromServer = in.readLine();
				System.out.println("Server - " + fromServer);
				if (fromServer.equals("exit")||fromServer.equals("Invaild username/password"))
					break;
 
				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("Client - " + fromUser);
					output.println(fromUser);
				}
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

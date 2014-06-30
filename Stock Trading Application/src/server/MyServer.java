package server;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class MyServer {
	//Server Socket
	private static ServerSocket serversocket = null;
	
	//Client Socket
	private static Socket clientSocket = null;
	
	//'maxClients' is the number of connections, server can accept
	private static final int maxClient = 5;
	
	//A thread pool
	static AThread[] threads = new AThread[maxClient];
	
	public static void main(String args[]){
		int portNum = 1234;
		if (args.length < 1){
			System.out.println("Usage: java MyServer <portNumber>\n Using default port number"+ portNum);
		}else{
			portNum = Integer.valueOf(args[0]);
		}
		
		/*
		 * Open welcome socket at given or default port number
		 * */
		try{
			serversocket = new ServerSocket(portNum);
			StockDemon demon = new StockDemon();
			Thread thread = new Thread(demon);
			thread.run();
		}catch (SocketException ex){
			System.out.println("Server Socket Exception ->"+ex.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("My Server IOException ->"+e.getMessage());
		}
		
		/*
		 * Generate a thread for each client request
		 * */
		
		while (true){
			try {
				clientSocket = serversocket.accept();
				System.out.println("A new Connection accepted");
				int i;
				for (i=0; i < maxClient; i++){
					//servicethread.execute(new AThread(clientSocket));
					if(threads[i] == null){
						new Thread((threads[i] = new AThread(clientSocket, threads))).start();
						break;
					}
				}
				
				if (i == maxClient){
					PrintStream out = new PrintStream(clientSocket.getOutputStream());
					out.println("Server is too busy. Try Again.");
					out.close();
					clientSocket.close();
				}
				System.out.println("Listening....");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

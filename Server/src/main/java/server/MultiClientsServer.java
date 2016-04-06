package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import database.DatabaseManager;
/**
 * Server
 *
 */
public class MultiClientsServer {

	private final int port = 10008;

	ServerSocket myServerSocket;
	private static boolean serverOn = true;

	private static DatabaseManager dbManager;

	//Using a vector because it's synchronised
	private static Vector<ClientServiceThread> clientsThreadList = new Vector<ClientServiceThread>();

	public MultiClientsServer() 
	{ 
		try 
		{ 
			myServerSocket = new ServerSocket(port); 
		} 
		catch(IOException ioe) 
		{ 
			System.out.println("Could not create server socket on port 10008. Quitting."); 
			System.exit(-1); 
		} 

		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("It is now : " + formatter.format(now.getTime()));

		setDbManager(new DatabaseManager());

		// Successfully created Server Socket. Now wait for connections. 
		while(serverOn) 
		{                        
			try 
			{ 
				// Accept incoming connections. 
				Socket clientSocket = myServerSocket.accept(); 

				// accept() will block until a client connects to the server. 
				// If execution reaches this point, then it means that a client 
				// socket has been accepted. 

				// For each client, we will start a service thread to 
				// service the client requests. This is to demonstrate a 
				// Multi-Threaded server. Starting a thread also lets our 
				// MultiThreadedSocketServer accept multiple connections simultaneously. 

				// Start a Service thread 

				ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
				cliThread.start();
			} 
			catch(IOException ioe) 
			{ 
				System.out.println("Exception encountered on accept. Ignoring. Stack Trace :"); 
				ioe.printStackTrace(); 
			} 
		}
	} 

	public void stopServer(){
		try 
		{ 
			myServerSocket.close();
			serverOn = false;
			System.out.println("Server Stopped"); 
		} 
		catch(Exception ioe) 
		{ 
			System.out.println("Problem stopping server socket"); 
			System.exit(-1); 
		} 
	}

	public static void main (String[] args) 
	{ 
		new MultiClientsServer();        
	} 

	public static void findRecipient(String username, String message, String senderUsername){
		ClientServiceThread recipient = null;
		ClientServiceThread sender = null;
		
		Iterator<ClientServiceThread> itr = clientsThreadList.iterator();

		//use hasNext() and next() methods of Iterator to iterate through the elements
		while(itr.hasNext()){
			ClientServiceThread tempClient = itr.next();
			if(tempClient.getClientName().equals(username)){
				recipient = tempClient;
			}
			if(tempClient.getClientName().equals(senderUsername)){
				sender = tempClient;
			}
		}

		if(recipient == null){
			sender.sendMessage("User not found");
			System.out.println("User not found");
		}else if(recipient.getClientName().equals(senderUsername)){
			sender.sendMessage("You can't send a message to yourself you fool");
			System.out.println("This user ("+senderUsername+") is stupid");
		}else{
			recipient.sendMessageTo(message, senderUsername);
		} 
	}

	public static void RemoveFromList(String clientName) {
		Iterator<ClientServiceThread> itr = clientsThreadList.iterator();
		while(itr.hasNext()){
			ClientServiceThread tempRecipient = itr.next();
			if(tempRecipient.getClientName().equals(clientName)){
				itr.remove();
			}
		}
	}

	public static void addToList(ClientServiceThread cliThread) {
		clientsThreadList.add(cliThread);
	}
	
	public static boolean isUserConnected(String username){
		
		Iterator<ClientServiceThread> itr = clientsThreadList.iterator();
		boolean isConnected = false;
		//use hasNext() and next() methods of Iterator to iterate through the elements
		while(itr.hasNext()){
			ClientServiceThread tempClient = itr.next();
			if(tempClient.getClientName().equals(username)){
				isConnected=true;
			}
		}
		return isConnected;
	}
	
	public static boolean isServerOn(){
		return serverOn;
	}

	public static Vector<String> getListOfClients() {

		Vector<String> clientsList = new Vector<String>();

		Iterator<ClientServiceThread> itr = clientsThreadList.iterator();

		//use hasNext() and next() methods of Iterator to iterate through the elements
		while(itr.hasNext()){
			ClientServiceThread tempClientService = itr.next();
			clientsList.add(tempClientService.getClientName());
		}

		return clientsList;
	}

	public static DatabaseManager getDbManager() {
		return dbManager;
	}

	public static void setDbManager(DatabaseManager dbManager) {
		MultiClientsServer.dbManager = dbManager;
	}
}
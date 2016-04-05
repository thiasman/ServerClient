package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import message.Message;
import message.AdminMessage;
import message.BasicMessage;
import message.LogonMessage;
import message.MessageUsersList;

/**
 * Connect to the server from a computer
 *
 */
public class BasicClient {

	private SimpleUser user;
	private final String serverHostname = new String ("127.0.0.1");
	private final int serverPort = 10008;
	
	Socket echoSocket;
	ObjectOutputStream outputStream =  null;
	ObjectInputStream inputStream =  null;
	
	private ThreadListener clientThreadListener = null;
	
	private String storedPassword;
	private boolean connectedToServer = false;
	
	public BasicClient(String name, String pwd) {
		System.out.println ("Attemping to connect to host " + serverHostname + " on port "+ serverPort + " .");
		
		setUser(new SimpleUser(name));
		
		storedPassword = pwd;
		
		if(initializeBuffer()){
			connectedToServer = true;
			connectToServer();
		}
		else{
			connectedToServer = false;
			notConnectedToServer();
		}
	}

	private void notConnectedToServer() {
		String userInput = null;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println ("Press C to to try to connect to the server");
		
		try {
			while ((userInput = stdIn.readLine()) != null)
			{
				switch(userInput){
				case "C":
					if(initializeBuffer()){
						connectedToServer = true;
						connectToServer();
					}
					else{
						connectedToServer = false;
						notConnectedToServer();
					}
					break;
				}
			}
		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void connectToServer() {
		logon(storedPassword);
		
		clientThreadListener = new ThreadListener();
		clientThreadListener.start(); 
		
		System.out.println ("Hello "+ getUser().getName());

		String userInput = null;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		DisplayMenu();
		try {
			while ((userInput = stdIn.readLine()) != null && connectedToServer) 
			{
				switch(userInput){
					case "T":
						AdminMessage time = new AdminMessage(Message.MessageTypes.TIME);
						outputStream.writeObject(time);
						break;
					case "L":
						MessageUsersList list = new MessageUsersList();
						outputStream.writeObject(list);
						break;
					case "S":
						System.out.println ("Write his username and press enter");
						String username = stdIn.readLine();
						System.out.println ("Write your message and press enter");
						String mess = stdIn.readLine();
						BasicMessage basicMessage = new BasicMessage(Message.MessageTypes.BASIC_MESSAGE, mess, username, getUser().getName());
						outputStream.writeObject(basicMessage);
						break;
					case "Q":
						AdminMessage quit = new AdminMessage(Message.MessageTypes.QUIT);
						outputStream.writeObject(quit);
						logout();
						clientThreadListener.stopThread();
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void logon(String pwd){
		try {
			outputStream.writeObject(new LogonMessage(getUser().getName(), pwd));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logout(){
		try {
			outputStream.close();
			inputStream.close();
			echoSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public boolean initializeBuffer(){
		try {
			echoSocket = new Socket(serverHostname, serverPort);
			outputStream =  new ObjectOutputStream(echoSocket.getOutputStream());
			inputStream =  new ObjectInputStream(echoSocket.getInputStream());
			return true;
			} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + serverHostname);
//			System.exit(1);
			return false;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: " + serverHostname);
			return false;
//			System.exit(1);
		}
	}

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("unused")
		BasicClient client = new BasicClient(args[0], args[1]);
	}

	private static void DisplayMenu() {
		System.out.println ("MENU :");
		System.out.println ("Press T to ask for the time");
		System.out.println ("Press L for a list of users");
		System.out.println ("Press S to send a message to a user");
		System.out.println ("Press Q to disconnect");
	}
	
	class ThreadListener extends Thread 
	{
		private boolean running = true;
		//Create a thread which will just listen to the socket
		public void run() 
		{
			while(running){
				try {
					Message message = (Message) inputStream.readObject();
					
					switch(message.getMessageType()){
					case LOGON:
						System.out.println("You shouldn't receive this message");
						break;
					case TIME:
						System.out.println(message.getComment());
						break;
					case POSITION:
						break;
					case BASIC_MESSAGE:
						if(((BasicMessage) message).getSenderUsername() != null){
							System.out.println(((BasicMessage) message).getSenderUsername() + ": " + message.getComment());
						}else{
							System.out.println("Server: " + message.getComment());
						}
						break;
					case QUIT:
						System.out.print("Server down: "); 
						break;
					case LIST_USERS:
						DisplayListUsers(((MessageUsersList) message).getClientsList());
						break;
					}
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Connexion lost");
					stopThread();
					connectedToServer = false;
					notConnectedToServer();
					
				}
			}
		}
		
		public void stopThread(){
			running=false;
		}
	}

	private void DisplayListUsers(Vector<String> clientsList) {
		Iterator<String> i = clientsList.iterator();
	    while (i.hasNext()) {
	      System.out.println(i.next());
	    }
	}
	
	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
	}

	public ThreadListener getClientThreadListener() {
		return clientThreadListener;
	}

	public void setClientThreadListener(ThreadListener clientThreadListener) {
		this.clientThreadListener = clientThreadListener;
	}

	public boolean isConnectedToServer() {
		return connectedToServer;
	}

	public void setConnectedToServer(boolean connectedToServer) {
		this.connectedToServer = connectedToServer;
	}
}

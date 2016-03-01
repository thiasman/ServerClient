package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import messages.BasicMessage;
import messages.Message;
import messages.MessageUsersList;
/**
 * Connect to the server
 *
 */
public class BasicClient {

	private SimpleUser user;
	private final String serverHostname = new String ("127.0.0.1");
	private final int serverPort = 10008;
	
	Socket echoSocket;
	ObjectOutputStream outputStream =  null;
	ObjectInputStream inputStream =  null;
	
	public BasicClient() {
	}

	public void logon(){
		try {
			outputStream.writeObject(new BasicMessage(Message.MessageTypes.LOGON, getUser().getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initializeBuffer(){
		try {
			echoSocket = new Socket(serverHostname, serverPort);
			outputStream =  new ObjectOutputStream(echoSocket.getOutputStream());
			inputStream =  new ObjectInputStream(echoSocket.getInputStream());
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: " + serverHostname);
			System.exit(1);
		}
	}

	public static void main(String[] args) throws IOException {

		BasicClient client = new BasicClient();

		client.setUser(new SimpleUser(args[0]));

		System.out.println ("Attemping to connect to host " + client.serverHostname + " on port "+ client.serverPort + " .");
		
		client.initializeBuffer();

		client.logon();
		
		ThreadListener cliThread = client.new ThreadListener();
		cliThread.start(); 
		
		System.out.println ("Hello "+ client.getUser().getName());

		String userInput = null;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		DisplayMenu();
		while ((userInput = stdIn.readLine()) != null) 
		{
			switch(userInput){
				case "A":
					BasicMessage time = new BasicMessage();
					client.outputStream.writeObject(time);
					break;
				case "B":
					MessageUsersList list = new MessageUsersList();
					client.outputStream.writeObject(list);
					break;
			}
			DisplayMenu();
		}
		
		client.outputStream.close();
		client.inputStream.close();
		client.echoSocket.close();
	}

	private static void DisplayMenu() {
		System.out.println ("MENU :");
		System.out.println ("Press A to ask for the time");
		System.out.println ("Press B for a list of users");
	}
	
	class ThreadListener extends Thread 
	{
		//Create a thread which will just listen to the socket
		public void run() 
		{
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
				e.printStackTrace();
			}
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
}

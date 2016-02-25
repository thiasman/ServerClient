package main;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import messages.BasicMessage;
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
			outputStream.writeObject(new BasicMessage(BasicMessage.MessageTypes.LOGON, getUser().getName()));
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
		
		System.out.println ("Type Message (\"Bye.\" to quit)");
		
		BasicMessage time = new BasicMessage();
		client.outputStream.writeObject(time);
		
		String userInput = "test";
		
		while (userInput != null) 
		{
//			client.out.println(userInput);
//
//			// end loop
//			if (userInput.equals("Bye."))
//				break;
//
//			System.out.println("echo: " + client.in.readLine());
		}
		

		
		
		client.outputStream.close();
		client.inputStream.close();
		client.echoSocket.close();
	}

	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
	}
}

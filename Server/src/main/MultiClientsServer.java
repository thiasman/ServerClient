package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import messages.BasicMessage;


/**
 * Server
 *
 */
public class MultiClientsServer {

	private final int port = 10008;
	
	ServerSocket myServerSocket;
	boolean ServerOn = true;

	protected int nbClients =0;
	
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

		// Successfully created Server Socket. Now wait for connections. 
		while(ServerOn) 
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

		try 
		{ 
			myServerSocket.close(); 
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


	class ClientServiceThread extends Thread 
	{ 
		Socket myClientSocket;
		boolean m_bRunThread = true; 

		ObjectInputStream serverInputStream = null;
        ObjectOutputStream serverOutputStream = null;
		
		private int clientNumber =++nbClients;
		private String clientName;

		public ClientServiceThread() 
		{ 
			super(); 
		} 

		ClientServiceThread(Socket s) 
		{ 
			myClientSocket = s; 
		} 

		public void run() 
		{            
		
			
			// Print out details of this connection 
			System.out.println("Accepted client "+ clientNumber +" with address - " + myClientSocket.getInetAddress().getHostName()); 

			try 
			{                                
				serverInputStream = new ObjectInputStream(myClientSocket.getInputStream());
				serverOutputStream = new ObjectOutputStream(myClientSocket.getOutputStream());
				
				//Read the name which is sent automatically
				//clientName = in.readLine(); 

				// At this point, we can read for input and reply with appropriate output. 

				// Run in a loop until m_bRunThread is set to false 
				while(m_bRunThread) 
				{                    
					BasicMessage message = (BasicMessage) serverInputStream.readObject();
					
					if(!ServerOn) 
					{ 
						// Special command. Quit this thread 
						System.out.print("Server has already stopped"); 
						serverOutputStream.writeObject(new BasicMessage(BasicMessage.MessageTypes.BASIC_MESSAGE, "Server is down"));
						serverOutputStream.flush(); 
						m_bRunThread = false;   

					}
					switch(message.getMessageType()){
					case LOGON:
						clientName = message.getComment();
						break;
					case TIME:
						System.out.println("Client " + clientName + " asks for the time");
						Calendar now = Calendar.getInstance();
						SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
						serverOutputStream.writeObject(new BasicMessage(BasicMessage.MessageTypes.TIME,"It is now : " + formatter.format(now.getTime()))); 
						break;
					case POSITION:
						break;
					case BASIC_MESSAGE:
						break;
					case QUIT:
						m_bRunThread = false;   
						System.out.print("Stopping client thread for client : "); 
						break;
					}
				} 
			} 
			catch(Exception e) 
			{ 
				e.printStackTrace(); 
			} 
			finally 
			{ 
				// Clean up 
				try 
				{        
					serverOutputStream.close();
					serverInputStream.close();
					myClientSocket.close(); 
					System.out.println("...Stopped"); 
				} 
				catch(IOException ioe) 
				{ 
					ioe.printStackTrace(); 
				} 
			} 
		} 


	} 
}
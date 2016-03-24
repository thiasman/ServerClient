package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import messages.AdminMessage;
import messages.BasicMessage;
import messages.LogonMessage;
import messages.Message;
import messages.MessageUsersList;

class ClientServiceThread extends Thread 
{ 
	//Thread for each new client
	Socket myClientSocket;
	boolean m_bRunThread = true; 

	ObjectInputStream serverInputStream = null;
    ObjectOutputStream serverOutputStream = null;
	
	private String clientName;

	public ClientServiceThread() 
	{ 
		super(); 
	} 

	public void sendMessageTo(String message, String senderUserName) {
		try {
			serverOutputStream.writeObject(new BasicMessage(Message.MessageTypes.BASIC_MESSAGE, message, senderUserName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ClientServiceThread(Socket s) 
	{ 
		myClientSocket = s; 
	} 

	public void run() 
	{            
		// Print out details of this connection 
		System.out.println("Accepted client "+ clientName +" with address - " + myClientSocket.getInetAddress().getHostName()); 

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
				Message message = (Message) serverInputStream.readObject();
				
				if(!MultiClientsServer.isServerOn()) 
				{ 
					// Special command. Quit this thread 
					System.out.print("Server has already stopped"); 
					serverOutputStream.writeObject(new AdminMessage(Message.MessageTypes.QUIT, "Server is down"));
					serverOutputStream.flush(); 
					m_bRunThread = false;   

				}
				switch(message.getMessageType()){
				case LOGON:
					if(MultiClientsServer.getDbManager().isConnectedToDB()){
						if(MultiClientsServer.getDbManager().checkPassword(((LogonMessage)message).getLogonUsername(), ((LogonMessage)message).getLogonPassword())){
							clientName = ((LogonMessage)message).getLogonUsername();
							System.out.println("Client " + clientName + " successfully connect to the server.");
						}else{
							m_bRunThread = false;
							System.out.println("Wrong password or username");
						}
					}
					break;
				case TIME:
					System.out.println("Client " + clientName + " asks for the time");
					Calendar now = Calendar.getInstance();
					SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
					serverOutputStream.writeObject(new AdminMessage(Message.MessageTypes.TIME,"It is now : " + formatter.format(now.getTime()))); 
					break;
				case POSITION:
					break;
				case BASIC_MESSAGE:
					MultiClientsServer.findRecipient(((BasicMessage) message).getRecipientUsername(), message.getComment(), ((BasicMessage) message).getSenderUsername());
					break;
				case QUIT:
					m_bRunThread = false;
					MultiClientsServer.RemoveFromList(clientName);
					System.out.print("Stopping client thread for client : " + clientName); 
					break;
				case LIST_USERS:
					MessageUsersList mes = new MessageUsersList(Message.MessageTypes.LIST_USERS, "Here is the list of users");
					mes.setClientsList(MultiClientsServer.getListOfClients());
					serverOutputStream.writeObject(mes); 
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
			// Finally statement is executed even if an exception is caught
			// Used to clean all the shit
			try 
			{   
				m_bRunThread = false;
				MultiClientsServer.RemoveFromList(clientName);
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

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	} 
} 
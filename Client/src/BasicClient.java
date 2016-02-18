import java.io.*;
import java.net.*;

public class BasicClient {

	private SimpleUser user;
	private String serverHostname = new String ("127.0.0.1");
	Socket echoSocket;
	PrintWriter out;
	BufferedReader in;	

	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

	public BasicClient() {
	}

	public void logon(){
		out.println(getUser().getName());
	}

	public void initializeBuffer(){
		try {
			echoSocket = new Socket(serverHostname, 10008);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));
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

		System.out.println ("Attemping to connect to host " + client.serverHostname + " on port 10008.");

		String userInput;
		
		client.initializeBuffer();

		client.logon();
		
		System.out.println ("Type Message (\"Bye.\" to quit)");
		while ((userInput = client.stdIn.readLine()) != null) 
		{
			client.out.println(userInput);

			// end loop
			if (userInput.equals("Bye."))
				break;

			System.out.println("echo: " + client.in.readLine());
		}

		client.out.close();
		client.in.close();
		client.stdIn.close();
		client.echoSocket.close();
	}

	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
	}
}

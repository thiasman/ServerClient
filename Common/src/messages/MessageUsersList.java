package messages;

import java.util.Vector;

public class MessageUsersList extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -227193489442043496L;
	
	Vector<String> clientsList = null;
	
	public MessageUsersList() {
		super();
	}
	
	public MessageUsersList(MessageTypes type, String comment) {
		super(type, comment);
	}
	
	public Vector<String> getClientsList() {
		return clientsList;
	}

	public void setClientsList(Vector<String> clientsList) {
		this.clientsList = clientsList;
	}
}

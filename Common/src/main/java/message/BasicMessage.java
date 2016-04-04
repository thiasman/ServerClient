package message;

/**
 * Message from a user to another one
 */
public class BasicMessage extends Message{

	private static final long serialVersionUID = 998014782548599637L;
	

	public BasicMessage(MessageTypes type, String comment, String recipientUsername, String senderUsername) {
		super(type, comment, recipientUsername, senderUsername);
	}
	
	public BasicMessage(MessageTypes type, String comment, String senderUsername) {
		super(type, comment, senderUsername);
	}

	public BasicMessage(MessageTypes type, String comment) {
		super(type, comment);
	}

}

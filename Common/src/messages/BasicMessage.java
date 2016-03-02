package messages;

/**
 * Message from a user to another one
 */
public class BasicMessage extends Message{

	private static final long serialVersionUID = 998014782548599637L;
	
	private String recipientUsername;
	private String senderUsername;

	public BasicMessage(MessageTypes type, String comment, String recipientUsername, String senderUsername) {
		super(type, comment);
		this.recipientUsername = recipientUsername;
		this.senderUsername = senderUsername;
	}
	
	public BasicMessage(MessageTypes type, String comment, String senderUsername) {
		super(type, comment);
		this.senderUsername = senderUsername;
	}

	public BasicMessage(MessageTypes type, String comment) {
		super(type, comment);
	}
	
	public String getRecipientUsername() {
		return recipientUsername;
	}

	public void setRecipientUsername(String recipientUsername) {
		this.recipientUsername = recipientUsername;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
}

package messages;

/**
 * Message from a user to another one
 */
public class BasicMessage extends Message{

	private static final long serialVersionUID = 998014782548599637L;
	
	private String recipientUsername;

	public BasicMessage(MessageTypes type, String comment, String recipientUsername) {
		super(type, comment);
		this.recipientUsername = recipientUsername;
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
}

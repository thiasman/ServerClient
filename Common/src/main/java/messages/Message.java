package messages;
import java.io.Serializable;

/**
 * Define the type of message
 *
 */
public abstract class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8924373130989664963L;

	public enum MessageTypes {
	    TIME, POSITION, BASIC_MESSAGE, QUIT, LOGON, LIST_USERS
	}
	
	private MessageTypes messageType;
	private String comment = "";
	
	private String recipientUsername;
	private String senderUsername;
	
	public Message() {
	}
	
	public Message(MessageTypes mesType) {
		messageType = mesType;
	}

	public Message(MessageTypes type, String comment) {
		this.messageType = type;
		this.setComment(comment);
	}
	
	public Message(MessageTypes type, String comment, String recipientUsername, String senderUsername) {
		this.messageType = type;
		this.setComment(comment);
		this.setRecipientUsername(recipientUsername);
		this.setSenderUsername(senderUsername);
	}
	
	public Message(MessageTypes type, String comment, String senderUsername) {
		this.messageType = type;
		this.setComment(comment);
		this.setSenderUsername(senderUsername);
	}
	
	public MessageTypes getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageTypes messageType) {
		this.messageType = messageType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

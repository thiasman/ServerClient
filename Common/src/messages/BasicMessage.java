package messages;
import java.io.Serializable;

/**
 * Define the type of message
 *
 */
public class BasicMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8924373130989664963L;

	public enum MessageTypes {
	    TIME, POSITION, BASIC_MESSAGE, QUIT, LOGON
	}
	
	private MessageTypes messageType;
	private String comment = "";
	
	public BasicMessage() {
		//By default ask for the time
		messageType = MessageTypes.TIME;
	}

	public BasicMessage(MessageTypes type, String comment) {
		this.messageType = type;
		this.setComment(comment);
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
	
	
}

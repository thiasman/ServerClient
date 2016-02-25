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
	    TIME, POSITION, USERS 
	}
	
	private MessageTypes messageType;
	
	public BasicMessage() {
		//By default ask for the time
		messageType = MessageTypes.TIME;
	}

	public BasicMessage(MessageTypes type) {
		messageType = type;
	}
	
	public MessageTypes getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageTypes messageType) {
		this.messageType = messageType;
	}
	
	
}

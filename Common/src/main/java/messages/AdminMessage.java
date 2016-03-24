package messages;

/**
 * Message for the server
 */
public class AdminMessage extends Message{

	private static final long serialVersionUID = 328766323715491623L;

	public AdminMessage(MessageTypes mesType) {
		super(mesType);
	}
	
	public AdminMessage(MessageTypes type, String comment) {
		super(type, comment);
	}
	
}

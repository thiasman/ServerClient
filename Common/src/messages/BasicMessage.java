package messages;

public class BasicMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 998014782548599637L;

	public BasicMessage() {
		super();
	}
	
	public BasicMessage(MessageTypes mesType) {
		super(mesType);
	}
	
	public BasicMessage(MessageTypes type, String comment) {
		super(type, comment);
	}
	
}

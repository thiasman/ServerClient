package client;

/**
 * Contains info about the client
 *
 */

public class SimpleUser {
	
	private String name;
	
	public SimpleUser(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

package messages;

public class LogonMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -367473841229624494L;

	private String logonUsername;
	private String logonPassword;
	
	public LogonMessage(String username, String password){
		this.setMessageType(MessageTypes.LOGON);
		logonUsername = username;
		logonPassword = password;
	}

	public String getLogonUsername() {
		return logonUsername;
	}

	public void setLogonUsername(String logonUsername) {
		this.logonUsername = logonUsername;
	}

	public String getLogonPassword() {
		return logonPassword;
	}

	public void setLogonPassword(String logonPassword) {
		this.logonPassword = logonPassword;
	}
}

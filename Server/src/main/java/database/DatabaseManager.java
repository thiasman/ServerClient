package database;

import java.sql.*;

/*
 * Connect to the database server_database
 * Create the database and use the dump to have the same one
 * Then modify the credential to be able to access it
 * 
 */

public class DatabaseManager {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/server_database?characterEncoding=UTF-8&useSSL=false";

	//  Database credentials
	static final String DB_USER = "root";
	static final String DB_PASSWORD = "0000";

	private Connection dbConnection = null;

	boolean ConnectedToDB = false;
	
	public DatabaseManager() {
		ConnectionToDB();
	}

	public boolean checkPassword(String username, String password){
		String goodPassword = null;
		boolean isGood = false;
		try {
			Statement stmt = dbConnection.createStatement();
			System.out.println("Fetching password for username " + username);
			String sql = "SELECT password FROM clients" + " WHERE username = \"" + username + "\";";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
		         goodPassword = rs.getString("password");
		    }
			
			if(password.equals(goodPassword)){
				isGood = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isGood;
	}

	private void ConnectionToDB() {
		dbConnection = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			System.out.println("Connected database successfully...");
			
			ConnectedToDB = true;

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void closeConnectionToDB() throws SQLException{
		System.out.println("Closing connection to DB ...");
		ConnectedToDB = false;
		dbConnection.close();
	}
	
	public static void main(String[] args) {
		//Main class use for testing
		DatabaseManager dbManager = new DatabaseManager();
		dbManager.ConnectionToDB();
	}
	
	public boolean isConnectedToDB() {
		return ConnectedToDB;
	}

	public void setConnectedToDB(boolean connected) {
		this.ConnectedToDB = connected;
	}
}
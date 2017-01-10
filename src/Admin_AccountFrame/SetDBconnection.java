package Admin_AccountFrame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SetDBconnection {	
	private Connection conn;
 
	public SetDBconnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection makeConnection() {
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@211.238.142.180:1521:xe", "sist2", "sist2");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public void disConnection() {
		try {
			if(!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

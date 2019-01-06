package uni.colewe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyRunner {
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/colewe";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "doYouSaySequel?";
	
	static final String INFILE = "C://_projects//GWT//ThirdHomework//war//WEB-INF//dict.tsv";

	public static void main(String[] args) {
		Connection conn;
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected");
			Statement stmt = conn.createStatement();
			// clear table
			String sql1 = "DELETE FROM dict";
			System.out.println("Deleting....");
			stmt.execute(sql1);
			System.out.println("Done!");
			// import data
			String opt = "SET GLOBAL local_infile = true";
			stmt.execute(opt);
			String sql2 = "LOAD DATA LOCAL INFILE \"" + INFILE + "\" INTO TABLE dict";
			System.out.println("Importing....");
			stmt.execute(sql2);
			System.out.println("Done!");
			// close connection
			conn.close();
			System.out.println("Disconnected");
		}
		catch (SQLException e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
	}
}

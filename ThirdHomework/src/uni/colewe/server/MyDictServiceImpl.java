package uni.colewe.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import uni.colewe.client.MyDictService;
import uni.colewe.shared.MyDictionaryEntry;

@SuppressWarnings("serial")
public class MyDictServiceImpl extends RemoteServiceServlet implements MyDictService{
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/colewe";

	// Database credentials
	static final String USER = "homework";
	static final String PASS = "test";

	@Override
	public List<MyDictionaryEntry> queryServer(String query, boolean rev, boolean like) throws IllegalArgumentException{
		Set<MyDictionaryEntry> results = new HashSet<MyDictionaryEntry>();
		Connection conn;
		try {

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String insertStr = "";
			if(rev && like){
				insertStr ="SELECT * FROM dict WHERE eng LIKE ?";
				query = "%" + query + "%";
			} else if(rev && !like){
				insertStr ="SELECT * FROM dict WHERE eng RLIKE ?";
				query = "(^|[^a-z]+)" + query + "($|[^a-z]+)";
			} else if(!rev && like){
				insertStr ="SELECT * FROM dict WHERE rus LIKE ?";
				query = "%" + query + "%";
			} else {
				insertStr ="SELECT * FROM dict WHERE rus = ?";
			}

			PreparedStatement stmt = conn.prepareStatement(insertStr);
			stmt.setString(1, query);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				results.add(new MyDictionaryEntry(rs.getString("rus"), rs.getString("pos"), rs.getString("eng")));
			}
		
			conn.close();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
		return new ArrayList<MyDictionaryEntry>(results);
	}

	
	  @Override
	  public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    try {
			// initialize data base driver
			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	  }
}

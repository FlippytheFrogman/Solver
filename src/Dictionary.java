import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * @author sheehy
 *
 */
public class Dictionary {

	/**
	 * @param args
	 */
	Props property = new Props();
	
	//private PreparedStatement preparedStatement = null;
	String prepared = "select * from words where name= ?";
	String query = "select * from words where name= ";
	DataSource dataSource;
	
	public Dictionary() {
		
		String url = property.getDbUrl();
		String username = property.getDbUserName(); 
		String password = property.getDbPassword();

		System.out.println("Connecting to database...");
		String connectURI = url + "?autoReconnect=true&useSSL=false";
		dataSource = TestDB.setupDataSource(connectURI);

		//try {
		//connection = DriverManager.getConnection(url, username, password);
		//System.out.println("Database connected!");
		//preparedStatement = connection.prepareStatement(prepared);
		//connection.setAutoCommit(true);

		//} catch (SQLException e) {
		//	System.out.println("Error connecting to database.");
		//	e.printStackTrace();
		//}
	}

	//public void releaseConnection() {
	//	System.out.println("Release connection");
	//	try {
	//		connection.close();
	//	} catch (SQLException e) {
	//		System.out.println("Error releasing connection");
	//		e.printStackTrace();
	//	}
	//}

	public boolean isWord(String word) {
		boolean result = false;
		try {

			//
			// Now, we can use JDBC DataSource as we normally would.
			//
			Connection conn = null;
			Statement stmt = null;
			ResultSet rset = null;

			try {
				//System.out.println("Creating connection.");

				conn = dataSource.getConnection();
				//System.out.println("Creating statement.");

				stmt = conn.createStatement();
				//preparedStatement = conn.prepareStatement(prepared);
				//preparedStatement.setString(1, word);
				//System.out.println("Executing statement.");
				//rset = preparedStatement.executeQuery();
				String str = query + "'" + word + "'" ;
		        rset = stmt.executeQuery(str);

				//connection.commit();
				//rset = preparedStatement.getResultSet();
				if (rset.next()){
					result = true;
				}
				//TestDB.printDataSourceStats(dataSource);

			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				try { if (rset != null) rset.close(); } catch(Exception e) { }
				try { if (stmt != null) stmt.close(); } catch(Exception e) { }
				try { if (conn != null) conn.close(); } catch(Exception e) { }
			}
		}
		//preparedStatement.setString(1, word);
		//ResultSet rs = preparedStatement.executeQuery();
		//connection.commit();
		//ResultSet rs = preparedStatement.getResultSet();
		//if (rs.next()){
		//	result = true;
		//}
		//} catch (SQLException e) {
		//	System.out.println("SQL Exception");
		//	e.printStackTrace();
		//}
		finally {
		}
		return result;
	}

	public static void main(String[] args) {
		Dictionary v = new Dictionary();
		System.out.println(v.isWord("apple"));
		System.out.println(v.isWord("xyzzy"));
		System.out.println(v.isWord("pear"));
		//v.releaseConnection();
	}
}
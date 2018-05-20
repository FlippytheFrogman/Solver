import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sheehy
 *
 */
public class ConnectionDictionary {

	/**
	 * @param args
	 */
	
	
	Connection connection = null;

	PreparedStatement preparedStatement = null;
	String prepared= "select * from words where name= ?";
	
	public ConnectionDictionary() {
		System.out.println("Connecting to database...");
		Props property = new Props();
		String url = property.getDbUrl();
		String username = property.getDbUserName(); 
		String password = property.getDbPassword();

		try {
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
			preparedStatement = connection.prepareStatement(prepared);
			connection.setAutoCommit(true);

			
		} catch (SQLException e) {
			System.out.println("Error connecting to database.");
			e.printStackTrace();
		}
	}

	public void releaseConnection() {
		System.out.println("Release connection");
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error releasing connection");
			e.printStackTrace();
		}
	}

	public boolean isWord(String word) {
		boolean result = false;
		try {
			preparedStatement.setString(1, word);
			ResultSet rs = preparedStatement.executeQuery();
			//connection.commit();
			//ResultSet rs = preparedStatement.getResultSet();
			if (rs.next()){
				result = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception");
			e.printStackTrace();
		}
		finally {
		}
		return result;
	}

	public static void main(String[] args) {
		ConnectionDictionary v = new ConnectionDictionary();
		System.out.println(v.isWord("apple"));
		System.out.println(v.isWord("xyzzy"));
		v.releaseConnection();
	}
}
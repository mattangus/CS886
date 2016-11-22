import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.commons.dbcp2.BasicDataSource;

public class Driver {

	public static void main(String[] args) throws SQLException {		
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		final String DB_URL = "jdbc:mysql://gdansk/gamedata";
		
		//  Database credentials
		final String USER = "egta";
		final String PASS = "gametheorypass";
		
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(JDBC_DRIVER);
		ds.setUsername(USER);
		ds.setPassword(PASS);
		ds.setUrl(DB_URL);
		
		ds.setMinIdle(5);
		ds.setMaxIdle(20);
		ds.setMaxOpenPreparedStatements(180);
		
		Connection con = ds.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from profiles");
		while (rs.next()) {
			System.out.println("found a record");
		}
		System.out.println("done");
		
		if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
        if (st != null) try { st.close(); } catch (SQLException e) {e.printStackTrace();}
        if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
	}
}
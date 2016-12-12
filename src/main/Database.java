package main;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Database {
	private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private final static String DB_URL = "jdbc:mysql://gdansk/gamedata";
	//  Database credentials
	private final static String USER = "egta";
	private final static String PASS = "gametheorypass";
	
	private ComboPooledDataSource ds;
	
	private static Database instance;
	public static synchronized Database getInstance() throws PropertyVetoException
	{
		if(instance == null)
			instance = new Database();
		return instance;
	}
	
	private Database() throws PropertyVetoException
	{
		ds = new ComboPooledDataSource();
		ds.setDriverClass(JDBC_DRIVER);
		ds.setJdbcUrl(DB_URL);
		ds.setUser(USER);
		ds.setPassword(PASS);
	}
	
	public static synchronized void close()
	{
		if(instance != null)
		{
			instance.ds.close();
		}
	}
	
	public Connection getConnection() throws SQLException
	{
		return ds.getConnection();
	}
}

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.ResultSet;

public class Driver {

	public static void main(String[] args) throws SQLException, PropertyVetoException {
		Connection[] cons = new Connection[5];
		for(int i = 0; i < cons.length; i++)
		{
			cons[i] = Database.getInstance().getConnection();
		}
		
		Database.close();
	}
}
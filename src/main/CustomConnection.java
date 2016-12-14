package main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class CustomConnection {
	Connection con;
	
	public CustomConnection(Connection con)
	{
		this.con = con;
	}
	
	public int addOAgentProfile(Integer id, int encoding) throws SQLException
	{
		CallableStatement cs = con.prepareCall("{? = call gamedata.add_o_agent_profile(?,?)}");
		cs.registerOutParameter(1, Types.INTEGER);
		if(id == null)
			cs.setNull(2, Types.INTEGER);
		else
			cs.setInt(2, id);
		cs.setInt(3, encoding);
		cs.execute();
		return cs.getInt(1);
	}
	
	public Integer getOAgentProfileId(Integer[] list) throws SQLException
	{
		if(list.length != 4)
			throw new RuntimeException("can only have 4 args");
		CallableStatement cs = con.prepareCall("{? = call gamedata.get_o_agent_profile_id(?,?,?,?)}");
		cs.registerOutParameter(1, Types.INTEGER);
		for(int i = 0; i < list.length; i++)
		{
			if(list[i]!=null)
				cs.setInt(i+2, list[i]);
			else
				cs.setNull(i+2, Types.INTEGER);
		}
		cs.execute();
		Integer ret = cs.getInt(1);
		if(cs.wasNull())
			ret = null;
		return ret;
	}
	
	public int addStrategy(int encoding) throws SQLException
	{
		CallableStatement cs = con.prepareCall("{? = call gamedata.add_strategy(?)}");
		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.setInt(2, encoding);
		cs.execute();
		return cs.getInt(1);
	}
	
	public void addObservation(int profileId, int strategyId, int oAgentProfileId, int numPlayers, double payoff) throws SQLException
	{
		CallableStatement cs = con.prepareCall("{call gamedata.add_observation(?,?,?,?,?)}");
		cs.setInt(1, profileId);
		cs.setInt(2, strategyId);
		cs.setInt(3, oAgentProfileId);
		cs.setInt(4, numPlayers);
		cs.setDouble(5, payoff);
		cs.execute();
	}
	
	public int addProfile(int[] assignment) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < assignment.length; i++)
		{
			sb.append(assignment[i]);
			if(i != assignment.length - 1)
				sb.append(",");
		}
		String strAssignment = sb.toString();
		int numStrat = assignment.length;
		
		CallableStatement cs = con.prepareCall("{? = call gamedata.add_profile(?,?)}");
		cs.registerOutParameter(1, Types.INTEGER);
		cs.setString(2, strAssignment);
		cs.setInt(3, numStrat);
		cs.execute();
		return cs.getInt(1);
	}
	
	public void addObservation()
	{
		
	}
}

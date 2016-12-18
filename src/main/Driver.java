package main;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sfm.agents.Agent;
import com.sfm.agents.AgentContainer;
import com.sfm.agents.EgoCar;
import com.sfm.agents.Pedestrian;
import com.sfm.agents.PedestrianFactory;
import com.sfm.ds.PedestrianBehaviour;
import com.sfm.ds.Vector2d;
import com.sfm.engine.SFMEngine;
import com.sfm.helper.RandomHelper;
import com.sfm.states.AgentState;

import java.sql.ResultSet;

public class Driver {

	public static void main(String[] args) throws SQLException, PropertyVetoException {
		/*Connection[] cons = new Connection[5];
		for(int i = 0; i < cons.length; i++)
		{
			cons[i] = Database.getInstance().getConnection();
		}
		
		Database.close();*/
		
		int numCores = Runtime.getRuntime().availableProcessors()*2;
		if(args.length != 0)
			numCores = Integer.parseInt(args[0]);
		
		MultiSim sim = new MultiSim();
		sim.run(numCores);
		Database.close();
		
		/*ClusterCalculator cc = new ClusterCalculator(new TrackParser("tracks.txt").parse(),5);
		cc.computeStats();
		*/
	}
	
}

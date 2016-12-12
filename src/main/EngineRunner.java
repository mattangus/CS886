package main;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.sfm.BaseMap;
import com.sfm.agents.Agent;
import com.sfm.agents.AgentContainer;
import com.sfm.agents.EgoCar;
import com.sfm.agents.Pedestrian;
import com.sfm.agents.PedestrianFactory;
import com.sfm.constants.PedestrianConst;
import com.sfm.ds.NormalDistribution;
import com.sfm.ds.PedestrianBehaviour;
import com.sfm.ds.Vector2d;
import com.sfm.engine.SFMEngine;
import com.sfm.helper.RandomHelper;
import com.sfm.states.AgentState;

public class EngineRunner implements Runnable {
	
	Connection con;
	MultiBaseNum current;
	double step;
	double min;
	double max;
	List<PartialBehaviour> clusters;
	BaseMap map;
	
	public EngineRunner(Connection con, List<PartialBehaviour> clusters, BaseMap map)
	{
		this.con = con;
		this.map = map;
		min = 0.1;
		max = 3;
		int desired = 10;
		step = (max-min)/desired;
		this.clusters = clusters;
		int k = clusters.size();
		current = new MultiBaseNum(new int[] {k,desired,desired,desired,desired,desired,desired});
	}
	
	@Override
	public void run() {
		try {
			execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void execute()
	{
		System.out.println("Thread " + Thread.currentThread().getId() + " powering up");
		//for(MultiBaseNum i = new MultiBaseNum(min); i.compareTo(max) < 0; i.inc())
		for(int i = 0; i < 10; i++)
		{
			ArrayList<Agent> agentList = new ArrayList<Agent>();
			for(int j = 0; j < Consts.numPlayers; j++)
			{
				current.set(RandomHelper.nextInt(current.getMax()));
				PedestrianBehaviour pb = getCurrentBehaviour();
				Pedestrian ped = PedestrianFactory.generatePedestrian(map);
				ped.behaviour = pb;
				agentList.add(ped);
			}
			AgentContainer agents = new AgentContainer(agentList);
			agents.car = new EgoCar(new AgentState(new Vector2d()));
			SFMEngine engine = new SFMEngine(0.1, agents, map);
			engine.run(false);
		}
		System.out.println("Thread " + Thread.currentThread().getId() + " donezo");
	}

	private PedestrianBehaviour getCurrentBehaviour()
	{
		int[] values = current.getValue();
		PedestrianBehaviour pb = new PedestrianBehaviour();
		PartialBehaviour partial = clusters.get(values[0]);
		
		//not observable
		pb.crosswalkPullStrengthCoefA = min + step*(double)values[1];
		pb.crosswalkPullStrengthCoefB = min + step*(double)values[2];
		pb.crosswalkPushStrengthCoefA = min + step*(double)values[3];
		pb.crosswalkPushStrengthCoefB = min + step*(double)values[4];
		pb.repulsiveStrengthCoefA = min + step*(double)values[5];
		pb.repulsiveStrengthCoefB = min + step*(double)values[6];
		
		//observable
		pb.evasiveSpeedChange = partial.evasiveSpeedChange;
		pb.evasiveAngleChange = partial.evasiveAngleChange;
		pb.accelerationTime = partial.accelerationTime;
		pb.desiredSpeed = partial.desiredSpeed;
		
		//calibration not required
		pb.privateSphere = PedestrianConst.privateSphere;
		pb.evasiveRelativeTimeThresh = PedestrianConst.evasiveRelativeTimeThresh;
		pb.visualRange = PedestrianConst.visualRange;
		pb.fieldOfView = PedestrianConst.fieldOfView;
		return pb;
	}
	
}

package main;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sfm.BaseMap;
import com.sfm.agents.Agent;
import com.sfm.agents.AgentContainer;
import com.sfm.agents.AgentType;
import com.sfm.agents.EgoCar;
import com.sfm.agents.Pedestrian;
import com.sfm.agents.PedestrianFactory;
import com.sfm.constants.PedestrianConst;
import com.sfm.ds.PedestrianBehaviour;
import com.sfm.ds.Vector2d;
import com.sfm.engine.SFMEngine;
import com.sfm.helper.RandomHelper;
import com.sfm.helper.SimulationSnapshot;
import com.sfm.states.AgentState;

public class EngineRunnerBR implements Runnable {
	
	CustomConnection con;
	MultiBaseNum current;
	double step;
	double min;
	double max;
	List<PartialBehaviour> clusters;
	BaseMap map;
	public static final int maxLoop = 10000;
	PayoffCalculator payoffCalc;
	double timeStep;
	
	public EngineRunnerBR(CustomConnection con, List<PartialBehaviour> clusters, BaseMap map, double timeStep, PayoffCalculator payoffCalc)
	{
		this.con = con;
		this.map = map;
		min = 0.1;
		max = 3;
		int desired = 8;
		step = (max-min)/desired;
		this.clusters = clusters;
		this.timeStep = timeStep;
		int k = clusters.size();
		current = new MultiBaseNum(new int[] {k,desired,desired,desired,desired,desired,desired});
		this.payoffCalc = payoffCalc;
	}
	
	@Override
	public void run() {
		try {
			execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void execute() throws SQLException
	{
		System.out.println("Thread " + Thread.currentThread().getId() + " powering up");
		//for(MultiBaseNum i = new MultiBaseNum(min); i.compareTo(max) < 0; i.inc())
		for(int i = 0; i < maxLoop; i++)
		{
			int[] orig = con.getRandomProfile();
			int numExplore = 500;
			for(int p = 0; p < numExplore; p++)
			{
				int[] profiles = new int[orig.length];
				int toMod = RandomHelper.nextInt(orig.length);
				for(int q = 0; q < orig.length; q++)
				{
					if(q != toMod)
						profiles[q] = orig[q];
					else
						profiles[q] = RandomHelper.nextInt(current.getMax()); //assign random to one 
				}
				int numTrial = 5;
				PayoffVector totalPayoff = new PayoffVector(Consts.numPlayers);
				//average over 10
				for(int k = 0; k < numTrial; k++)
				{
					ArrayList<Agent> agentList = new ArrayList<Agent>();
					for(int j = 0; j < Consts.numPlayers; j++)
					{
						current.set(profiles[j]);
						PedestrianBehaviour pb = getCurrentBehaviour();
						Pedestrian ped = PedestrianFactory.generatePedestrian(map);
						ped.behaviour = pb;
						agentList.add(ped);
					}
					AgentContainer agents = new AgentContainer(agentList);
					agents.car = new EgoCar(new AgentState(new Vector2d()));
					SFMEngine engine = new SFMEngine(timeStep, agents, map);
					engine.run(false);
					ArrayList<Trajectory> trajectories = toTrajectories(engine.snapshots);
					PayoffVector payoffs = payoffCalc.getPayoff(trajectories);
					totalPayoff.add(payoffs);
				}
				totalPayoff.div(numTrial);
				System.out.println("updating db");
				updateDB(profiles,totalPayoff);
				System.out.println("done updating");
			}
		}
		System.out.println("Thread " + Thread.currentThread().getId() + " donezo");
	}
	
	private void updateDB(int[] profile, PayoffVector payoff)
	{
		for(int i = 0; i < profile.length; i++)
		{
			if(profile[i] <0)
				continue;
			try {
				int strategyId = con.addStrategy(profile[i]);
				int profileId = con.addProfile(profile);
				ArrayList<Integer> oAgentsProfiles = new ArrayList<Integer>();
				int count = 1;
				for(int j = 0; j < profile.length; j++)
				{
					if(i != j)
					{
						if(profile[i] == profile[j])
						{
							count++;
							profile[j] = -1;
						}
						else
						{
							oAgentsProfiles.add(profile[j]);
						}
					}
				}
				if(count == Consts.numPlayers) //all playing same strat
				{
					oAgentsProfiles.add(profile[i]);
				}
				while(oAgentsProfiles.size() < profile.length)
					oAgentsProfiles.add(null);
				Integer oAgentProfileId = con.getOAgentProfileId(oAgentsProfiles.toArray(new Integer[0]));
				if(oAgentProfileId == null)
				{
					for(int j = 0; j < oAgentsProfiles.size(); j++)
					{
						if(oAgentsProfiles.get(j) == null)
							break;//the rest will be null too
						oAgentProfileId = con.addOAgentProfile(oAgentProfileId, oAgentsProfiles.get(j));
					}
				}
				
				con.addObservation(profileId, strategyId, oAgentProfileId, count, payoff.payoffs.get(i));
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("don't know what to do");
			}
		}
	}
	
	private ArrayList<Trajectory> toTrajectories(ArrayList<SimulationSnapshot> snapshots)
	{
		ArrayList<Trajectory> ret = new ArrayList<Trajectory>();
		for(int i = 0; i < Consts.numPlayers; i++)
			ret.add(new Trajectory());
		
		for(int i = 0; i < snapshots.size(); i++)
		{
			SimulationSnapshot cur = snapshots.get(i);
			for(int j = 0; j < Consts.numPlayers; j++)
			{
				if(cur.types.get(j) == AgentType.Pedestrian)
				{
					ret.get(j).points.add(new TimePoint2d(cur.positions.get(j).x,cur.positions.get(j).y,timeStep*i));
				}
			}
		}
		
		return ret;
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

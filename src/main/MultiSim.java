package main;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.sfm.BaseMap;
import com.sfm.constants.PedestrianConst;
import com.sfm.ds.NormalDistribution;
import com.sfm.helper.RandomHelper;

public class MultiSim {
	
	public MultiSim()
	{
		RandomHelper.nextInt(10);//prevent invalid access to singleton
	}
	
	public void run(int numCores)
	{
		ExecutorService ex = Executors.newFixedThreadPool(numCores);
		
		/*ArrayList<double[]> minDiv = new ArrayList<double[]>();
		ArrayList<double[]> maxDiv = new ArrayList<double[]>();
		
		double[] min = new double[]  {1, 0.1, 0.1, 0.1, 0.1, 0.1};
		double[] max = new double[]  {3, 2,   2,   2,   2,   2  };
		double[] steps = new double[]{1, 1,   1,   1,   1,   1  };
		
		for(int i = 0; i < numCores; i++)
		{
			double[] tempMin = new double[min.length];
			double[] tempMax = new double[max.length];
			
			for(int j = 0; j < tempMin.length; j++)
			{
				double step = (max[j] - min[j])/numCores;
				tempMin[j] = i*step + min[j];
				tempMax[j] = (i+1)*step + min[j];
			}
			minDiv.add(tempMin);
			maxDiv.add(tempMax);
		}*/
		
		ArrayList<PartialBehaviour> clusters = getClusters();
		
		TrackParser tp = new TrackParser("tracks.txt");
		PayoffCalculator pc = new PayoffCalculator(tp.parse());
		
		long start = System.nanoTime();
		for(int i = 0; i < numCores; i++)
		{
			BaseMap map = new BaseMap("map.xml");
			map.init();
			try {
				ex.submit(new EngineRunner(new CustomConnection(Database.getInstance().getConnection()), clusters, map, 0.1, pc));
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		
		ex.shutdown();
		
		try { ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); } catch (InterruptedException e) { e.printStackTrace();}
		
		long end = System.nanoTime();
		
		System.out.println("cores: " + numCores + ", time: " + (double)(end-start)*1e-9 + " time per sim " + ((double)(end-start)*1e-9)/(numCores*((double)EngineRunner.maxLoop)));
	}
	private ArrayList<PartialBehaviour> getClusters()
	{
		ArrayList<PartialBehaviour> clusters = new ArrayList<PartialBehaviour>();
		//Cluster 0
		PartialBehaviour pb0 = new PartialBehaviour();
		pb0.desiredSpeed = 1.4453559898158483;
		pb0.accelerationTime = 0.3755818553810574;
		pb0.evasiveAngleChange = new NormalDistribution(12.03842946618673,1.9459895828399674);
		pb0.evasiveSpeedChange = new NormalDistribution(0.0057631129018505325,0.022148983461130905);
		clusters.add(pb0);
		//-----------------------//
		//Cluster 1
		PartialBehaviour pb1 = new PartialBehaviour();
		pb1.desiredSpeed = 1.3015572571256948;
		pb1.accelerationTime = 0.44924938066233316;
		pb1.evasiveAngleChange = new NormalDistribution(19.365430968771832,2.53537487356503);
		pb1.evasiveSpeedChange = new NormalDistribution(0.010456530417234905,0.025652612311811207);
		clusters.add(pb1);
		//-----------------------//
		//Cluster 2
		PartialBehaviour pb2 = new PartialBehaviour();
		pb2.desiredSpeed = 0.9702095950624665;
		pb2.accelerationTime = 1.0587329410795274;
		pb2.evasiveAngleChange = new NormalDistribution(30.52626943976455,4.047508011377513);
		pb2.evasiveSpeedChange = new NormalDistribution(0.0038451950166167874,0.017105663372396173);
		clusters.add(pb2);
		//-----------------------//
		//Cluster 3
		PartialBehaviour pb3 = new PartialBehaviour();
		pb3.desiredSpeed = 0.4176270672800551;
		pb3.accelerationTime = 1.9563037757607424;
		pb3.evasiveAngleChange = new NormalDistribution(49.00043265357544,7.261484685168292);
		pb3.evasiveSpeedChange = new NormalDistribution(0.0025905990907527047,0.00585422124275064);
		clusters.add(pb3);
		//-----------------------//
		//Cluster 4
		PartialBehaviour pb4 = new PartialBehaviour();
		pb4.desiredSpeed = 1.5740634569109089;
		pb4.accelerationTime = 0.3329298885525596;
		pb4.evasiveAngleChange = new NormalDistribution(5.875583596005186,1.5604117403490163);
		pb4.evasiveSpeedChange = new NormalDistribution(0.004176817229910902,0.017323734822400377);
		clusters.add(pb4);
		//-----------------------//

		return clusters;
	}
}

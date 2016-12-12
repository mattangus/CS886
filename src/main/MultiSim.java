package main;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.sfm.BaseMap;
import com.sfm.constants.PedestrianConst;
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
		
		ArrayList<PartialBehaviour> clusters = new ArrayList<PartialBehaviour>();
		PartialBehaviour pb = new PartialBehaviour();
		pb.evasiveSpeedChange = PedestrianConst.evasiveSpeedChange;
		pb.evasiveAngleChange = PedestrianConst.evasiveAngleChange;
		pb.accelerationTime = PedestrianConst.accelerationTime.nextDouble(0.001,2.0);
		pb.desiredSpeed = PedestrianConst.desiredSpeed.nextDouble(0.001,200.0);
		clusters.add(pb);
		
		long start = System.nanoTime();
		for(int i = 0; i < numCores; i++)
		{
			BaseMap map = new BaseMap("map.xml");
			map.init();
			//try {
				ex.submit(new EngineRunner(null/*Database.getInstance().getConnection()*/, clusters, map));
			//} catch (SQLException e) {
			//	e.printStackTrace();
			//} catch (PropertyVetoException e) {
			//	e.printStackTrace();
			//}
		}
		
		//long clockStart = System.currentTimeMillis();
		//while(clockStart + 20000 > System.currentTimeMillis());
		
		ex.shutdown();
		
		try { ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); } catch (InterruptedException e) { e.printStackTrace();}
		
		long end = System.nanoTime();
		
		System.out.println("cores: " + numCores + ", time: " + (double)(end-start)*1e-9 + " time per sim " + ((double)(end-start)*1e-9)/(numCores));
	}
	
}

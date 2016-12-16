package main;

import java.util.ArrayList;

import com.sfm.constants.PedestrianConst;
import com.sfm.ds.Vector2d;
import com.sfm.helper.RandomHelper;

import main.kmeans.DoubleEKmeans;

public class ClusterCalculator {
	
	ArrayList<Trajectory> trajectories;
	private double timeStep = 0.1;
	int k;
	
	public ClusterCalculator(ArrayList<Trajectory> trajectories, int k)
	{
		this.trajectories = trajectories;
		this.k = k;
	}
	
	public void printClusters()
	{
		for(int i = 0; i < trajectories.size(); i++)
		{
			Trajectory t = trajectories.get(i);
			System.out.print("len " + t.points.size() + " ");
			double avgSpeed = averageSpeed(t);
			System.out.print("avgSpeed " + avgSpeed + " ");
			double relTime = relaxationTime(t,avgSpeed);
			System.out.print("relTime " + relTime + " ");
			double[] velChange = velChange(t);
			System.out.print("velChange avg " + velChange[0] + " ");
			double[] dirChange = dirChange(t);
			System.out.print("dirChange avg " + dirChange[0] + " ");
			
			System.out.println();
		}
	}
	
	private class ArrayListDouble extends ArrayList<Double> {private static final long serialVersionUID = -7228972406999468056L;}
	public void computeStats()
	{
		double[][] points = new double[trajectories.size()][4];
		double[][] centroids = new double[k][4];
		for(int i = 0; i < trajectories.size(); i++)
		{
			Trajectory t = trajectories.get(i);
			double avgSpeed = averageSpeed(t);
			double relTime = relaxationTime(t,avgSpeed);
			double[] velChange = velChange(t);;
			double[] dirChange = dirChange(t);
			
			points[i][0] = avgSpeed;
			points[i][1] = relTime;
			points[i][2] = velChange[0];
			points[i][3] = dirChange[0];
			
		}
		
		for(int i = 0; i < k; i++)
		{
			for(int j = 0; j < 4; j++)
					centroids[i][j] = RandomHelper.nextDouble(-100, 100);
		}
		
		DoubleEKmeans kmeans = new DoubleEKmeans(centroids,points,false,DoubleEKmeans.EUCLIDEAN_DISTANCE_FUNCTION,null);
		int[] assignment = kmeans.run(5000);
		
		ArrayListDouble[][] groups = new ArrayListDouble[k][4];
		for(int j = 0; j < 4; j++)
		{
			for(int i = 0; i < k; i++)
			{
				groups[i][j] = new ArrayListDouble();
			}
			
			for(int i = 0; i < points.length; i++)
			{
				if(assignment[i] >= 0)
					groups[assignment[i]][j].add(points[i][j]);
			}
		}
		boolean code = false;
		
		for(int i = 0; i < k; i++)
		{
			/*PartialBehaviour pb = new PartialBehaviour();
			pb.evasiveSpeedChange = PedestrianConst.evasiveSpeedChange;
			pb.evasiveAngleChange = PedestrianConst.evasiveAngleChange;
			pb.accelerationTime = PedestrianConst.accelerationTime.nextDouble(0.001,2.0);
			pb.desiredSpeed = PedestrianConst.desiredSpeed.nextDouble(0.001,200.0);
			clusters.add(pb);*/
			
			if(code)
			{
				System.out.println("//Cluster " + i + " (" + groups[i][0].size() + ")");
				System.out.println("PartialBehaviour pb" + i + " = new PartialBehaviour();");
				System.out.println("pb" + i + ".desiredSpeed = " + centroids[i][0] + ";");
				System.out.println("pb" + i + ".accelerationTime = " + centroids[i][1] + ";");
				System.out.println("pb" + i + ".evasiveAngleChange = new NormalDistribution(" + centroids[i][3] + "," + getSD(groups[i][3],centroids[i][3]) + ");");
				System.out.println("pb" + i + ".evasiveSpeedChange = new NormalDistribution(" + centroids[i][2] + "," + getSD(groups[i][2],centroids[i][2]) + ");");
				System.out.println("clusters.add(pb" + i + ");");
				System.out.println("//-----------------------//");
			}
			else
			{
				System.out.print(round(centroids[i][0],3) + " (" + round(getSD(groups[i][0],centroids[i][0]),3) + ")\t");
				System.out.print(round(centroids[i][1],3) + " (" + round(getSD(groups[i][1],centroids[i][1]),3) + ")\t");
				System.out.print(round(centroids[i][2],3) + " (" + round(getSD(groups[i][2],centroids[i][2]),3) + ")\t");
				System.out.print(round(centroids[i][3],3) + " (" + round(getSD(groups[i][3],centroids[i][3]),3) + ")\t");
				System.out.print(groups[i][0].size());
				System.out.println();
			}
			
		}
		
	}
	
	private double round(double val, int numDec)
	{
		return (double) Math.round(val * Math.pow(10, numDec)) / Math.pow(10.0, numDec);
	}
	
	private double averageSpeed(Trajectory t)
	{
		double speedSum = 0;
		for(int i = 0; i < t.points.size()-1; i++)
		{
			speedSum += instantaneousSpeed(t,i);
		}
		double ret = speedSum/(t.points.size()-1);

		return ret;
	}
	
	private double relaxationTime(Trajectory t, double avgSpeed)
	{
		ArrayList<Double> relTimes = relaxationTimes(t,avgSpeed);
		double relSum = 0;
		for(int i = 0; i < relTimes.size(); i++)
		{
			relSum += relTimes.get(i);
		}
		double ret = relSum/relTimes.size();
		return ret;
	}
	
	private ArrayList<Double> relaxationTimes(Trajectory t, double avgSpeed)
	{
		ArrayList<Double> relTimes = new ArrayList<Double>();
		
		int m = 0;
		while(m < t.points.size()-2)
		{
			int n = m;
			while(!(instantaneousSpeed(t,n) < avgSpeed))
			{
				n++;
				if(n>=t.points.size()-2)
					return relTimes;
			}
			
			int k = 1;
			try
			{
				while(instantaneousSpeed(t,n+k) < avgSpeed)
				{
					k++;
					if(n+k >= t.points.size()-2)
						return relTimes;
				}
			} catch (Exception e) {
				System.out.println(n + ", " + k + ", " + m);
				throw e;
			}
			
			double tau = k*timeStep;
			m = n+k+1;
			
			relTimes.add(tau);
		}
		
		return relTimes;		
	}
	
	private double[] dirChange(Trajectory t)
	{
		ArrayList<Double> dirChanges = new ArrayList<Double>();
		double sum = 0;
		for(int i = 0; i < t.points.size()-2; i++)
		{
			TimePoint2d a = t.points.get(i);
			TimePoint2d b = t.points.get(i+1);
			TimePoint2d c = t.points.get(i+2);
			
			Vector2d cur = new Vector2d(a.x-b.x,a.y-b.y);
			Vector2d next = new Vector2d(b.x-c.x,b.y-c.y);
			double angle = cur.angle(next)*180/Math.PI;
			if(!Double.isNaN(angle))
			{
				sum += angle;
				dirChanges.add(angle);
			}
		}
		double avg = sum/(t.points.size()-2);
		return new double[]{avg,getSD(dirChanges,avg)};
	}
	
	private double[] velChange(Trajectory t)
	{
		ArrayList<Double> velChanges = new ArrayList<Double>();
		double sum = 0;
		for(int i = 0; i < t.points.size()-2;i++)
		{
			double cur = instantaneousSpeed(t,i) - instantaneousSpeed(t,i+1);
			sum += cur;
			velChanges.add(cur);
		}
		double avg = sum/(t.points.size()-2);
		return new double[]{avg,getSD(velChanges,avg)};
	}
	
	private double getSD(ArrayList<Double> vals, double avg)
	{
		double innerSum = 0;
		for(int i = 0; i < vals.size(); i++)
		{
			double temp = vals.get(i)-avg;
			innerSum += temp*temp;
		}
		return Math.sqrt(innerSum/vals.size());
	}
	
	private double instantaneousSpeed(Trajectory t, int i)
	{
		TimePoint2d pCur = t.points.get(i);
		TimePoint2d pNext = t.points.get(i+1);
		return pCur.spatialDist(pNext)/Math.abs(pCur.t - pNext.t);
	}
	
}

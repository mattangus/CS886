package main;

import java.util.ArrayList;
import java.util.List;

import com.sfm.helper.RandomHelper;

public class PayoffCalculator {
	
	ArrayList<Trajectory> trajectories;
	public PayoffCalculator(ArrayList<Trajectory> trajectories)
	{
		this.trajectories = trajectories;
		reducePoints(4);
	}
	
	public void reducePoints(int factor)
	{
		ArrayList<Trajectory> shortened = new ArrayList<Trajectory>();
		int oldSum = 0;
		int newSum = 0;
		for(int i = 0; i < trajectories.size(); i++)
		{
			Trajectory cur = new Trajectory();
			Trajectory old = trajectories.get(i);
			int n = old.points.size();
			int c = factor;
			int N = (int) Math.ceil((double)n/c);
			for(int j = 0; j < n; j+=c)
			{
				TimePoint2d avgPoint = new TimePoint2d(0,0,0);
				for(int k = 0; k < c && k+j < n; k++)
				{
					avgPoint = avgPoint.add(old.points.get(j+k));
				}
				avgPoint = avgPoint.mul(1.0/c);
				cur.add(avgPoint);
			}
			if(cur.points.size() != N)
				System.out.println("uh oh: " + cur.points.size() + " " + N);
			oldSum += n;
			newSum += N;
			//System.out.println("shortening from " + n + " to " + N + " (" + cur.points.size() == N + ")");
			shortened.add(cur);
		}
		System.out.println("old num points: " + oldSum);
		System.out.println("new sum points: " + newSum);
		trajectories = shortened;
	}
	
	public PayoffVector getPayoff(List<Trajectory> trajectories)
	{
		ArrayList<Double> payoffs = new ArrayList<Double>();
		for(int i = 0; i < trajectories.size(); i++)
			payoffs.add(1.0/getMinDist(trajectories.get(i)));
		return new PayoffVector(payoffs);
	}
	
	private double getMinDist(Trajectory t)
	{
		double min = Double.MAX_VALUE;
		for(int i = 0; i < trajectories.size(); i++)
		{
			double dist = t.dist(trajectories.get(i));
			if(min > dist)
				min = dist;
		}
		return min;
	}
}

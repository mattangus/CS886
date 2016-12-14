package main;
import java.util.ArrayList;

public class Trajectory {
	ArrayList<TimePoint2d> points;
	
	public Trajectory(ArrayList<TimePoint2d> points)
	{
		this.points = points;
	}
	
	public Trajectory()
	{
		points = new ArrayList<TimePoint2d>();
	}

	public void add(TimePoint2d p)
	{
		points.add(p);
	}
	
	double dist(Trajectory other)
	{
		int n = points.size();
		int m = other.points.size();
		
		double [][] DTW = new double[n][m];
		for(int i = 0; i < n; i++)
			DTW[i][0] = Double.MAX_VALUE;
		for(int i = 0; i < m; i++)
			DTW[0][i] = Double.MAX_VALUE;
		
		DTW[0][0] = 0;
		
		for(int i = 1; i < n; i++)
		{
			for(int j = 1; j < m; j++)
			{
				double cost = points.get(i).dist(other.points.get(j));
				DTW[i][j] = cost + Math.min(DTW[i-1][j], Math.min(DTW[i][j-1], DTW[i-1][j-1]));
			}
		}
		return DTW[n-1][m-1];
	}
	
	public String toString()
	{
		return points.toString();
	}
}

package main;

public class TimePoint2d {
	double t;
	double x;
	double y;
	
	public double dist(TimePoint2d other)
	{
		double dx = x - other.x;
		double dy = y - other.y;
		double dt = t - other.t;
		
		return Math.sqrt(dx*dx + dy*dy + dt*dt);
	}
}

package main;

public class TimePoint2d {
	double t;
	double x;
	double y;
	
	public TimePoint2d(double x, double y, double t) {
		this.x = x;
		this.y = y;
		this.t = t;
	}

	public double dist(TimePoint2d other)
	{
		double dx = x - other.x;
		double dy = y - other.y;
		double dt = t - other.t;
		
		return Math.sqrt(dx*dx + dy*dy + dt*dt);
	}
	
	public double spatialDist(TimePoint2d other)
	{
		double dx = x - other.x;
		double dy = y - other.y;
		
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public TimePoint2d sub(TimePoint2d other)
	{
		return new TimePoint2d(x-other.x,y-other.y,t-other.t);
	}
	
	public TimePoint2d add(TimePoint2d other)
	{
		return new TimePoint2d(x+other.x,y+other.y,t-other.t);
	}

	public TimePoint2d mul(double d) {
		return new TimePoint2d(x*d,y*d,t*d);
	}
	
	@Override
	public String toString()
	{
		return "(" + x + "," + y + "," + t + ")";
	}
}

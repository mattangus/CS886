package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackParser {
	
	String filename;
	private double timeStep = 0.1;
	private double units = 0.0247; //convert to m
	
	public TrackParser(String file)
	{
		filename = file;
	}
	
	public ArrayList<Trajectory> parse()
	{
		try {
			File f = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(f));
			br.readLine();//num trajectories
			br.readLine();//blank line
			String line;
			ArrayList<Trajectory> ret = new ArrayList<Trajectory>();
			for(int i = 1; (line = br.readLine()) != null; i++)
			{
				if(i % 2 == 0)//ignore stats
				{
					ret.add(parseLine(line));
				}
			}
			br.close();
			return ret;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Trajectory parseLine(String line)
	{
		final String regex = "\\d+ \\d+ \\d+";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(line);
		
		Trajectory t = new Trajectory();
		
		TimePoint2d start = null;
		boolean first = true;
		while (matcher.find()) {
			String[] strPoint = matcher.group(0).split(" ");
			if(first)
			{
				first = false;
				start = new TimePoint2d(Integer.parseInt(strPoint[0])*units,Integer.parseInt(strPoint[1])*units,Integer.parseInt(strPoint[2])*timeStep);
				t.add(new TimePoint2d(0,0,0));
			}
			else
			{
				TimePoint2d p = new TimePoint2d(Integer.parseInt(strPoint[0])*units,Integer.parseInt(strPoint[1])*units,Integer.parseInt(strPoint[2])*timeStep);
				p = p.sub(start);//normalize
				TimePoint2d last; //linearly interpolate if gaps
				while(p.t - (last = t.points.get(t.points.size()-1)).t - 0.1 > 0.00001) //epsilon thresh for error
				{
					double dt = p.t - last.t;
					double newT = last.t + timeStep;
					double newX = (last.x*(newT - last.t) + p.x*(p.t - newT))/dt;
					double newY = (last.y*(newT - last.t) + p.y*(p.t - newT))/dt;
					t.add(new TimePoint2d(newX,newY,newT));
				}
				
				if(p.t - (last = t.points.get(t.points.size()-1)).t < 0.00001) //avoid identical points in time
					t.add(p);
			}
		}
		return t;
	}
	
}

package main;

import java.util.ArrayList;
import java.util.List;

import com.sfm.helper.RandomHelper;

public class PayoffCalculator {
	
	public PayoffCalculator()
	{
		//TODO: load data here
	}
	
	public PayoffVector getPayoff(List<Trajectory> trajectories)
	{
		ArrayList<Double> payoffs = new ArrayList<Double>();
		for(int i = 0; i < trajectories.size(); i++)
			payoffs.add(RandomHelper.nextDouble(-100, 100));
		return new PayoffVector(payoffs);
	}
	
}

package main;
import com.sfm.constants.PedestrianConst;
import com.sfm.helper.RandomHelper;

public class ConstGenerator {
	
	private double[] min;
	private double[] max;
	
	public ConstGenerator(double[] min, double[] max)
	{
		this.min = min;
		this.max = max;
		if(min.length != max.length)
			throw new RuntimeException("min and max must match length");
	}
	/*
	public void modify(PedestrianConst pConst)
	{
		FakeDistribution[] fd = new FakeDistribution[4];
		
		for(int i = 0; i < 4; i++)
		{
			fd[i] = new FakeDistribution();
			for(int j = 0; j < Consts.numPlayers; j++)
			{
				fd[i].add(RandomHelper.nextDouble(min[i], max[i]));
			}
		}
		
		pConst.desiredSpeed = fd[0];
		pConst.accelerationTime = fd[1];
		pConst.evasiveSpeedChange = fd[2];
		pConst.evasiveAngleChange = fd[3];
		
		for(int i = 4; i < min.length; i++)
		{
			assign(i,RandomHelper.nextDouble(min[i], max[i]), pConst);
		}
	}
	
	private void assign(int i, double val, PedestrianConst pConst)
	{
		switch(i)
		{
		case 4: pConst.evasiveRelativeTimeThresh = val; break;
		case 5: pConst.repulsiveStrengthCoefA = val; break;
		case 6: pConst.repulsiveStrengthCoefB = val; break;
		case 7: pConst.crosswalkPullStrengthCoefA = val; break;
		case 8: pConst.crosswalkPullStrengthCoefB = val; break;
		case 9: pConst.crosswalkPushStrengthCoefA = val; break;
		case 10: pConst.crosswalkPushStrengthCoefB = val; break;
		}
	}
	*/
}

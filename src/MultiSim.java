import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sfm.engine.SFMEngine;

public class MultiSim {
	
	public void run()
	{
		int numCores = Runtime.getRuntime().availableProcessors();
		ExecutorService ex = Executors.newFixedThreadPool(numCores);
		
		long start = System.nanoTime();
		for(int i = 0; i < numCores; i++)
		{
			ex.submit(new EngineRunner());
		}
		ex.shutdown();
		
		try { ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); } catch (InterruptedException e) { e.printStackTrace();}
		
		long end = System.nanoTime();
		
		System.out.println("cores: " + numCores + ", time: " + (double)(end-start)*1e-9);
	}
}

import com.sfm.engine.SFMEngine;

public class EngineRunner implements Runnable {

	@Override
	public void run() {
		for(int i = 0; i < 5; i++)
		{
			SFMEngine engine = new SFMEngine(0.05);
			engine.run();
		}
	}

}

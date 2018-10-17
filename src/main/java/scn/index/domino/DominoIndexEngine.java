package scn.index.domino;


import java.util.concurrent.CountDownLatch;

public class DominoIndexEngine {
	
	private static  DominoIndexEngine instance;
	private static int counterDown = 8;
	private DominoIndexEngine()
	{
		init();
	}
	
	
	static public DominoIndexEngine getInstance()
	{
		if(instance == null)
		{
			instance = new DominoIndexEngine();
		} 
		
		
		return instance;
	}
	
	
	private void init()
	{
		
		
	}
	
	
}

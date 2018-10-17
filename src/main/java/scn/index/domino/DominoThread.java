package scn.index.domino;

import lotus.domino.NotesThread;
import lotus.domino.Session;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ArrayBlockingQueue;

abstract public class DominoThread extends NotesThread{

	public boolean  _interupted=false;
	public boolean _finished = false;
	private ThreadLocal<DominoUtil> dominoUtil;
	private ThreadLocal<Session> session;
	
	
	public DominoThread()
	{
		
	}
	
	public void runNotes()
	{
		try {
			startup();
		
			while(!_interupted||!_finished)
			{
				runTask();
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			
			shutdown();
		}
		
	}

	
	abstract public void startup();
	
	abstract public void shutdown();
	public void interupt() 
	{
		_interupted = true;
		Thread.currentThread().interrupt();
	}
	
	public void finish()
	{
		_finished = true;
	}
	
	abstract public void runTask() ;
}

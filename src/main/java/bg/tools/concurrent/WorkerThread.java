package bg.tools.concurrent;

import bg.tools.GlobalConstants;
import bg.tools.Logger;

public class WorkerThread extends Thread {
	
	public WorkerThread()
	{
		super.setName(MultiTaskMgr.getInstance(GlobalConstants.G_INIT_WORKERS).getThreadPRE());
	}
	//ThreadLocal<Boolean> inRunning = new  ThreadLocal<Boolean>();
	Boolean inRunning =false;
	public Boolean inRunning()
	{
		if(inRunning ==null)
		{
			Logger.logError("inRunning not exit");
		}
		return inRunning;
	}
	public void run()
	{
		Logger.logInfo("worker start and try fetch task");
		Task t  = null;
		
		try {
			while(true) {
			t = MultiTaskMgr.getInstance(GlobalConstants.G_INIT_WORKERS).fetch();
			inRunning = true;
			t.run();
			inRunning = false;
			}
		} catch (InterruptedException e) {
			Logger.logError("Interrupted by user");
		}
		catch(Exception e)
		{
			Logger.logError("Unhandle exception:"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(t!=null) t.clear(); 
			Logger.logInfo("exit worker");
		}
	}
}

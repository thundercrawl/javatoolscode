package bg.tools.concurrent;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import bg.tools.Logger;

public class MultiTaskMgr {

	private static int THREAD_MAX=500;
	private static int THREAD_INIT = 10;
	private static ArrayList<WorkerThread> mgrThreads = new ArrayList<WorkerThread>();
	private static MultiTaskMgr mgr;
	private static BlockingQueue<Task> queueTask = new ArrayBlockingQueue<Task>(THREAD_MAX);
	private static String THREAD_PRE = "MULTI_";
	private static int threadindx = 1;
	private static Integer _num = 0;
	
	public String getThreadPRE()
	{
		return THREAD_PRE+threadindx++;
	}
	/*
	 * Define the spawn thread in running, that descide the throughput in the engine
	 * Num: default is 10
	 */
	public static MultiTaskMgr getInstance(Integer num)
	{
		if(mgr==null)
		{
			if(num!=null&& num>0)
				mgr = new MultiTaskMgr(num);
			else
				mgr = new MultiTaskMgr(THREAD_INIT);
			spawnWorker(_num);
		}
		return mgr;
	}
	private MultiTaskMgr(Integer num)
	{	
		MultiTaskMgr._num = num;
	}
	
	private static void spawnWorker(Integer num)
	{
		int init=0;
		if(num!=0)
			init = num;
		else
		init= THREAD_INIT;
		
		while(init-->0)
		{
			WorkerThread t = new WorkerThread();
			mgrThreads.add(t);
			t.start();
		}
	}
	public MultiTaskMgr submitTask(Task t)
	{
		
		queueTask.add(t);
		
		return mgr;
		
	}
	
	public Task fetch() throws InterruptedException
	{
		return queueTask.take();
	}
	
	public void shutdown()
	{
		for(Thread t:mgrThreads)
			t.interrupt();
	}
	
	public void waitForExit()
	{
		while(true)
		{
			int count = 0;
			if(queueTask.size() == 0)
			{
				for(WorkerThread t:mgrThreads)
				{
					if(t==null) Logger.logError("Thread object not valid");
					if(t.inRunning()) break;
					count++;
				}
				if(count == mgrThreads.size() )
				{
					for(WorkerThread t:mgrThreads)
					{
						t.interrupt();
					}
					return;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Logger.logInfo("broke sleep by interrup");
			}
		}
	}
}

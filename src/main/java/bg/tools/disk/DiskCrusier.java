package bg.tools.disk;

import bg.tools.Logger;
import bg.tools.concurrent.MultiTaskMgr;

public class DiskCrusier {

	public static void main(String[] args) {
		Integer  tunnel = 5;
		if(args.length == 0)
		{
			Logger.logInfo("not set default tunnel to execute ,default is 5");
		}
		else
		{
			Logger.logInfo("set tunnel to "+args[0]);
			tunnel = new Integer(args[0]);
		}
		MultiTaskMgr mgr=	MultiTaskMgr.getInstance(tunnel);
		mgr.submitTask(new DiskWorker()).submitTask(new DiskWorker());
		mgr.waitForExit();

	}

}

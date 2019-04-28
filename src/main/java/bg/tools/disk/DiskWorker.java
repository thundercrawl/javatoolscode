package bg.tools.disk;

import bg.tools.Logger;
import bg.tools.concurrent.Task;

public class DiskWorker implements Task {

	public void run() {
		Logger.logInfo("running diskworker task");
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.logInfo("finish diskworker task");
	}

	public void clear() {
		Logger.logInfo("clear diskworker");
	}

}

package scn.index.LoggerUtil;

public class CommonLogger {

	private static CommonLogger log;
	public static  synchronized void consolePrint(String msg)
	{
		System.out.println("[ "+Thread.currentThread().getName()+" ]"+msg);
	
	}
	
}

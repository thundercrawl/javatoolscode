package bg.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static String datepattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static SimpleDateFormat datefmt= new SimpleDateFormat(datepattern);
	private static String  getLoggerTime()
	{
		String dstr = "";
		dstr = datefmt.format(new Date());
		return dstr;
	}
	public synchronized static void logInfo(String msg)
	{
		System.out.println("[INFO] "+getLoggerTime()+" [ "+Thread.currentThread().getName()+" ]"+msg);
	}
	
	public synchronized static void logError(String msg)
	{
		System.out.println("[ERROR] "+getLoggerTime()+" [ "+Thread.currentThread().getName()+" ]"+msg);
	}
}

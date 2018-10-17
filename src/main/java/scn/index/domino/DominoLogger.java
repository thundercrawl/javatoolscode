package scn.index.domino;

import lotus.domino.Log;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;

public class DominoLogger {

	private static Session session;
	private static Log log;
	public static  synchronized void consolePrint(String msg)
	{
		if(session == null)
		{
			try {
				session = NotesFactory.createSession();
				Log log = session.createLog("populator");
			} catch (NotesException e) {
				
				e.printStackTrace();
			}
		}
		
		try {
			log.logAction(msg);
		} catch (NotesException e) {
			
			e.printStackTrace();
		}
	}
	
	public void finalize()
	{
		System.out.println("clear Domino logger session info");
		try {
		session.recycle();
		}
		catch(NotesException e)
		{
			System.out.println("exception when close logger session"+e.getMessage());
			e.printStackTrace();
		}
	}
}

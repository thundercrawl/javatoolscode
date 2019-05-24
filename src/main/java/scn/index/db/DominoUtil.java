package scn.index.db;

import lotus.domino.Database;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;

public class DominoUtil {

	
	
	private Session session;
	private DominoUtil(Session session)
	{
		this.session = session;
	}
	
	public void listdocuments(String dbpath)
	{
	
		try
		{
			
			
			DbDirectory dir = session.getDbDirectory(null);
			Database db = dir.openDatabase(dbpath);
			if(db == null)
			{
				DominoLogger.consolePrint("error to open db "+ dbpath);
				return;
			}
			DocumentCollection dc = db.getAllDocuments();
		      Document doc = dc.getFirstDocument();
		      while (doc != null) {
		    	  
		    	  DominoLogger.consolePrint(
		          doc.getItemValueString("Subject")+" id="+doc.getNoteID());
		    	  
		        doc = dc.getNextDocument();
		      }
		}  catch (NotesException e)  {
			DominoLogger.consolePrint(" - Notes Error getting Notes version: " + e.id + " " + e.text);
			
		}  catch (Exception e)  {
			e.printStackTrace();
			DominoLogger.consolePrint(" - Java Error getting Notes version: " + e.getMessage());
			
		}
		
		
	}
	
}

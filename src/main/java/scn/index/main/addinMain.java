package scn.index.main;
//make sure Notes.jar is in your ClassPath
import lotus.domino.*;
import lotus.notes.addins.JavaServerAddin;
import lotus.notes.internal.MessageQueue;
import scn.index.amq.App;
import scn.index.amq.Constants;
import scn.index.amq.MessageHandler;
import scn.index.amq.amqConsumer;
import scn.index.amq.amqProducer;
import scn.index.solr.PayLoad;
import scn.index.solr.SolrAPI;
import scn.index.domino.DominoProperties;

import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;



public class addinMain extends JavaServerAddin implements MessageHandler {
	
	/* global variables */
	// the name of this program
	String progName = new String("DominoIndexer");
	
	// the "friendly" name for this Addin
	String addinName = new String("Java Addin Test");
	
	
	public static final int MQ_MAX_MSGSIZE = 256;
	// this is already defined (should be = 1):
	public static final int	MQ_WAIT_FOR_MSG = MessageQueue.MQ_WAIT_FOR_MSG;
	
	// MessageQueue errors:
	public static final int PKG_MISC = 0x0400;
	public static final int ERR_MQ_POOLFULL = PKG_MISC+94;
	public static final int ERR_MQ_TIMEOUT = PKG_MISC+95;
	public static final int ERR_MQSCAN_ABORT = PKG_MISC+96;
	public static final int ERR_DUPLICATE_MQ = PKG_MISC+97;
	public static final int ERR_NO_SUCH_MQ = PKG_MISC+98;
	public static final int ERR_MQ_EXCEEDED_QUOTA = PKG_MISC+99;
	public static final int ERR_MQ_EMPTY = PKG_MISC+100;
	public static final int ERR_MQ_BFR_TOO_SMALL = PKG_MISC+101;
	public static final int ERR_MQ_QUITTING = PKG_MISC+102;

	
	/* the main method, which just kicks off the runNotes method */
	public static void main (String[] args)
	{
		// kick off the Addin from main -- you can also do something
		// here with optional program args, if you want to...
		addinMain addinTest = new addinMain();
		addinTest.addinMain();
	}
	
	
	/* some class constructors */
	public void addinMain ()
	{
		
	}
	
	public void addinMain (String[] args)
	{
		setName(progName);
		// do something with the args that were passed...
		if (args.length > 0)
		{
			// whatever...
		}
	}
	
	
	/* the runNotes method, which is the main loop of the Addin */
	public void runNotes ()
	{
		int taskID;
		MessageQueue mq;
		StringBuffer qBuffer = new StringBuffer();
		int mqError;
		
		taskID = AddInCreateStatusLine(addinName);
		
		AddInSetStatusLine(taskID, "Initialization in progress...");
		displayNotesVersion();
		consolePrint("10001");
		//listDocuments("c:\\domino32\\data\\mail\\admin.nsf");
		initAMQConsumers();
		
		while (addInRunning())
		{
			
			
			OSPreemptOccasionally();
			
			
		}
	}
	
	public void initAMQConsumers()
	{
		Session session = null;
		boolean durable=false;
		try
		{
			/* older code nothing at all
			session = NotesFactory.createSession();
			
			String result = session.sendConsoleCommand(session.getServerName(), "show config *");
			consolePrint("show results:"+result);
			
			if(result.contains("AQMDURABLE=true"))
			{
				durable=true;
				consolePrint("amq durable feature set");
			}
			else
				consolePrint("amq durable feature disable");
				*/
			
			durable = DominoProperties.getProperties().AMQDURABLE;
			if(durable)
			{
				durable=true;
				consolePrint("amq durable feature set");
			}
			else
				consolePrint("amq durable feature disable");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		amqProducer producer = new amqProducer();
        producer.init();
        amqConsumer consumer = new amqConsumer(durable);
        consumer.init();
        consumer.setMessageHandler(this);
        ExecutorService executor = Executors.newFixedThreadPool(6);
    
    	consolePrint("--consumer register for  ADD/DELETE/CREATE/MODIFY --");
       /*
    	executor.execute(new App().new ConsumerMq(consumer,Constants.addSelector) );
        executor.execute(new App().new ConsumerMq(consumer,Constants.createSelector) );
        executor.execute(new App().new ConsumerMq(consumer,Constants.addSelector) );
        executor.execute(new App().new ConsumerMq(consumer,Constants.deleteSelector) );
        executor.execute(new App().new ConsumerMq(consumer,Constants.unknowTypeSelector) );
        */
        executor.execute(new App().new ConsumerMq(consumer,""));

		// keep the memory clean
		try 
		{
			//session.recycle();
		}  catch (Exception e)  { }
		
        
	}
	public void insertDocument(PayLoad pl)
	{
		String notesid;
		if(pl == null || pl.noteid== "")
		{
			consolePrint("error notes id, null or empty");
			return;
		}
		else
		{
			notesid=Integer.toHexString(new Integer(pl.noteid));
			consolePrint("insert document id(hex)="+ Integer.toHexString(new Integer(pl.noteid)));
		}
		Session session = null;
		try
		{
		
			session= NotesFactory.createSession();
		DbDirectory dir = session.getDbDirectory(null);
		Database db = dir.openDatabase(pl.dbpath);
		if(db == null)
		{
			consolePrint("error to open db "+ pl.dbpath);
			return;
		}
		
		Document doc= db.getDocumentByID(notesid);
		ArrayList<String> fieldList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		if(doc ==null)
		{
			consolePrint("doc not exit, id="+pl.noteid);
			return;
		}
		else
		{
			String subject = doc.getItemValueString("Subject");
			consolePrint("doc subject="+subject);
		}
		Enumeration items = doc.getItems().elements();
	      while (items.hasMoreElements()) {
	       
	        Item item = (Item)items.nextElement();
	        System.out.println("Name: " + item.getName());
	    	
			fieldList.add(item.getName());
			valueList.add(item.getValueString());
	        String type = "Undefined";
	        switch (item.getType()) {
	          case Item.ATTACHMENT :
	            type = "Attachment"; break;
	          case Item.EMBEDDEDOBJECT :
	            type = "Embedded object"; break;
	          case Item.ERRORITEM :
	            type = "Error item"; break;
	          case Item.NAMES :
	            type = "Names"; break;
	          case Item.AUTHORS :
	            type = "Authors"; break;
	          case Item.READERS :
	            type = "Readers"; break;
	          case Item.NOTELINKS :
	            type = "Note links"; break;
	          case Item.NOTEREFS :
	            type = "Note references"; break;
	          case Item.NUMBERS :
	            type = "Numbers"; break;
	          case Item.RICHTEXT :
	            type = "Rich text"; break;
	          case Item.TEXT :
	            type = "Text"; break;
	          case Item.SIGNATURE :
	            type = "Signature"; break;
	          case Item.DATETIMES :
	            type = "Date-times"; break;
	          case Item.UNAVAILABLE :
	            type = "Unavailable"; break;
	          case Item.UNKNOWN :
	            type = "Unknown"; break;
	          case Item.USERDATA :
	            type = "User data"; break;
	          case Item.USERID :
	            type = "User ID"; break;
	          }
	        System.out.println("Type: " + type);
	        if (item.isEncrypted())
	          System.out.println("Is encrypted");
	        if (item.isSigned())
	          System.out.println("Is signed");
	        if (item.isSummary())
	          System.out.println("Is summary");
	        if (item.isProtected())
	          System.out.println("Is protected");
	        System.out.println("Text:\n" + item.getText());
	        }
	      final String url = "http://localhost:8983/solr/test";
	      SolrAPI api = new SolrAPI(url);
	      
	      api.commitData(  fieldList.toArray(), valueList.toArray());
	      consolePrint("finish insert documents to solr");
		
		}  catch (NotesException e)  {
			consolePrint(progName + " - Notes Error getting Notes version: " + e.id + " " + e.text);
			
		}  catch (Exception e)  {
			e.printStackTrace();
			consolePrint(progName + " - Java Error getting Notes version: " + e.getMessage());
			
		}
		
		// keep the memory clean
		try 
		{
			session.recycle();
		}  catch (Exception e)  { }
		
		
	}
	
	/* the consolePrint method, which is a tiny wrapper around the
	   AddInLogMessageText method (because AddInLogMessageText requires
	   a second parameter of 0, and I always forget to type it) */
	private void consolePrint (String msg)
	{
		AddInLogMessageText(msg, 0);
	}
	
	
	
	/* the doCleanUp method, which performs all the tasks we should do when
	   the Addin terminates */
	public void doCleanUp (int taskID, MessageQueue mq)
	{
		try
		{
			
			mq.close(0);
			consolePrint(addinName + " has terminated.");
		}  catch (Exception e)  { }
		
	}
	
	
	/* the processMsg method, which translates and reacts to user commands,
	   like "TELL JavaAddinTest THIS THAT" (where "THIS" and "THAT" are the
	   messages we'll see in the queue) */
	private int processMsg (StringBuffer qBuffer)
	{
		StringTokenizer st;
		String token;
		int tokenCount;
		
		st = new StringTokenizer(qBuffer.toString());
		tokenCount = st.countTokens();
		
		// do a quick error check
		if (tokenCount == 0)
		{
			displayHelp();
			return -1;
		}

		// get the first token, and check it against our known list of arguments
		token = st.nextToken();
		
		// ? or HELP should display a help screen
		if ((token.equalsIgnoreCase("?")) || (token.equalsIgnoreCase("HELP")))
		{
			displayHelp();
			return 0;
		}
		
		// VER should display the version of Notes we're running
		if (token.equalsIgnoreCase("VER"))
		{
			return displayNotesVersion();
		}
		
		// DBSIZE <dbname> should display the size of a particular database
		if (token.equalsIgnoreCase("DBSIZE"))
		{
			token = st.nextToken();
			return displayDbSize(token);
		}
		
		// QUIT and EXIT will stop the Addin
		if ((token.equalsIgnoreCase("QUIT")) || (token.equalsIgnoreCase("EXIT")))
		{
			// automatically handled by the system
			return 0;
		}

		// if we got here, the user gave us an unknown argument, so we should
		// just display the help screen
		consolePrint("Unknown argument for " + addinName + ": " + token);
		displayHelp();
		return -1;
		
	}
	
	
	/* the displayHelp method simply shows a little Help screen on the console */
	private void displayHelp ()
	{
		consolePrint(addinName + " Usage:");
		consolePrint("Tell " + progName + " HELP  -- displays this help screen");
		consolePrint("Tell " + progName + " VER  -- displays the Notes version of this server");
		consolePrint("Tell " + progName + " DBSIZE <dbname>  -- displays the size of a given database");
		consolePrint("Tell " + progName + " QUIT  -- terminates this addin");
	}
	
	
	/* the displayNotesVersion method, which just prints the Notes version
	   that we're running */
	private int displayNotesVersion ()
	{
		int retVal = 0;
		Session session = null;
		
		try
		{
			session = NotesFactory.createSession();
			String ver = session.getNotesVersion();
			consolePrint(progName + " - Domino version: " + ver);
		}  catch (NotesException e)  {
			consolePrint(progName + " - Notes Error getting Notes version: " + e.id + " " + e.text);
			retVal = -1;
		}  catch (Exception e)  {
			consolePrint(progName + " - Java Error getting Notes version: " + e.getMessage());
			retVal = -1;
		}
		
		// keep the memory clean
		try 
		{
			session.recycle();
		}  catch (Exception e)  { }
		
		return retVal;
	}
	
	
	/* the displayDbSize method, which simply opens a database and displays
	   its size */
	private int displayDbSize (String dbName)
	{
		int retVal = 0;
		Session session = null;
		Database db = null;
		
		try
		{
			session = NotesFactory.createSession();
			db = session.getDatabase(null, dbName);
			if (!db.isOpen()) {
				consolePrint(progName + " - Database " + dbName + " not found or is not accessible");
				retVal = -1;
			} else {
				double dbSize = db.getSize();
				consolePrint(progName + " - Database " + dbName + " is " + (int)dbSize + " bytes");
			}
		}  catch (NotesException e)  {
			consolePrint(progName + " - Notes Error getting database size: " + e.id + " " + e.text);
			retVal = -1;
		}  catch (Exception e)  {
			consolePrint(progName + " - Java Error getting database size: " + e.getMessage());
			retVal = -1;
		}
		
		// keep the memory clean
		try 
		{
			if (db != null)
				db.recycle();
			session.recycle();
		}  catch (Exception e)  { }
		
		return retVal;		
	}


	@Override
	public void onMessageHandler(String message) {
		String id = null;
		Gson gs = new Gson();
		JsonReader reader = new JsonReader(new StringReader(message));
		reader.setLenient(true);
		message=message.replace('\\', '/');
		consolePrint("replaced string as:"+message);
		PayLoad pl = (PayLoad) gs.fromJson(message, PayLoad.class);
		
		insertDocument(pl);
	}

}

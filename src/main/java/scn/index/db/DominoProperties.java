package scn.index.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DominoProperties {

	
		final String AMQUTIL_NAME="AMQDURABLE";
		public  boolean AMQDURABLE = false;
	   private DominoProperties()
	   {
	   }

	   private DominoProperties(Properties prop) throws Exception
	   {

	      if (prop.get(AMQUTIL_NAME) == null )
	    	  {
	    	  System.out.println(prop.toString());
	    	  throw new Exception(
	    	  
	                  "failed to get the AMQUtilName");
	    	  }
	      
	      AMQDURABLE = Boolean.parseBoolean(prop.getProperty(AMQUTIL_NAME));
	   }

	   public static DominoProperties getProperties()
	   {
	      Properties prop = new Properties();
	      InputStream input = null;

	      try
	      {

	         input = new FileInputStream("notes.ini");

	         // load a properties file
	         prop.load(input);

	         DominoProperties ap = new DominoProperties(prop);
	         return ap;

	      }
	      catch (IOException ex)
	      {
	         ex.printStackTrace();
	        
	      }
	      catch (Exception e)
	      {
	        System.out.println(e.getMessage());

	      }
	      finally
	      {
	         if (input != null)
	         {
	            try
	            {
	               input.close();
	            }
	            catch (IOException e)
	            {
	               e.printStackTrace();
	            }
	         }
	      }
	      return null;
	   }

	

}

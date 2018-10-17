package scntest.ps;

import com.google.gson.Gson;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import scn.index.solr.PayLoad;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testJson()
    {
    	//String json ="{\"dbpath\":\"mail\\admin\\.nsf\",\"eventtype\":2,\"noteid\":-2147483647,\"noteunid\":\"3a5aa5497596846c48258298003656f7\",\"folderid\":0}";
    	String json = "{\"dbpath\":\"mail/admin.nsf\"}";
    	String json1 =" {\"dbpath\":\"Rover\",\"doors\":5}";
    	Gson gs = new Gson();
    	System.out.println(json);
    	PayLoad pl = gs.fromJson(json, PayLoad.class);
    	System.out.println(pl.dbpath);
    	
    }
    
   
}

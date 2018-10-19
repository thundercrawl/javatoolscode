package scntest.ps;

import com.google.gson.Gson;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import scn.index.ES.ElasticSearchClient;
import scn.index.LoggerUtil.CommonLogger;
import scn.index.solr.PayLoad;
import scn.index.status.*;

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

    public void testES()
    {
        CommonLogger.consolePrint("Create elastic search client test ----------->");
        ElasticSearchClient client = new ElasticSearchClient("47.105.127.77",9200);
        String mapping = "{\r\n    \"bookContents\": {\r\n              \"properties\": {\r\n            \"content\": {\r\n                \"type\": \"text\",\r\n                \"store\": \"true\",\r\n                \"term_vector\": \"with_positions_offsets\",\r\n                \"analyzer\": \"ik_max_word\",\r\n                \"search_analyzer\": \"ik_max_word\",\r\n                        \"boost\": 8\r\n            }\r\n        }\r\n    }\r\n}";
        CommonStatus status = client.CreateIndex("books_cn",mapping,"bookContents");
        CommonLogger.consolePrint(status.getMessage());
        client.CreateDocument();
    }
    
   
}

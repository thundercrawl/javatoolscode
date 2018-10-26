package scntest.ps;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public void buildTestIndex()
    {
        CommonLogger.consolePrint("Create elastic search client test ----------->");
        ElasticSearchClient client = new ElasticSearchClient("47.105.127.77",9200);
        String mapping = "{\r\n    \"bookContents\": {\r\n              \"properties\": {\r\n            \"content\": {\r\n                \"type\": \"text\",\r\n                \"store\": \"true\",\r\n                \"term_vector\": \"with_positions_offsets\",\r\n                \"analyzer\": \"ik_max_word\",\r\n                \"search_analyzer\": \"ik_max_word\",\r\n                        \"boost\": 8\r\n            }\r\n        }\r\n    }\r\n}";
        CommonStatus status = client.CreateIndex("books_cn",mapping,"bookContents");
        CommonLogger.consolePrint(status.getMessage());


        Map<String, Object> jsonMap = new HashMap<>();
       

        try {
            File file = new File("c:/tmp/all.txt");
            if(file.isFile() && file.exists()) {
              InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
              BufferedReader br = new BufferedReader(isr);
              String lineTxt = null;
              while ((lineTxt = br.readLine()) != null) {
                jsonMap.put("user", "kimmy");
                jsonMap.put("postDate", new Date());
                jsonMap.put("bookContents", lineTxt);
                jsonMap.put("bookName","浪子回头");
                client.CreateDocument("books_cn","bookContents",jsonMap);
                jsonMap.clear();
              }
              br.close();
            } else {
              System.out.println("文件不存在!");
            }
          } catch (Exception e) {
            System.out.println("文件读取错误!");
          }
        
    }
    
   
}
 
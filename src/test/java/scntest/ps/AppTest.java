package scntest.ps;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;

import scn.index.ES.ElasticSearchClient;
import scn.index.LoggerUtil.CommonLogger;
import scn.index.solr.PayLoad;
import scn.index.status.CommonStatus;


/**
 * Unit test for simple App.
 */
public class AppTest 
    
{


    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp()
    {
        assertTrue( true );
    }
    @Test
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
@Test
    public void buildTestIndex()
    {
        CommonLogger.consolePrint("Create elastic search client test ----------->");
        ElasticSearchClient client  =   Mockito.mock(ElasticSearchClient.class);
       // ElasticSearchClient client = new ElasticSearchClient("localhost",9200);
        String mapping = "{\r\n    \"bookContents\": {\r\n              \"properties\": {\r\n            \"content\": {\r\n                \"type\": \"text\",\r\n                \"store\": \"true\",\r\n                \"term_vector\": \"with_positions_offsets\",\r\n                \"analyzer\": \"ik_max_word\",\r\n                \"search_analyzer\": \"ik_max_word\",\r\n                        \"boost\": 8\r\n            }\r\n        }\r\n    }\r\n}";
        //CommonStatus status = client.CreateIndex("books_cn",mapping,"bookContents");
       // Mockito.verify(client).CreateIndex("books_cn", mapping, "bookContents");
        CommonStatus rt = new CommonStatus();
        rt.setStatusCode(200);
        rt.setMessage("Get User principal");
        when(client.CreateIndex("books_cn", mapping, "bookContents")).thenReturn(rt);
        when(client.isOnline()).thenReturn(true);
        assertEquals(client.isOnline(),true);
        assertEquals(client.CreateIndex("books_cn", mapping, "bookContents").getStatusCode(),200);
         CommonLogger.consolePrint(client.CreateIndex("books_cn", mapping, "bookContents").getMessage());


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
 
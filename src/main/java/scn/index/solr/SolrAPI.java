package scn.index.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.Create;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrAPI {
	
	private final String  serverurl;
	
	public SolrAPI(String url)
	{
		serverurl = url;
	}
	
	
	public void changeSchema(String fieldName,String type)
	{
		
		SolrClient client = new HttpSolrClient.Builder(serverurl).build();
		
	}
	public void commitData(Object[] fields,Object[] values)
	{
		 SolrClient client = new HttpSolrClient.Builder(serverurl).build();
		   
		      SolrInputDocument doc = new SolrInputDocument();
		     
		      for(int i=0;i< fields.length;i++)
		      {
		    	  doc.addField((String)fields[i], (String)values[i]);
		      }
		      try {
				client.add(doc);
				client.commit();
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		    }
	
	
	public void searchSingle(String searchcontents,String searchfield, String... rtfields)
	{
		SolrClient client = new HttpSolrClient.Builder(serverurl).build();
		
		
        SolrQuery query = new SolrQuery();
        query.setQuery(searchfield+":"+searchcontents);
        
        query.setFields(rtfields);
        query.setStart(0);
        //query.set("defType", "edismax");

        QueryResponse response;
		try {
			response = client.query(query);
			 SolrDocumentList results = response.getResults();
			 System.out.println("get results size:="+results.size());
		        for (int i = 0; i < results.size(); ++i) {
		            System.out.println(results.get(i));
		        }
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
		   
	
}

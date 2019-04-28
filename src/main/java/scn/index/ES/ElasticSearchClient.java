package scn.index.ES;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;

import scn.index.status.CommonStatus;

public class ElasticSearchClient implements Serializable{

        private String host;
        private int port;
        private RestHighLevelClient client;
        public ElasticSearchClient(String host,int port)
        {
                this.host = host;
                this.port = port;
                client= new RestHighLevelClient(
                        RestClient.builder(
                                new HttpHost(host, port, "http")).setMaxRetryTimeoutMillis(9000*1000));
        
        }
        
        public boolean isOnline()
        {
        	return false;
        }
	/*
	 * Hiding all setting for shard and replica
	 * shard and replica should distribute on the backend cluster by ES methodology
	 */
	public  CommonStatus CreateIndex(String idxName,String mapping,String type)
	{
                CommonStatus status = new CommonStatus();
                
              
                CreateIndexRequest request = new CreateIndexRequest(idxName);//创建索引
                //创建的每个索引都可以有与之关联的特定设置。
                request.settings(Settings.builder()
                        .put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 1)
                );
                request.mapping(type, mapping,XContentType.JSON);
                /*
                //创建索引时创建文档类型映射
                request.mapping("tweet",//类型定义
                        "  {\n" +
                                "    \"tweet\": {\n" +
                                "      \"properties\": {\n" +
                                "        \"message\": {\n" +
                                "          \"type\": \"text\"\n" +
                                "        }\n" +
                                "      }\n" +
                                "    }\n" +
                                "  }",//类型映射，需要的是一个JSON字符串
                        XContentType.JSON);
		*/
                //为索引设置一个别名
                request.alias(
                        new Alias("books-alias")
                );
                //可选参数
                request.timeout(TimeValue.timeValueMinutes(2));//超时,等待所有节点被确认(使用TimeValue方式)
                //request.timeout("2m");//超时,等待所有节点被确认(使用字符串方式)
                
                request.masterNodeTimeout(TimeValue.timeValueMinutes(1));//连接master节点的超时时间(使用TimeValue方式)
                //request.masterNodeTimeout("1m");//连接master节点的超时时间(使用字符串方式)

                request.waitForActiveShards(1);//在创建索引API返回响应之前等待的活动分片副本的数量，以int形式表示。
                //request.waitForActiveShards(ActiveShardCount.DEFAULT);//在创建索引API返回响应之前等待的活动分片副本的数量，以ActiveShardCount形式表示。

                //同步执行

                final RequestOptions COMMON_OPTIONS;
                RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
                
                
                COMMON_OPTIONS = builder.build();
                CreateIndexResponse createIndexResponse = null;
                
        try {
                createIndexResponse = client.indices().create(request,COMMON_OPTIONS);
                        
			
		} catch (IOException e) {
                        e.printStackTrace();
			
                }
                catch(ElasticsearchStatusException e)
                {
                        if(e.getMessage().indexOf("exists")==-1)
			{
                                e.printStackTrace();
                                status.setMessage("Error:"+e.getMessage());
                                status.setStatusCode(400);
                        }else
                        {
                                status.setMessage("Success:"+"already created");
                                status.setStatusCode(201);
                        }
                       
                }
                finally{
                        if(createIndexResponse!=null&&createIndexResponse.isAcknowledged())
                        {
                                status.setMessage("Success:index created");
                                status.setStatusCode(200);
                        }
                        
                }
		
		return status;
        }
        


        public CommonStatus CreateDocument(String idx,String type,Map<String, Object> jsonContent)
        {
                CommonStatus status = new CommonStatus();

                IndexRequest request = new IndexRequest(
                idx, 
                type);  
        
                request.source(jsonContent, XContentType.JSON); 

                try {
                       IndexResponse rep = client.index(request, RequestOptions.DEFAULT);
                       RestStatus st = rep.status();
                       status.setStatusCode(st.getStatus());
                       
                } catch (IOException e) {
                       
                        e.printStackTrace();
                }
                return  status;
        }

        public CommonStatus searchIndex(String searchText)
        {
                CommonStatus status = new CommonStatus();
                
                
                return status;
        }


}

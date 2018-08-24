package cn.e3mall.solrj;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {

	@Test
	public void testAddDocument() throws Exception, IOException{
		//创建一个集群的连接，应该使用cloudSolrServer创建
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183");
        //zkHost:zookeeper的地址列表
		//设置一个defaultCollection属性
		cloudSolrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		//向文档中添加域
		 solrInputDocument.setField("id","solrcloud01");
		 solrInputDocument.setField("item_title","测试商品01");
		 solrInputDocument.setField("item_price",123);
		 //把文件写入索引库
		 cloudSolrServer.add(solrInputDocument);
		 //提交
		 cloudSolrServer.commit();
		 
	
	
	
	}
	
	@Test
	public void testDeleteDocument() throws Exception, IOException{
		//创建一个集群的连接，应该使用cloudSolrServer创建
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183");
        //zkHost:zookeeper的地址列表
		//设置一个defaultCollection属性
		cloudSolrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		//向文档中添加域
		 solrInputDocument.setField("id","solrcloud01");
		 solrInputDocument.setField("item_title","测试商品01");
		 solrInputDocument.setField("item_price",123);
		 //把文件写入索引库
		 cloudSolrServer.deleteByQuery("id:solrcloud01");
		 //提交
		 cloudSolrServer.commit();
		 
	
	
	
	}
	
	@Test
	public void testQueryDocument() throws Exception{
		//创建一个CloudSolrServer对象
	CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183");
    //设置默认的Collection
	cloudSolrServer.setDefaultCollection("collection2");
	//创建一个查询对象
	SolrQuery solrQuery = new SolrQuery();
	//创建一个对象查询
	solrQuery.setQuery("*:*");
	//执行查询
	QueryResponse queryResponse = cloudSolrServer.query(solrQuery);
	//取得查询结果
	 SolrDocumentList results = queryResponse.getResults();
	 System.out.println("总纪律数:"+results.getNumFound());
	 
	 //打印
	 for(SolrDocument solrDocument:results){
		 System.out.println(solrDocument.get("id"));
		 System.out.println(solrDocument.get("title"));
		 System.out.println(solrDocument.get("item_title"));
		 System.out.println(solrDocument.get("item_price"));
	 }
	 
	 
	
	
	
		
	}
}

    

package cn.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {

	@Test
	public void addDocument() throws Exception {
		//创建一个SolrServer对象，创建一个连接。参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.163:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域。文档中必须包含一个id域，所有的域的名称必须在schema.xml中定义。
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void deleteDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.163:8080/solr/collection1");
		//删除文档
		//solrServer.deleteById("doc01");
		solrServer.deleteByQuery("id:doc01");
		//提交
		solrServer.commit();
	}
	
	@Test
	public void queryIndex()throws Exception{
		//创建一个SolrServer对象，创建一个连接。参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://182.61.12.183:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.set("q","*:*");
		//执行查询，QueryResponse对象
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//取文档列表。取查询结果的总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询结果的总记录数："+solrDocumentList.getNumFound());
		
		//遍历文档列表，取域中内容
		for(SolrDocument solrDocument:solrDocumentList){
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}
	
	@Test
	public void queryIndexFuza() throws Exception{
		//创建一个SolrServer对象，创建一个连接。参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://182.61.12.183:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//查询条件
		solrQuery.setQuery("手机");
		solrQuery.setStart(0);
		solrQuery.setRows(20);
		solrQuery.set("df","item_title");
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePost("</em>");
		solrQuery.setHighlightSimplePre("<em>");
		//执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//取文档列表，取查询结果的总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
		
		//遍历文档列表，取出域内容
		Map<String,Map<String,List<String>>> highlighting = queryResponse.getHighlighting();
		
		for(SolrDocument solrDocument:solrDocumentList){
			System.out.println(solrDocument.get("id"));
			//去高亮显示
		   List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
		   
		   String title="";
		   if(list!=null&&list.size()>0){
			   title=list.get(0);
		   }else{
			   
			   title=(String) solrDocument.get("item_title");
		   }
		   System.out.println(title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
		
	}
	
	
	
	
	
	
}

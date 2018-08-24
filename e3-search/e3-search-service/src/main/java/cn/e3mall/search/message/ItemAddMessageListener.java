package cn.e3mall.search.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

/**
 * 监听商品添加事件，报道消息后，将对应的商品信息同步到索引库
 * @author 孔旻
 *
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer slorServer;
	@Override
	public void onMessage(Message message) {
		
		 TextMessage textMessage = (TextMessage) message;
		 try {
			 //从消息中取出商品ID
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			//根据商品Id查询数据库
			SearchItem searchItem = itemMapper.getItemById(itemId);
			
			//创建一个文档对象
			   SolrInputDocument document = new SolrInputDocument();
			   //想文档对象添加域
			   document.addField("id",searchItem.getId());
				 document.addField("item_title",searchItem.getTitle());
				 document.addField("item_sell_point",searchItem.getSell_point());
				 document.addField("item_price",searchItem.getPrice());
				 document.addField("item_image",searchItem.getImage());
				 document.addField("item_category_name",searchItem.getCategory_name());
			//创建文档写入索引库
				 slorServer.add(document);  
			//提交
				 slorServer.commit();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

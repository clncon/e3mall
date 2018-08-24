package cn.e3mall.service.impl;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.comm.jedis.JedisClient;
import cn.e3mall.comm.util.E3Result;
import cn.e3mall.comm.util.IDUtils;
import cn.e3mall.comm.util.JsonUtils;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbItemParamMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.pojo.TbItemParam;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Service
 * @author 孔旻
 */
@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private TbItemParamMapper itemParamMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	
	
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	
	@Override
	public TbItem getItemById(long itemId) {
		
		//查詢緩存
		 try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			 if(!StringUtils.isEmpty(json)){
				 TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				 return tbItem;
			 }
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 
		 
		//根据主键查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		
		
		if(list!=null&&list.size()>0){
			try {
				//把結果添加到緩存
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE",JsonUtils.objectToJson(list.get(0)));
				//設置過期時間
				 jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}


	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		easyUIDataGridResult.setRows(list);
		//取分页的结果
		PageInfo pageInfo = new PageInfo<>(list);
		easyUIDataGridResult.setTotal(pageInfo.getTotal());
		return easyUIDataGridResult;
	}


	@Override
	public E3Result addItem(TbItem item, String desc) {
		//1.生成商品的Id
		final long itemId = IDUtils.genItemId();
		//2.补全TbItem对象的属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		//3.向商品表插入数据
		itemMapper.insert(item);
		//4.创建一个TbItemDesc对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		//5.补全TbItemDesc的属性
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		//6.向商品描述表插入数据
		itemDescMapper.insert(tbItemDesc);
		
		//发送商品添加消息
		jmsTemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		//7.E3Result.ok()
		return E3Result.ok();
	}


	@Override
	public E3Result deleteItem(String ids) {
		if(StringUtils.isEmpty(ids)){
			return E3Result.build(404,"params为空");
		}
		
		String[] itemIds = ids.split(",");
		
		for(String id : itemIds){
			
			itemMapper.deleteByPrimaryKey(Long.parseLong(id));
		}
		return E3Result.ok();
	}


	@Override
	public E3Result getItemDescById(Long itemId) {
	
		
		if(itemId==null){
			return E3Result.build(500, "itemId is null");
		}
		
		   TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
			return E3Result.ok(itemDesc);
			
		
	}


	@Override
	public E3Result getItemParamById(Long itemId) {
		
		if(itemId==null){
			return E3Result.build(500, "itemId is null");
		}
		
		TbItemParam itemParam = itemParamMapper.selectByPrimaryKey(itemId);
		
		// TODO Auto-generated method stub
		return E3Result.ok(itemParam);
	}


	@Override
	public E3Result updateItem(TbItem tbItem, String desc) {
		if(tbItem==null||desc==null){
			return E3Result.build(500,"tbItem is null or desc is null");
		}
		itemMapper.updateByPrimaryKeySelective(tbItem);
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(tbItem.getId());
		itemDesc.setItemDesc(desc);
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		return E3Result.ok(itemDesc);
	}


	@Override
	public TbItemDesc getTbItemDescById(long itemId) {
		//查詢緩存
		 try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			 if(!StringUtils.isEmpty(json)){
				 TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				 return tbItemDesc;
			 }
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
			//把結果添加到緩存
			jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC",JsonUtils.objectToJson(itemDesc));
			//設置過期時間
			 jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemDesc;
	}
	
	


	
	
}

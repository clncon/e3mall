package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.comm.util.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		   TbItem tbItem = itemService.getItemById(itemId);
		   
		return tbItem;
		
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		
		return result;
	}
	
	
	/**
	 * 商品添加功能
	 * @return
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		E3Result result = itemService.addItem(item, desc);
		return result;
		
	}
	/**
	 * 商品删除功能
	 * @return
	 */
	@RequestMapping(value="/rest/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteItem(String ids){
		 
		E3Result result = itemService.deleteItem(ids);
		return result;
		
	}
	
	
	
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result editUIItemDesc(@PathVariable Long itemId){
		
		E3Result result = itemService.getItemDescById(itemId);
		return result;
		
		
	}
	
	@RequestMapping("/rest/item/param/item/query/{itemId}")
    public E3Result editUIItem(@PathVariable Long itemId){
		
		E3Result result = itemService.getItemParamById(itemId);
		return result;
    	
    }
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result toEditItemUI(TbItem tbItem,String desc){
		
		E3Result e3Result = itemService.updateItem(tbItem,desc);
		return e3Result;
		
	}
	
	

}

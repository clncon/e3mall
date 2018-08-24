package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.comm.util.E3Result;
import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

/**
 * 商品详情页面展示Controller
 * @author 孔旻
 *
 */
@Controller
public class ItemController {

  @Autowired
  private ItemService itemService;
  
  @RequestMapping("/item/{itemId}")
  public String showItemInfo(@PathVariable Long itemId,Model model){
	  
	  //调用服务取商品基本信息
	   TbItem tbItem = itemService.getItemById(itemId);
	   Item item = new Item(tbItem);
	   //取出商品详情
	   TbItemDesc tbItemDesc =  itemService.getTbItemDescById(itemId);
	   
	   //把信息转递到页面
	    model.addAttribute("item",item);
	    model.addAttribute("itemDesc",tbItemDesc);
	    
	    //返回逻辑视图
	return "item";
	  
  }
}

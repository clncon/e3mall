package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.comm.util.CookieUtils;
import cn.e3mall.comm.util.E3Result;
import cn.e3mall.comm.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * 购物车处理
 * @author 孔旻
 *
 */
@Controller
public class CartController {
	@Autowired
	private ItemService ItemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private int COOKIE_CART_EXPIRE;
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
		//判断用户是否是登录状态
				TbUser user = (TbUser) request.getAttribute("user");
				if(user!=null){
				   cartService.deleteCartItem(user.getId(), itemId);
				   return "redirect:/cart/cart.html";
				}
		//如果用户未登录状态，把购物车写入cookie
		//从cookie中取出购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历列表，找到要删除的商品
		 for(TbItem tbItem:cartList){
			 if(tbItem.getId().longValue()==itemId){
				 //删除商品
				 cartList.remove(tbItem);
				 //跳出循环
				  break;
			 }
		 }
		//把购物车列表写入cookie
		  CookieUtils.setCookie(request, response, "cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		  
		  return "redirect:/cart/cart.html";
	}
	/**
	 * 更新购物车
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response){
		//判断用户是否是登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
		   cartService.updateCartNum(user.getId(), itemId, num);
		   return E3Result.ok();
		}
		//从cookie中取购物车列表
		  List<TbItem> cartList = getCartListFromCookie(request);
		//遍历商品列表找到对应的商品
		  for(TbItem tbItem:cartList){
			  if(tbItem.getId().longValue()==itemId){
				  
				  //更新商品数量
				  tbItem.setNum(num);
				  break;
			  }
		  }
		//把购物车写会到cookie
		     CookieUtils.setCookie(request, response, "cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);	
		     return E3Result.ok();
	}
	/**
	 * 展示购物车列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCatList(HttpServletRequest request,HttpServletResponse response){
	     List<TbItem> cartList = getCartListFromCookie(request);
		//判断用户是否为登录状态
		   TbUser user  = (TbUser) request.getAttribute("user");
		 //如果是登录状态
		   if(user!=null){	   
			   //如果不为空，把cookie中的购物车和服务端的购物车商品合并
			   //从Cookie中取购物车列表
			    cartService.MergeCart(user.getId(),cartList);
			   //把cookie中的购物车删除
			    CookieUtils.deleteCookie(request, response,"cart");
			   //从服务端取购物车列表
			    cartList= cartService.getCartList(user.getId());
		   }
		
	
		//把列表出传递到页面
		 request.setAttribute("cartList", cartList);
		//返回逻辑视图
		 return "cart";
	}
	
	
     /**
      * 添加购物车
      * @param itemId
      * @param num
      * @param request
      * @param response
      * @return
      */
	  @RequestMapping("/cart/add/{itemId}")
	 public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录
		TbUser user =  (TbUser) request.getAttribute("user");
		  if(user!=null){
			  //如果用户登录状态，把购物车写入reidis
			  //保存到服务端
			  cartService.addCart(user.getId(), itemId, num);
			  //返回逻辑视图
			  return "cartSuccess";
		  }
		  //从cookie中取购物车列表
		   List<TbItem> cartList = getCartListFromCookie(request);
		  //判断商品在商品列表中是否存在
		   boolean flag=false;
		   for(TbItem tbItem:cartList){
			   if(tbItem.getId()==itemId.longValue()){
				   //如果存在数量相加
				   //找到商品，数量相加
				   flag=true;
				   tbItem.setNum(tbItem.getNum()+num);
				   //跳出循环
				    break;
			   }
		   }
		  //如果不存在，根据商品id查询商品信息,得到tbItem对象
		   if(!flag){
			   //根据商品id查询商品信息  
			   TbItem item = ItemService.getItemById(itemId);
			   //商品添加到商品列表
			   item.setNum(num);
			   //取一张图片
			   String image = item.getImage();
			   if(StringUtils.isNoneBlank(image)){
				   item.setImage(image.split(",")[0]);
				   
			   }
			   //把商品添加到商品列表
			    cartList.add(item);
			   
		   }
		  //写入cookie
         CookieUtils.setCookie(request, response, "cart",JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);	
         
         return "cartSuccess";
	  }
	  
	  
	  /**
	   * 从cookie中取出购物车列表的处理
	   * @param request
	   * @return
	   */
	  public  List<TbItem> getCartListFromCookie(HttpServletRequest request){
		  String json = CookieUtils.getCookieValue(request,"cart",true);
		  //判断json是否为空
		  if(StringUtils.isBlank(json)){
			  return new ArrayList<>();
		  }
		  
		  return JsonUtils.jsonToList(json, TbItem.class);
	  }
	
}

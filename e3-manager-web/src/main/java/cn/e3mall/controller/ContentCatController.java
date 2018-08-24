package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.comm.util.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容分类管理
 * @author 孔旻
 *
 */
@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id",defaultValue="0") Long parentId){
		
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
		
	}
	
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result createContentCategory(Long parentId,String name){
		//调用服务把内容数据保存到数据库
		E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
		return e3Result;
		
	}
}

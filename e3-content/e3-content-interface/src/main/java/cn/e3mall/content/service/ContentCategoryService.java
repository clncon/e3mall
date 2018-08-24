package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.comm.util.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(long parentId);
	E3Result addContentCategory(long parentId,String name);
}

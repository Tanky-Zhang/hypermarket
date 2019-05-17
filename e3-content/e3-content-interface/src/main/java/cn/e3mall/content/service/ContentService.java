package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	List<EasyUITreeNode> getCategory(long id);
	E3Result updateCatgory(long parentId, String name);
	E3Result updateCat(Long id, String name);
	E3Result deleteCat(Long id);
	E3Result addContent(TbContent tb);
	List<TbContent> showList(long id);
}

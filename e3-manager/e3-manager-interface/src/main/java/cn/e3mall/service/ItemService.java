package cn.e3mall.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page, int rows);
	List<EasyUITreeNode> selectTreeNode(long id);
    E3Result addItem(TbItem item,String desc);
	TbItemDesc getItemdesc(long parseLong);
}

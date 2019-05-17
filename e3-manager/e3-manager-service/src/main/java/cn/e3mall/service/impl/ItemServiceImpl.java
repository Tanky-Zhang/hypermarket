package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Service
 * <p>
 * Title: ItemServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.itcast.cn
 * </p>
 * 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbItemDescMapper itemDescmapper;
	@Autowired
	private JmsTemplate jmstemplate;
	@Resource
	private Destination topicdestination;
	@Autowired
	private TbItemDescMapper itemdescmapper;
	@Autowired
	private JedisClient jedis;

	@Override
	public TbItem getItemById(long itemId) {
		try {
			String json = jedis.get("ITEM_INFI_PRE:" + itemId + ":BASE");
			if(!StringUtils.isEmpty(json)){
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 根据主键查询
		// TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andIdEqualTo(itemId);
		// 执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			try {
				jedis.set("ITEM_INFI_PRE:" + item.getId() + ":BASE", JsonUtils.objectToJson(item));
				jedis.expire("ITEM_INFI_PRE:" + item.getId() + ":BASE", 3600);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return item;

		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		// 此时得到的list其实是一个page<T>对象。因为这个page<T>继承了list对象所以可以直接用List去接受
		// 并且设置了分页以后紧跟的第一个查询会产生分页效果5
		List<TbItem> list = itemMapper.selectByExample(example);
		// 创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		// 取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		// 取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public List<EasyUITreeNode> selectTreeNode(long id) {
		// TODO Auto-generated method stub
		TbItemCatExample ex = new TbItemCatExample();
		// cn.e3mall.pojo.TbContentCategoryExample.Criteria createCriteria =
		// example.createCriteria();
		cn.e3mall.pojo.TbItemCatExample.Criteria criteria = ex.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbItemCat> list = itemCatMapper.selectByExample(ex);
		List<EasyUITreeNode> elist = new ArrayList<EasyUITreeNode>();
		// EasyUITreeNode node =new EasyUITreeNode();
		for (TbItemCat li : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(li.getId());
			node.setState(li.getIsParent() ? "closed" : "open");
			node.setText(li.getName());
			elist.add(node);
		}
		return elist;
	}

	public E3Result addItem(TbItem item, String desc) {

		final Long ID = IDUtils.genItemId();
		item.setId(ID);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		// 1 正常 2下架 3删除
		item.setStatus((byte) 1);
		itemMapper.insert(item);
		
		TbItemDesc itemdesc = new TbItemDesc();
		itemdesc.setCreated(date);
		itemdesc.setItemDesc(desc);
		itemdesc.setItemId(ID);
		itemdesc.setUpdated(date);
		itemDescmapper.insert(itemdesc);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		jmstemplate.send(topicdestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {

				TextMessage message = session.createTextMessage(ID + "");
				return message;
			}
		});

		return E3Result.ok();
	}

	@Override
	public TbItemDesc getItemdesc(long parseLong) {
		// TODO Auto-generated method stub
		try {
			String json = jedis.get("ITEM_INFI_PRE:" + parseLong + ":DESC");
			if(!StringUtils.isEmpty(json)){
			TbItemDesc itemdesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
			return itemdesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItemDesc tbitem = itemdescmapper.selectByPrimaryKey(parseLong);
		try {
			jedis.set("ITEM_INFI_PRE:" + tbitem.getItemId() + ":DESC", JsonUtils.objectToJson(tbitem));
			jedis.expire("ITEM_INFI_PRE:" + tbitem.getItemId() + ":DESC", 3600);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tbitem;
	}
}

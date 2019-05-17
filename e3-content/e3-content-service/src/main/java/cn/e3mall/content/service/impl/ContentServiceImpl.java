package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
import cn.e3mall.pojo.TbContentExample;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentCategoryMapper tbContentCategory;
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	JedisClient jedis;

	@Override
	public List<EasyUITreeNode> getCategory(long id) {
		// TODO Auto-generated method stub
		// long id=0;
		TbContentCategoryExample ex = new TbContentCategoryExample();
		Criteria criteria = ex.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> list = tbContentCategory.selectByExample(ex);
		List<EasyUITreeNode> elist = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory li : list) {
			EasyUITreeNode tree = new EasyUITreeNode();
			tree.setId(li.getId());
			tree.setText(li.getName());
			tree.setState(li.getIsParent() ? "closed" : "open");
			elist.add(tree);
		}

		return elist;
	}

	@Override
	public E3Result updateCatgory(long parentId, String name) {
		// TODO Auto-generated method stub
		TbContentCategory tb = new TbContentCategory();
		tb.setParentId(parentId);
		tb.setCreated(new Date());
		tb.setUpdated(new Date());
		tb.setIsParent(false);
		tb.setName(name);
		// 1正常 2 删除
		tb.setStatus(1);
		tb.setSortOrder(1);
		tbContentCategory.insert(tb);
		TbContentCategory parent = tbContentCategory.selectByPrimaryKey(parentId);
		if (parent.getIsParent() == false) {
			parent.setIsParent(true);
			tbContentCategory.updateByPrimaryKey(parent);
		}
		// E3Result e3=new E3Result();

		return E3Result.ok(tb);
	}

	@Override
	public E3Result updateCat(Long id, String name) {
		// TODO Auto-generated method stub
		TbContentCategory tb = new TbContentCategory();
		tb.setId(id);
		tb.setName(name);
		// System.out.println(tb);
		tbContentCategory.updateByPrimaryKeySelective(tb);
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCat(Long id) {
		// TODO Auto-generated method stub

		TbContentCategory tb = tbContentCategory.selectByPrimaryKey(id);
		tbContentCategory.deleteByPrimaryKey(id);
		if (tb.getIsParent()) {
			TbContentCategoryExample ex = new TbContentCategoryExample();
			Criteria c = ex.createCriteria();
			c.andParentIdEqualTo(id);
			tbContentCategory.deleteByExample(ex);
		}
		return E3Result.ok();
	}

	@Override
	public E3Result addContent(TbContent tb) {
		// TODO Auto-generated method stub

		tb.setCreated(new Date());
		tb.setUpdated(new Date());
		contentMapper.insert(tb);
		// 用为新添加数据以后之前的缓存就不全了所以要删掉之前的数据
		try {
			jedis.hdel("CONTENT_KEY", tb.getCategoryId() + "");
		} catch (Exception e) {

		}
		return E3Result.ok();
	}

	@Override
	public List<TbContent> showList(long id) {
		// TODO Auto-generated method stub
		// 使用try catch是因为使用缓存以后不能影响正常的业务逻辑
		try {
			String hget = jedis.hget("CONTENT_KEY", id + "");
			if (StringUtils.isNotBlank(hget)) {
				List<TbContent> list = JsonUtils.jsonToList(hget, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		TbContentExample ex = new TbContentExample();
		cn.e3mall.pojo.TbContentExample.Criteria cr = ex.createCriteria();
		cr.andCategoryIdEqualTo(id);
		List<TbContent> list = contentMapper.selectByExample(ex);
		try {
			jedis.hset("CONTENT_KEY", id + "", JsonUtils.objectToJson(list));

		} catch (Exception e) {
			e.printStackTrace();

		}

		return list;
	}

}

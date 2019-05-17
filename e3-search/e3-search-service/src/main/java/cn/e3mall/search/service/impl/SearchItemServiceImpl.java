package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.mapper.Itemmapper;
import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	private SolrServer solr;
	@Autowired
	private Itemmapper itemmapper;
	@Autowired
	private SearchDao searchdao;

	@Override
	public E3Result addSearchItem() {
		try {// TODO Auto-generated method stub
			List<SearchItem> list = itemmapper.getSearch();
			for (SearchItem item : list) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", item.getId());
				doc.addField("item_title", item.getTitle());
				doc.addField("item_sell_point", item.getSell_point());
				doc.addField("item_price", item.getPrice());
				doc.addField("item_image", item.getImage());
				doc.addField("item_category_name", item.getCname());
				solr.add(doc);

			}
			solr.commit();
			return E3Result.ok();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return E3Result.build(500, "导入数据失败");
		}

	}

	@Override
	public SearchResult search(int page, int rows, String keyword) throws Exception {
		// TODO Auto-generated method stub
		SolrQuery query = new SolrQuery();
		query.setQuery(keyword);
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		query.set("df", "item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");

		SearchResult search = searchdao.search(query);
		int total = search.getTotalcount();
		int p1 = total / rows;
		if (total % rows > 0) {
			p1++;
		}
		search.setTotalPages(p1);

		return search;
		
	}
	//public void  itemAddLIstener implements 

	public E3Result itemaddsolr(long id) {
		// TODO Auto-generated method stub
		SearchItem searchitem = itemmapper.getitemadd(id);
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", searchitem.getId());
		doc.addField("item_title", searchitem.getTitle());
		doc.addField("item_sell_point", searchitem.getSell_point());
		doc.addField("item_price", searchitem.getPrice());
		doc.addField("item_image", searchitem.getImage());
		doc.addField("item_category_name", searchitem.getCname());
		try {
			solr.add(doc);
			solr.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return E3Result.ok();
	}

}

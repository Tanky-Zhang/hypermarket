package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.common.util.E3Result;

public interface SearchItemService {
	public E3Result addSearchItem();
	public SearchResult search(int page,int rows,String keyword) throws Exception;
	public  E3Result itemaddsolr(long id);

}

package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	private List<SearchItem> list;
	private int totalPages;
	private int totalcount;
	public List<SearchItem> getList() {
		return list;
	}
	public void setList(List<SearchItem> list) {
		this.list = list;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
	

}

package ngo.nabarun.test.ngo_nabarun_test.models;

import java.util.List;



public class ApiPagination<T> {
    private int pageIndex;
    private Integer pageSize;
    private int totalSize;
    private int currentSize;
    private int totalPages;
    private List<T> content;
    private int nextPageIndex;
    private int prevPageIndex;
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public int getCurrentSize() {
		return currentSize;
	}
	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	public int getNextPageIndex() {
		return nextPageIndex;
	}
	public void setNextPageIndex(int nextPageIndex) {
		this.nextPageIndex = nextPageIndex;
	}
	public int getPrevPageIndex() {
		return prevPageIndex;
	}
	public void setPrevPageIndex(int prevPageIndex) {
		this.prevPageIndex = prevPageIndex;
	}
}
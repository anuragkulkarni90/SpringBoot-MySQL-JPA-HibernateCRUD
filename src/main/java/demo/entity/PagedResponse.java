package demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagedResponse<T> {

	@JsonProperty("Page No")
	private final int pageNo;

	@JsonProperty("Page Size")
	private final int pageSize;

	@JsonProperty("Total Records")
	private int totalRecords;

	@JsonProperty("Total Pages")
	private int totalPages;

	@JsonProperty("Page")
	private T result;

	public PagedResponse(int pageNo, int pageSize, T result) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.result = result;
	}

	public PagedResponse(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public PagedResponse(int pageNo, int pageSize, int totalRecords, int totalPages, T result) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalRecords = totalRecords;
		this.totalPages = totalPages;
		this.result = result;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}

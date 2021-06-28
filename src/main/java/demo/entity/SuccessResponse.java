package demo.entity;


public class SuccessResponse<T> {
	private String status;
	private int code;
	private String message;
	private T result;

	public SuccessResponse(String status, int code, String message, T result) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public SuccessResponse(String status, int code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}

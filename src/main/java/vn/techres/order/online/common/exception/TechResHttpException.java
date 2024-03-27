package vn.techres.order.online.common.exception;

import org.springframework.http.HttpStatus;

public class TechResHttpException extends TechResBaseException {

	static final long serialVersionUID = -3387516993124229948L;
	private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	private String errorMessage = HttpStatus.INTERNAL_SERVER_ERROR.toString();
	private Object data = null;

	public TechResHttpException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public TechResHttpException(HttpStatus httpStatus, String errorMessage) {
		super(errorMessage);
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}

	public TechResHttpException(HttpStatus httpStatus, String errorMessage, Object data) {
		super(errorMessage);
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
		this.data = data;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}

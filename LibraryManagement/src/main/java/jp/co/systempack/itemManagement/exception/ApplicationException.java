package jp.co.systempack.itemManagement.exception;

public class ApplicationException extends RuntimeException {

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}

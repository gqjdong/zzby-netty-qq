package qq.exception;

public class DecodeException extends Exception{
	private static final long serialVersionUID = -7042803079342673400L;

    public DecodeException() {
        super();
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(Throwable cause) {
        super(cause);
    }
}

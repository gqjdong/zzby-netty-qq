package qq.exception;

public class NotEnoughDataException extends DecodeException {
	public NotEnoughDataException(int length, int writerIndex) {
		super("Data is not enough:" + length + ">" + writerIndex);
	}

	private static final long serialVersionUID = -5741276434958114724L;
}

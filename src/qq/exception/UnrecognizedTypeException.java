package qq.exception;

public class UnrecognizedTypeException extends DecodeException {

    public UnrecognizedTypeException(int msgType) {
        super("Unsupported message type:" + msgType);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -794731263234292143L;

}

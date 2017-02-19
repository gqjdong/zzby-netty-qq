package qq.message;

/**
 * 消息类型的枚举类
 * @author Administrator
 *
 */
public enum MessageType {
	LOGIN_REQUEST(0,LoginRequestMessage.class),
	LOGIN_STATUS(1,LoginReplyMessage.class),
	DATA(2,DataMessage.class),
	LOGIN_OUT(3,LoginOutMessage.class);
	
	//消息类型所对应的十进制数
	public final int type;
	
	//消息类型的class对象
	public final Class<?> classType;
	
	private MessageType(int type,Class<?> classType){
		this.type = type;
		this.classType = classType;
	}
}

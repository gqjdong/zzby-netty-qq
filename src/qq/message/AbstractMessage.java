package qq.message;

import java.nio.ByteBuffer;

import qq.constants.MessageConstants;
import qq.codec.BufferWrapper;

public abstract class AbstractMessage {
	
	
	 /*
     *  0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |0|0|0|0|0| Ver | type          |       
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *     length                      |                               
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *                           data
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+     *  ......
     */
	
	//消息头总长6字节
	public static final int HEADER_BYTE_LEN = 6;

	//第一个字节为版本号
    public static final int VER_BYTE_LEN = 1;

    //第二个字节为消息类型
    public static final int PACKTYPE_BYTE_LEN = 1;

    //接下来的4个字节为消息主体的长度
    public static final int PACKLEN_BYTE_LEN = 4;

    //消息主体的编码方式（utf-8）
    public static final String UTF8 = "UTF-8";
    
    //消息类型
    private final int messageType;
    //协议版本号
    private final int protocolVersion;
    
    public AbstractMessage(int type){
    	messageType = type;
    	protocolVersion = MessageConstants.QQ_PROTOCOL_VERSION;
    }
    
    /**
     * 整个消息编码
     * @return
     */
    public BufferWrapper encode(){
    	//消息主体的缓冲字节
    	ByteBuffer body = this.encodeBody().toByteBuffer();
    	BufferWrapper whole = BufferWrapper.createEmpty();
    	//协议版本号
    	whole.writeByte(protocolVersion);
    	//消息类型
    	whole.writeByte(messageType);
    	//消息体字节长度
    	whole.write4Bytes(body.limit());
    	//消息体
    	whole.writeByteBuffer(body);
    	return whole;
    }
	
    /**
     * 整个消息解码
     * @return
     */
    public void decode(BufferWrapper bodyBuffer){
    	decodeBody(bodyBuffer);
    }
    
    /**
     * 消息主体编码，在具体的子类信息中进行实现
     * @return
     */
    protected abstract BufferWrapper encodeBody();
    
    /**
     * 消息主体解码，在具体的子类信息中进行实现
     * @return
     */
    protected abstract void decodeBody(BufferWrapper buffer);
}

package qq.codec;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import qq.constants.MessageConstants;
import qq.exception.DecodeException;
import qq.exception.UnrecognizedTypeException;
import qq.message.AbstractMessage;
import qq.message.MessageType;

public class Decoder extends FrameDecoder{

	private final Map<Integer,Class<?>> type = new HashMap<Integer,Class<?>>();
	{
		//循环得出所有的消息类型
		for(MessageType t : MessageType.values()){
			type.put(t.type, t.classType);
		}
	}
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		//如果缓冲区不够消息头的长度，重新读取
		if(buffer.readableBytes() < AbstractMessage.HEADER_BYTE_LEN) return null;
		//make start for reset
		buffer.markReaderIndex();
		
		BufferWrapper wrapper = new BufferWrapper(buffer);
		//协议版本号
		int version = wrapper.readByte();
		if(version != MessageConstants.QQ_PROTOCOL_VERSION){
			throw new DecodeException("版本不正确！");
		}
		//消息类型
		int messageType = wrapper.readByte();
		//消息主体的字节长度
		int messageLen = wrapper.read4Bytes();
		//如果剩下的可读字节数小于消息主体的字节数，重新读取
		if(wrapper.readableBytes() < messageLen){
			buffer.resetReaderIndex();
			return null;
		}
		//根据消息类型获得消息的Class
		Class<?> msg = type.get(messageType);
		//如果是支持的消息类型
		if(msg != null){
			//利用反射将消息主体在具体的消息类型中解码
			Object message = msg.newInstance();
			msg.getMethod("decode", BufferWrapper.class).invoke(message, wrapper.readAsBuffer(messageLen));
			return message;
		}else{
			throw new UnrecognizedTypeException(messageType);
		}
		
	}
	
}

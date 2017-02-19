package qq.codec;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import qq.message.AbstractMessage;

public class Encoder extends OneToOneEncoder{

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if(!(msg instanceof AbstractMessage)){
			return msg;
		}
		return ((AbstractMessage)msg).encode().buffer;
	}
	
}

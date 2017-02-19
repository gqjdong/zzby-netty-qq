package qq.client.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import qq.message.DataMessage;

public class DataMessageHandler extends SimpleChannelUpstreamHandler{
	
	private static final String name = "CLIENT_DATA_HANDLER";
	
	public static String getName(){
		return name;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if(!(e.getMessage() instanceof DataMessage)) {
			super.messageReceived(ctx, e);
			return;
		}
		
		DataMessage message = (DataMessage)e.getMessage();
		
		System.out.println(message.toString());
		
		super.messageReceived(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		ctx.getChannel().close();
		super.exceptionCaught(ctx, e);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		
		super.channelOpen(ctx, e);
	}
	
}

package qq.server.hanlder;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import qq.message.DataMessage;
import qq.server.context.ChatContext;

public class DataMessageHandler extends SimpleChannelUpstreamHandler{
	
	private static final String name = "SERVER_DATA_HANDLER";
	
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
		
		String revUser = message.getRevUser();
		if(revUser == null || revUser.equals("")) return;
		if(revUser.equals("all")){
			ChatContext.sentAllUser(message);
		}else{
			ChatContext.sentToAnotherByUserName(revUser, message);
		}
		
		super.messageReceived(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		ctx.getChannel().close();
		super.exceptionCaught(ctx, e);
	}

	@Override
	public void childChannelOpen(ChannelHandlerContext ctx,
			ChildChannelStateEvent e) throws Exception {
		
		super.childChannelOpen(ctx, e);
	}
	
}

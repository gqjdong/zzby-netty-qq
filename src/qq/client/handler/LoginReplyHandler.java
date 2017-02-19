package qq.client.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import qq.database.domain.User;
import qq.message.LoginReplyMessage;

public class LoginReplyHandler extends SimpleChannelUpstreamHandler {
	
	private static final String name = "CLIENT_LOGINREPLY_HANDLER";

	public static String getName() {
		return name;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!(e.getMessage() instanceof LoginReplyMessage))
			return;

		LoginReplyMessage message = (LoginReplyMessage) e.getMessage();

		System.out.println(message.toString());

		if(message.getLoginStatus() != 0){
			ctx.getChannel().close();
			return;
		}
		
		ctx.getChannel().setAttachment(new User(message.getUserName(),message.getUserPwd(),1));
		
		ctx.getPipeline().remove(getName());
		
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

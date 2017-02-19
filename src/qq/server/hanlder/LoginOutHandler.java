package qq.server.hanlder;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import qq.database.domain.User;
import qq.message.DataMessage;
import qq.message.LoginOutMessage;
import qq.server.context.ChatContext;

public class LoginOutHandler extends SimpleChannelUpstreamHandler{
	
	private static final String name = "SERVER_LOGINOUT_HANDLER";
	
	public static String getName(){
		return name;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if(!(e.getMessage() instanceof LoginOutMessage)) {
			super.messageReceived(ctx, e);
			return;
		}
		
		Object obj = ctx.getChannel().getAttachment();
		if(obj == null) return;
		
		LoginOutMessage message = (LoginOutMessage)e.getMessage();
		
		String userName = message.getUserName();
		Integer status = message.getStatus();
		
		User user = (User)obj;
		user.setStatus(status);
		
		if(status == 4){
			ChatContext.removeUser(userName);
			ctx.getChannel().close();
			ChatContext.sentAllOthersUser(ctx.getChannel(), new DataMessage(userName,"all","退出了！"));
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

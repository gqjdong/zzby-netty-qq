package qq.server.hanlder;

import java.util.Map;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import qq.database.domain.User;
import qq.message.DataMessage;
import qq.message.LoginReplyMessage;
import qq.message.LoginRequestMessage;
import qq.server.context.ChatContext;
import qq.util.PropertiesHelper;

public class LoginRequestHandler extends SimpleChannelUpstreamHandler{
	
	private final static String name = "SERVER_LOGIN_HANDLER";
	
	public static String getName(){
		return name;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object obj = e.getMessage();
		if(!(obj instanceof LoginRequestMessage)) return;
		
		LoginRequestMessage message = (LoginRequestMessage)obj;
		
		String userName = message.getUserName();
		String userPwd = message.getUserPwd();
		//检查登录
		if(!checkLogin(userName,userPwd)) {
			ctx.getChannel().write(new LoginReplyMessage(userName,userPwd,1));
			return;
		}
		
		//将此user添加到context中
		User user = new User(userName,userPwd,0);
		//用户放到对应的channel中
		ctx.getChannel().setAttachment(user);
		
		//addUser和sentAllOthersUser都对共享变量liveUsers进行了操作，所以要保证多线程时的有序性
		synchronized(ChatContext.getLiveUsers()){
			ChatContext.addUser(user, ctx.getChannel(), userName);
			
			//通知用户登陆是否成功
			ctx.getChannel().write(new LoginReplyMessage(userName,userPwd,0));
			
			//向其他在线用户发送登录通知
			ChatContext.sentAllOthersUser(ctx.getChannel(), new DataMessage(userName,"all","我登陆了！"));
		}
		
		super.messageReceived(ctx, e);
		//移除登录handler，因为登录只需一次
		ctx.getPipeline().remove(getName());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		ctx.getChannel().write(new LoginReplyMessage("系统","系统",2));
		ctx.getChannel().close();
		super.exceptionCaught(ctx, e);
	}
;
	@Override
	public void childChannelOpen(ChannelHandlerContext ctx,
			ChildChannelStateEvent e) throws Exception {
		ChatContext.init(ctx.getChannel());
		super.childChannelOpen(ctx, e);
	}
	
	private boolean checkLogin(String userName,String userPwd){
		Map<String,String> userMap = PropertiesHelper.getUser();
		if(userMap.containsKey(userName)){
			if(userPwd.equals(userMap.get(userName))){
				return true;
			}
		}
		return false;
	}
	
}

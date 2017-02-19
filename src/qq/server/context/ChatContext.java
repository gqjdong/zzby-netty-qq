package qq.server.context;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import qq.database.domain.User;
import qq.message.DataMessage;
import qq.util.UserSessionMap;

public class ChatContext {
	
	//存储所有的登录用户
	private static final UserSessionMap<User,Channel> liveUsers = new UserSessionMap<User,Channel>();
	
	public static UserSessionMap<User,Channel> getLiveUsers(){
		return liveUsers;
	}
	
	/**
	 * 登录时初始化
	 * @param channel
	 * @param count
	 */
	public static void init(final Channel channel){
		if(channel.getAttachment() == null){
			channel.setAttachment(new User());
		}
		channel.getCloseFuture().addListener(new ChannelFutureListener(){

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()){
					User user = (User)channel.getAttachment();
					String userName = user.getUserName();
					if(userName != null && userName.length() > 0){
						liveUsers.remove(channel);
						DataMessage exitMessage = new DataMessage(userName,"all","退出了！");
						sentAllOthersUser(channel,exitMessage);
					}
				}
			}
			
		});
	}
	
	/**
	 * 增加用户
	 * @param user
	 * @param channel
	 * @param userName
	 */
	public static void addUser(User user,Channel channel,String userName){
		liveUsers.put(user, channel, userName);
	}
	
	/**
	 * 根据用户名移除user
	 * @param userName
	 */
	public static void removeUser(String userName){
		liveUsers.remove(userName);
	}
	
	/**
	 * 根据channel移除user
	 * @param channel
	 */
	public static void removeUser(Channel channel){
		liveUsers.remove(channel);
	}
	
	/**
	 * 发送给所有其他的user(除了自己)
	 * @param message
	 */
	public static void sentAllOthersUser(Channel self,DataMessage message){
		synchronized(liveUsers){
			for(User key : liveUsers.keySet()){
				Channel revChannel = liveUsers.get(key);
				if(revChannel == self) continue;
				sentToAnother(revChannel,message);
			}
		}
	}
	
	/**
	 * 发送给所有的user
	 * @param message
	 */
	public static void sentAllUser(DataMessage message){
		synchronized(liveUsers){
			for(User key : liveUsers.keySet()){
				Channel revChannel = liveUsers.get(key);
				sentToAnother(revChannel,message);
			}
		}
	}
	
	/**
	 * 通过用户名发送给特定用户
	 * @param userName
	 * @param message
	 */
	public static void sentToAnotherByUserName(String userName,DataMessage message){
		Channel revChannel = liveUsers.get(userName);
		sentToAnother(revChannel,message);
	}
	
	/**
	 * 通过revChannel发送给特定用户
	 * @param revChannel
	 * @param message
	 */
	private static void sentToAnother(Channel revChannel,DataMessage message){
		revChannel.write(message);
	}
}

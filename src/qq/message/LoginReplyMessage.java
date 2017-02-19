package qq.message;

import qq.codec.BufferWrapper;

public class LoginReplyMessage extends AbstractMessage{

	private String userName;
	
	private String userPwd;
	
	private Integer loginStatus;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public Integer getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
	}

	public LoginReplyMessage(){
		super(MessageType.LOGIN_STATUS.type);
	}
	
	public LoginReplyMessage(String userName,String userPwd,Integer loginStatus){
		this();
		this.userName = userName;
		this.userPwd = userPwd;
		this.loginStatus = loginStatus;
	}
	
	@Override
	protected BufferWrapper encodeBody() {
		BufferWrapper loginReplyBuffer = BufferWrapper.createEmpty();
		//格式为用户名^密码^登录回复状态
		String loginReplyContent = this.userName + "^" + this.userPwd + "^" + this.loginStatus;
		loginReplyBuffer.writeString(loginReplyContent);
		return loginReplyBuffer;
	}

	@Override
	protected void decodeBody(BufferWrapper buffer) {
		String loginReplyContent = buffer.readAllAsString();
		if(loginReplyContent == null || loginReplyContent.equals("")) return;
		String[] arrayLoginReply = loginReplyContent.split("\\^");
		if(arrayLoginReply.length == 3){
			this.userName = arrayLoginReply[0];
			this.userPwd = arrayLoginReply[1];
			this.loginStatus = Integer.valueOf(arrayLoginReply[2]);
		}
	}

	@Override
	public String toString() {
		String loginReply = "";
		switch (this.loginStatus) {
			case 0:
				loginReply = "登录成功！";
				break;
			case 1:
				loginReply = "用户名或密码错误！";
				break;
			case 2:
				loginReply = "对不起，系统出现异常！";
				break;
			default:
				loginReply = "未知错误！";
				break;
		}
		return loginReply;
	}
	
}

package qq.message;

import qq.codec.BufferWrapper;

public class LoginRequestMessage extends AbstractMessage{
	
	private String userName;

	private String userPwd;
	
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

	public LoginRequestMessage(){
		super(MessageType.LOGIN_REQUEST.type);
	}
	
	public LoginRequestMessage(String userName,String userPwd){
		this();
		this.userName = userName;
		this.userPwd = userPwd;
	}

	@Override
	protected BufferWrapper encodeBody() {
		BufferWrapper loginBuffer = BufferWrapper.createEmpty();
		//格式为用户名^密码
		String loginContent = this.userName + "^" + this.userPwd;
		loginBuffer.writeString(loginContent);
		return loginBuffer;
	}

	@Override
	protected void decodeBody(BufferWrapper buffer) {
		String loginContent = buffer.readAllAsString();
		if(loginContent == null || loginContent.equals("")) return;
		String[] arrayLogin = loginContent.split("\\^");
		if(arrayLogin.length == 2){
			this.userName = arrayLogin[0];
			this.userPwd = arrayLogin[1];
		}
	}

	@Override
	public String toString() {
		return this.userName + "登录了！";
	}
	
}

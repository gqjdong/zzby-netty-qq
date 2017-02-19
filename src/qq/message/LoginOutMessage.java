package qq.message;

import qq.codec.BufferWrapper;

public class LoginOutMessage extends AbstractMessage{
	
	//用户名
	private String userName;
	
	//状态(0在线1隐身2忙碌3离开4退出)
	private Integer status;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LoginOutMessage(){
		super(MessageType.LOGIN_OUT.type);
	}
	
	public LoginOutMessage(String userName,Integer status){
		this();
		this.userName = userName;
		this.status = status;
	}
	
	@Override
	protected BufferWrapper encodeBody() {
		BufferWrapper loginOutBuffer = BufferWrapper.createEmpty();
		//格式为用户名^状态
		String loginOutContent = this.userName + "^" + this.status;
		loginOutBuffer.writeString(loginOutContent);
		return loginOutBuffer;
	}

	@Override
	protected void decodeBody(BufferWrapper buffer) {
		String loginOutContent = buffer.readAllAsString();
		if(loginOutContent == null || loginOutContent.equals("")) return;
		String[] arrayLoginOut = loginOutContent.split("\\^");
		if(arrayLoginOut.length == 2){
			this.userName = arrayLoginOut[0];
			this.status = Integer.valueOf(arrayLoginOut[1]);
		}
	}

	@Override
	public String toString() {
		String statusDes = "";
		switch (this.status) {
			case 0:
				statusDes = "在线";
				break;
			case 1:
				statusDes = "隐身";
				break;
			case 2:
				statusDes = "忙碌";
				break;
			case 3:
				statusDes = "离开";
				break;
			case 4:
				statusDes = "退出";
				break;
			default:
				statusDes = "未知状态";
				break;
		}
		return this.userName + statusDes + "！";
	}
	
}

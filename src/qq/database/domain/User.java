package qq.database.domain;

public class User {
	//用户名
	private String userName;
	//密码
	private String userPwd;
	//状态(0在线1隐身2忙碌3离开4退出)
	private int status;
	
	public User(){}
	
	public User(String userName,String userPwd,int status){
		this.userName = userName;
		this.userPwd = userPwd;
		this.status = status;
	}
	
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
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
}

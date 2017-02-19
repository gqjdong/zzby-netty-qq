package qq.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import qq.codec.BufferWrapper;

public class DataMessage extends AbstractMessage{
	
	//发送者
	private String sentUser;
	
	//发送时间
	private String sentTime;
	
	//接收者
	private String revUser;
	
	//发送内容
	private String dataContent;
	
	public String getSentUser() {
		return sentUser;
	}

	public void setSentUser(String sentUser) {
		this.sentUser = sentUser;
	}

	public String getSentTime() {
		return sentTime;
	}

	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}

	public String getRevUser() {
		return revUser;
	}

	public void setRevUser(String revUser) {
		this.revUser = revUser;
	}

	public String getDataContent() {
		return dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

	public DataMessage(){
		super(MessageType.DATA.type);
	}
	
	public DataMessage(String sentUser,String revUser,String dataContent){
		this();
		this.sentUser = sentUser;
		this.revUser = revUser;
		this.dataContent = dataContent;
	}
	
	@Override
	protected BufferWrapper encodeBody() {
		BufferWrapper dataBuffer = BufferWrapper.createEmpty();
		//消息内容，格式为    发送者^发送时间^接收者^发送内容
		this.sentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String messageContent = this.sentUser + "^" + this.sentTime + "^" + this.revUser + "^" + this.dataContent;
		dataBuffer.writeString(messageContent);
		return dataBuffer;
	}

	@Override
	protected void decodeBody(BufferWrapper buffer) {
		String messageContent = buffer.readAllAsString();
		if(messageContent == null || messageContent.equals("")) return;
		String[] arrayMessage = messageContent.split("\\^");
		if(arrayMessage.length == 4){
			this.sentUser = arrayMessage[0];
			this.sentTime = arrayMessage[1];
			this.revUser = arrayMessage[2];
			this.dataContent = arrayMessage[3];
		}
	}

	@Override
	public String toString() {
		return this.sentUser + " " + this.sentTime + "\r\n" + this.dataContent;
	}
	
}

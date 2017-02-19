package main;

import qq.client.QqClient;

public class ClientTest1 {

	public static void main(String[] args) {
		startClient("dongdong","dd");
	}
	
	private static void startClient(String userName,String password) {
		QqClient.start(userName,password);
	}
	
}

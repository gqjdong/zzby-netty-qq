package main;

import qq.client.QqClient;

public class ClientTest {
	
	public static void main(String[] args) {
		startClient("saisai","ss");
	}
	
	private static void startClient(String userName,String password) {
		QqClient.start(userName,password);
	}
	
}

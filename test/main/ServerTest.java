package main;

import qq.server.QqServer;

public class ServerTest {
	
	// 控制台的编码和workspace的编码要改为GBK,不然中文会乱码
	// 控制台编码修改：run configurations 的 common选项
	// workspace编码修改：window-->preferences-->workspace
	public static void main(String[] args) {
		startServer();
	}
	
	private static void startServer() {
		QqServer.start();
	}
	
}


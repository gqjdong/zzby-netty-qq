package qq.util;

import java.util.HashMap;
import java.util.Map;

public class PropertiesHelper {
	
	/************************server端配置参数开始**********************************************/
	
	public static int getServerWorkThreadMaxPoolSize(){
		int size = 300;
			
		try{
			size = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getServer(), "work.thread.maxPoolSize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static long getServerWorkThreadMaxChannelMemorySize(){
		long size = 655360;
		
		try{
			size = Long.parseLong(QqProperties.getPropertyByFileName(QqProperties.getServer(), "work.thread.maxChannelMemorySize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static long getServerWorkThreadMaxTotalMemorySize(){
		long size = 1572864;
		
		try{
			size = Long.parseLong(QqProperties.getPropertyByFileName(QqProperties.getServer(), "work.thread.maxTotalMemorySize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static long getServerWorkThreadKeepAliveTime(){
		long size = 200;
		
		try{
			size = Long.parseLong(QqProperties.getPropertyByFileName(QqProperties.getServer(), "work.thread.keepAliveTime"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static int getServerBossThreadMaxPoolSize(){
		int size = 10;
			
		try{
			size = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getServer(), "boss.thread.maxPoolSize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static int getServerIoThreadMaxPoolSize(){
		int size = 30;
			
		try{
			size = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getServer(), "io.thread.maxPoolSize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static int getServerPort(){
		int port = 8360;
		
		try{
			port = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getServer(), "server.host.address.port"));
		}catch(Exception e){
			
		}
		
		return port;
	}
	
	public static String getServerAddress(){
		String address = "0.0.0.0";
		
		try{
			address = QqProperties.getPropertyByFileName(QqProperties.getServer(), "server.host.address");
		}catch(Exception e){
			
		}
		
		return address;
	}
	/************************server端配置参数结束**********************************************/
	
	/************************client端配置参数开始**********************************************/
	
	public static int getClientWorkThreadMaxPoolSize(){
		int size = 300;
			
		try{
			size = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getClient(), "work.thread.maxPoolSize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static long getClientWorkThreadMaxChannelMemorySize(){
		long size = 655360;
		
		try{
			size = Long.parseLong(QqProperties.getPropertyByFileName(QqProperties.getClient(), "work.thread.maxChannelMemorySize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static long getClientWorkThreadMaxTotalMemorySize(){
		long size = 1572864;
		
		try{
			size = Long.parseLong(QqProperties.getPropertyByFileName(QqProperties.getClient(), "work.thread.maxTotalMemorySize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static long getClientWorkThreadKeepAliveTime(){
		long size = 200;
		
		try{
			size = Long.parseLong(QqProperties.getPropertyByFileName(QqProperties.getClient(), "work.thread.keepAliveTime"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static int getClientBossThreadMaxPoolSize(){
		int size = 1;
			
		try{
			size = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getClient(), "boss.thread.maxPoolSize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static int getClientIoThreadMaxPoolSize(){
		int size = 10;
			
		try{
			size = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getClient(), "io.thread.maxPoolSize"));
		}catch(Exception e){
			
		}
		
		return size;
	}
	
	public static int getClientServerPort(){
		int port = 8360;
		
		try{
			port = Integer.parseInt(QqProperties.getPropertyByFileName(QqProperties.getClient(), "server.host.address.port"));
		}catch(Exception e){
			
		}
		
		return port;
	}
	
	public static String getClientServerAddress(){
		String address = "0.0.0.0";
		
		try{
			address = QqProperties.getPropertyByFileName(QqProperties.getClient(), "server.host.address");
		}catch(Exception e){
			
		}
		
		return address;
	}
	
	/************************client端配置参数结束**********************************************/
	
	/************************user配置参数开始**********************************************/
	
	public static Map<String,String> getUser(){
		Map<String,String> userMap = new HashMap<String,String>();
		
		try{
			String userInfo = QqProperties.getPropertyByFileName(QqProperties.getUser(), "qq.user");
			String[] userArray = userInfo.split(",");
			for(String user : userArray){
				String[] userAndPwd = user.split(":");
				userMap.put(userAndPwd[0], userAndPwd[1]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return userMap;
	}
	
	/************************user端配置参数结束**********************************************/
}

package qq.client;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import qq.client.handler.DataMessageHandler;
import qq.client.handler.LoginReplyHandler;
import qq.codec.Decoder;
import qq.codec.Encoder;
import qq.database.domain.User;
import qq.message.DataMessage;
import qq.message.LoginOutMessage;
import qq.message.LoginRequestMessage;
import qq.util.PropertiesHelper;

public class QqClient {
	
	static final ExecutionHandler ServerExecutionHandler;

	static
	{
		MemoryAwareThreadPoolExecutor executor = new OrderedMemoryAwareThreadPoolExecutor(
				PropertiesHelper.getClientWorkThreadMaxPoolSize(),//最大线程数
				PropertiesHelper.getClientWorkThreadMaxChannelMemorySize(),//每个channel所占用的最大内存
				PropertiesHelper.getClientWorkThreadMaxTotalMemorySize(),//pool中总共所占用的最大内存
				PropertiesHelper.getClientWorkThreadKeepAliveTime(),//线程的超时时间
				TimeUnit.SECONDS//超时时间的单位
			);
		ServerExecutionHandler = new ExecutionHandler(executor);
	}
	
	public static ExecutionHandler getServerExecutionHandler()
    {
    	return ServerExecutionHandler;
    }
	
	public static void start(final String userName,final String password) {
		
		// 日志处理
		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		
		// 初始化channel的处理通道工厂
		ChannelPipelineFactory qqPipelineFactory = new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				// 解码handler
				pipeline.addLast("decode", new Decoder());
				// 编码handler
				pipeline.addLast("encode", new Encoder());
				
				// 因为业务处理耗费资源和时间，所以使用executionHandler，
				// 将其放到新的线程池中处理，在构造函数中对其进行了初始化
				// 在其后面添加的handler都会放到该线程池中进行处理
				pipeline.addLast("executor", ServerExecutionHandler);
				
				// 处理日志Handler
				pipeline.addLast("logger", new LoggingHandler(false));
				
				// 业务处理handler
				pipeline.addLast(LoginReplyHandler.getName(), new LoginReplyHandler());
				pipeline.addLast(DataMessageHandler.getName(),new DataMessageHandler());
				
				return pipeline;
			}
			
		};
		
		NioClientSocketChannelFactory qqNioClientSocketChannelFactory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),//boss线程池
				Executors.newCachedThreadPool(),//I/O线程池
				PropertiesHelper.getClientBossThreadMaxPoolSize(),//boss最大线程数
				PropertiesHelper.getClientIoThreadMaxPoolSize()//I/O最大线程数
				);
		
		ClientBootstrap clientBootstrap = new ClientBootstrap(qqNioClientSocketChannelFactory);
		
		clientBootstrap.setPipelineFactory(qqPipelineFactory);
		clientBootstrap.setOption("ReuseAddress", true);
		clientBootstrap.setOption("child.tcpNoDelay", true);
		
		InetSocketAddress serAddress = new InetSocketAddress(PropertiesHelper.getClientServerAddress(),PropertiesHelper.getClientServerPort());
		
		//用多线程测试并发情况
//		for(int i = 0;i < 500;i ++){
//			new Thread(new ClientThread(clientBootstrap,serAddress)).start();
//		}
		
		ChannelFuture future = clientBootstrap.connect(serAddress);
		
		final Channel channel = future.awaitUninterruptibly().getChannel();
		
		if(future.isSuccess()){
			
			channel.write(new LoginRequestMessage(userName,password));
			
			new Thread(new Runnable(){
				
				@Override
				public void run() {
					
					Scanner input = new Scanner(System.in);
					
					while(true){
						
						//如果channel已经关闭，退出程序
						if(!channel.isConnected()) System.exit(0);
						//登录尚未成功，进行下次循环
						if(channel.getAttachment() == null ) continue;
						
						User user = (User)channel.getAttachment();
						
						String inputStr = input.nextLine();
						input = new Scanner(System.in);
						
						if(inputStr.equals("exit")){
							
							channel.write(new LoginOutMessage(user.getUserName(),4));
							
							ChannelFuture closeFuture = channel.getCloseFuture();
							closeFuture.addListener(new ChannelFutureListener(){
								
								@Override
								public void operationComplete(ChannelFuture future) throws Exception {
									if(future.isSuccess()){
										System.exit(0);
									}
								}
								
							});
						}else{
							try {
								String[] inputArray = inputStr.split("\\^");
								channel.write(new DataMessage(user.getUserName(),inputArray[0],inputArray[1]));
							} catch (Exception e) {
								System.out.println("格式不正确，输入格式为：接收用户^聊天内容，请重新输入！");
							}
						}
					}
				}
				
			}).start();
		}
	}
	
}

class ClientThread extends Thread{

	private ClientBootstrap clientBootstrap;
	private InetSocketAddress serverAddress;
	
	public ClientThread(ClientBootstrap clientBootstrap,InetSocketAddress serverAddress){
		this.clientBootstrap = clientBootstrap;
		this.serverAddress = serverAddress;
	}
	
	@Override
	public void run() {
		ChannelFuture future = clientBootstrap.connect(serverAddress);
		final Channel channel = future.awaitUninterruptibly().getChannel();
		
		if(future.isSuccess()){
			channel.write(new LoginRequestMessage("saisai","ss"));
		}
	}
	
}

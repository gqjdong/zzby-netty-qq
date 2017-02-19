package qq.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import qq.codec.Decoder;
import qq.codec.Encoder;
import qq.server.hanlder.DataMessageHandler;
import qq.server.hanlder.LoginOutHandler;
import qq.server.hanlder.LoginRequestHandler;
import qq.util.PropertiesHelper;

public class QqServer {
	
	static final ExecutionHandler ServerExecutionHandler;

	static
	{
		MemoryAwareThreadPoolExecutor executor = new OrderedMemoryAwareThreadPoolExecutor(
				PropertiesHelper.getServerWorkThreadMaxPoolSize(),//最大线程数
				PropertiesHelper.getServerWorkThreadMaxChannelMemorySize(),//每个channel所占用的最大内存
				PropertiesHelper.getServerWorkThreadMaxTotalMemorySize(),//pool中总共所占用的最大内存
				PropertiesHelper.getServerWorkThreadKeepAliveTime(),//线程的超时时间
				TimeUnit.SECONDS//超时时间的单位
			);
		ServerExecutionHandler = new ExecutionHandler(executor);
	}
	
	public static ExecutionHandler getServerExecutionHandler()
    {
    	return ServerExecutionHandler;
    }
	
	public static void start() {
		//日志处理
		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		
		//初始化channel的处理通道工厂
		ChannelPipelineFactory qqPipelineFactory = new ChannelPipelineFactory(){
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				//解码handler
				pipeline.addLast("decode", new Decoder());
				//编码handler
				pipeline.addLast("encode", new Encoder());
				
				//因为业务处理耗费资源和时间，所以使用executionHandler，
				//将其放到新的线程池中处理，在构造函数中对其进行了初始化
				//在其后面添加的handler都会放到该线程池中进行处理
				pipeline.addLast("executor", ServerExecutionHandler);
				
				//处理日志Handler  
				pipeline.addLast("logger", new LoggingHandler(false));
				
				//业务处理handler
				pipeline.addLast(LoginRequestHandler.getName(), new LoginRequestHandler());
				pipeline.addLast(DataMessageHandler.getName(), new DataMessageHandler());
				pipeline.addLast(LoginOutHandler.getName(), new LoginOutHandler());
				
				return pipeline;
			}
			
		};
		
		//初始化socket工厂
		NioServerSocketChannelFactory qqNioServerSocketChannelFactory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),//boss线程池
				PropertiesHelper.getServerBossThreadMaxPoolSize(),//boss线程数
				Executors.newCachedThreadPool(),//I/O线程池
				PropertiesHelper.getServerIoThreadMaxPoolSize()//I/O线程数
				);
		ServerBootstrap bootstrap = new ServerBootstrap(qqNioServerSocketChannelFactory);
		
		InetSocketAddress qq_address = new InetSocketAddress(PropertiesHelper.getServerAddress(),PropertiesHelper.getServerPort());
		
		bootstrap.setPipelineFactory(qqPipelineFactory);
		bootstrap.setOption("backlog", 1000);
		//允许端口重用(详见http://blog.csdn.net/braveyly/article/details/6462276)
		bootstrap.setOption("reuseAddress", true);
		//禁用nagle算法(一种改善网络拥塞的算法，会尽量将小包组成大包后发送，但影响实时性，详见http://blog.163.com/li_xiang1102/blog/static/607140762011111103213616/)
		bootstrap.setOption("child.tcpNoDelay", true);
		
		bootstrap.bind(qq_address);
	}
	
}

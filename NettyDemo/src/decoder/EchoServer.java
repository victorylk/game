package decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

//分隔符解码器
public class EchoServer {
	
	private Logger logger = LoggerFactory.getLogger(EchoServer.class);
	
	public void bind(int port){
		logger.info("启动");
		//线程池：用于接收客户端的TCP链接。职责如下
		/**1：接收客户端TCP链接，初始化Channel参数。
		 * 2：将链路状态变更事件通知给ChannelPipeline
		 */
		EventLoopGroup boos = new NioEventLoopGroup();
		
		//线程池：用于处理I/O相关的读写操作，或者执行系统Task、定时任务Task等。职责如下：
		/**
		 * 1.异步读取通信对端的数据包，发送读事件到ChannelPipeline
		 * 2.异步发送消息到通信对端，调用ChannelPipeline的消息发送接口。
		 * 3.执行系统调用Task
		 * 4.执行定时任务Task，例如链路空闲状态监测定时任务。
		 */
		EventLoopGroup work = new NioEventLoopGroup();
		try {
			
			ServerBootstrap server = new ServerBootstrap();
			server.group(boos, work).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)//最大客户端链接数为100
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socket) throws Exception {
					//创建分隔符缓冲对象ByteBUf、：$_。自动对请求的消息进行了解码，后续的ChannelHandler接收到的msg就是个完整的消息包
					ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
					//1024表示单条消息的最大长度。达到长度后仍然没有查找到分隔符，就抛出异常。
					socket.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
					//将ByteBUf解码成字符串对象。
					socket.pipeline().addLast(new StringDecoder());
					//这个ChannelHandler接收到的msg消息就是解码后的字符串对象。
					socket.pipeline().addLast(new EchoServerHandler());
				}
			});
			
			//绑定端口
			ChannelFuture f = server.bind(port);
			f.addListener(new GenericFutureListener<Future<? super Void>>() {
				@Override
				public void operationComplete(Future<? super Void> fe) throws Exception {
					System.out.println("链接建立成功");
				}
			});
			//等待 服务端监听端口关闭
			f.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			boos.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new EchoServer().bind(8008);
	}
	
	public void test(){
		
	}
}

package com.server;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.handler.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerD {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerD.class);
	
	//监听的端口号
	private  int port;
	public ServerD(int port){
		this.port = port;
	}
	public static void main(String[] args) throws Exception {
		//设置端口值（抛出一个 NumberFormatException 如果该端口参数的格式不正确）
        //调用start方法启动服务器
        new ServerD(8000).start();
	}
	
	public void start() throws Exception{
		//创建 EventLoopGroup
		NioEventLoopGroup group = new NioEventLoopGroup();
		
		try {
			//创建 ServerBootstrap
			ServerBootstrap server = new ServerBootstrap();
			//指定使用 NIO 的传输 Channel
			server.group(group).channel(NioServerSocketChannel.class)
			.localAddress(new InetSocketAddress(port))//设置 socket 地址使用所选的端口
			.childHandler(new ChannelInitializer<SocketChannel>() {
				//添加 EchoServerHandler 到 Channel 的 ChannelPipeline
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ServerHandler());
				};
			});
			
			ChannelFuture f = server.bind().sync();//绑定的服务器;sync 等待服务器关闭
			
			System.out.println(ServerD.class.getSimpleName()+"started and listen on "+f.channel()
			.localAddress());
			f.channel().closeFuture().sync();//关闭 channel 和 块，直到它被关闭
		} finally {
			group.shutdownGracefully().sync();//关机的 EventLoopGroup，释放所有资源。
		}
	}
}

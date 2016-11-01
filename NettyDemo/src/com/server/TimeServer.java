package com.server;

import com.handler.TimeServerHandler;
import com.handler.TimeServerHandler2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {
	
	public void bind(int port){
		//配置服务端的Nio线程组，包含了一组Nio线程，专门用于网络事件的处理，实际上就是Reactor线程组。
		EventLoopGroup bossGroup = new NioEventLoopGroup();//一个用于服务端接受客户端的连接
		EventLoopGroup workGroup = new NioEventLoopGroup();//一个用于进行SocketChannel的网络读写
		
		try {
			//Netty用于启动NIo服务端的辅助启动类，目的是降低服务端的开发复杂度（减少代码量）
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)//设定服务端为非阻塞模式
			.option(ChannelOption.SO_BACKLOG,1024)
			.childHandler(new ChildHandler());
			
			//绑定端口，同步等待成功。
			ChannelFuture f = boot.bind(port).sync();
			
			//等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//优雅退出，释放线程池资源。
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	private class ChildHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel socket) throws Exception {
			//利用nettyLineBasedFrameDecoder解决Tcp粘包问题，添加解码器
			//Line~规定客户端发送过来的字节必须带有换行符\n或\r，没有就异常
			socket.pipeline().addLast(new LineBasedFrameDecoder(1024));
			socket.pipeline().addLast(new StringDecoder());
			
			socket.pipeline().addLast(new TimeServerHandler2());
		}
	}
	public static void main(String[] args) {
		new TimeServer().bind(8008);
	}
}

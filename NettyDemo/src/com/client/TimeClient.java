package com.client;

import com.handler.TimeClientHandler;
import com.handler.TimeClientHandler2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
	
	public void connect(int port,String host){
		//配置客户端NIo线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			//客户端辅助启动类
			Bootstrap client = new Bootstrap();
			client.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY,true)
			.handler(new ChannelInitializer<SocketChannel>() {
				//其作用是客户端创建成功之后，初始化它的时候将它的ChannelHandler设置到ChannelPipeline中，处理网络Io事件
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new TimeClientHandler2());
				}				
			});
			//发起异步链接操作
			ChannelFuture f = client.connect(host,port).sync();
			
			//等待客户端链路关闭
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new TimeClient().connect(8008, "127.0.0.1");
	}
}

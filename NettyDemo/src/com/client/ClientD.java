package com.client;

import java.net.InetSocketAddress;

import com.handler.ClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientD {
	
	private String host;
	private int port;
	
	public ClientD(String host,int port) {
		this.host = host;
		this.port = port;
	}
	
	
	public void start() throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap client = new Bootstrap();
			client.group(group)
			.channel(NioSocketChannel.class)
			.remoteAddress(new InetSocketAddress(host, port))
			.handler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ClientHandler());
				};
			});
			ChannelFuture f = client.connect().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {


        new ClientD("127.0.0.1", 8000).start();
		
	}
}

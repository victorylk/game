package serializableServer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import serializableServer.handler.SubscribeReqServerHandler;

public class ServerT {
	
	public static void main(String[] args) {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(boss, work).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socket) throws Exception {
					socket.pipeline().addLast(new ObjectDecoder(1024*1024,
							ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
					socket.pipeline().addLast(new ObjectEncoder());
					socket.pipeline().addLast(new SubscribeReqServerHandler());
				}
			});
			ChannelFuture f = server.bind(8008).sync();
			
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	
}

package serializableServer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import serializableServer.handler.SubscribeReqClientHandler;

public class TClient {
	
	public void connect(String host,int port){
		
		//配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap client = new Bootstrap();
			client.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,
					true).handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socket) throws Exception {
							socket.pipeline().addLast(new ObjectDecoder(1024,ClassResolvers.cacheDisabled(this.getClass()
									.getClassLoader())));
							socket.pipeline().addLast(new ObjectEncoder());
							socket.pipeline().addLast(new SubscribeReqClientHandler());
						}
					});
			ChannelFuture f = client.connect(host,port).sync();
			
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new TClient().connect("127.0.0.1", 8008);
	}
}

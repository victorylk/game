package webSocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import webSocket.handler.WebSocketHandler;

public class Server {
	
	public void bind(int port){
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(boss,work).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//将请求和应答消息编码或者解码为HTTP消息
					ch.pipeline().addLast("http-codec",new HttpServerCodec());
					//将消息的多个部分组合成一条完整的HTTP消息。
					ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
					
					//想客户端发送HTML5文件，主要用于支持游览器和服务端进行WebSocket通信。
					ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
					
					ch.pipeline().addLast("handler",new WebSocketHandler());
				}				
			});
			
			ChannelFuture f = server.bind(port).sync();		
			System.out.println("open your browser and navigate to ");
			f.channel().closeFuture().sync();			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new Server().bind(8008);
	}
}

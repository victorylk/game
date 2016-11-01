package httpServer.server;

import httpServer.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServer {
	
	private static final String DEFAULT_URL="/src/httpServer/";
	
	public void bind(final int port,final String url){
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(boss, work).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socket) throws Exception {
					
					//http请求消息解码器
					socket.pipeline().addLast("http-decoder",new HttpRequestDecoder());
					
					//作用是将多个消息转换为单一的FullHttpRequest或者FullHttpResponse，原因是Http解码器在每个消息中会生成多个消息对象
					//HttpRequest,HttpResponse,HttpContent,LastHttpContent
					socket.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
					
					//对Http响应消息进行编码
					socket.pipeline().addLast("http-encoder",new HttpResponseEncoder());
					
					//支持异步发送大的码流（例如大的文件传输）。但不占用过多的内存。
					socket.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
					socket.pipeline().addLast("fileServerHandler",new HttpServerHandler(url));
				}				
			});
			
			ChannelFuture f = server.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new HttpServer().bind(8008,DEFAULT_URL);
	}
}

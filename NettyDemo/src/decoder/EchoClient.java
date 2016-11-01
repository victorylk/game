package decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
	
	public void connect(String host,int port){
		EventLoopGroup boss = new NioEventLoopGroup();		
		try {
			Bootstrap client = new Bootstrap();
			client.group(boss).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socket) throws Exception {
					ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
					socket.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
					socket.pipeline().addLast(new StringDecoder());
					socket.pipeline().addLast(new EchoClientHandler());
				}				
			});
			//发起异步链接操作
			ChannelFuture f = client.connect(host, port).sync();
			
			//等待客户端链路关闭
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		//启动客户端操作。
		new EchoClient().connect("127.0.0.1", 8008);
	}
}

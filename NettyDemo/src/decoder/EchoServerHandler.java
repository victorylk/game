package decoder;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {
	
	int counter = 0;
	//ChannelHandlerContext：可以用来读写Netty中的数据流
	/**
	 * 可以有两种方式来发送数据，一种是把数据直接写入Channel，
	 * 一种是把数据写入ChannelHandlerContext，它们的区别是写入Channel的话，
	 * 数据流会从Channel的头开始传递，而如果写入ChannelHandlerContext的话，数据流会流入管道中的下一个Handler。
	 * ChannelHandlerContext.fireChannelRead(decodedMessage)方法把编码好的Message传递给下一个Handler。
	 */
	/**
	 * ChannelHandler有两个子类ChannelInboundHandler和ChannelOutboundHandler，
	 * 这两个类对应了两个数据流向，如果数据是从外部流入我们的应用程序，我们就看做是inbound，相反便是outbound。
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("This is"+ ++counter+" times receive client：["+body+"]");
		body+="$_";
		ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(echo);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	public static void main(String[] args) {
		List<String> list = Arrays.asList(new String[]{"帅哥","丑逼"});
		list.stream().forEach(str->{
			System.out.println(str);
		});
	}
}

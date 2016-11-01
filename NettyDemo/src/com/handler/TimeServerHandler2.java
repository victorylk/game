package com.handler;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 模拟TCP粘包拆包
 * @author like
 */
public class TimeServerHandler2 extends ChannelHandlerAdapter {
	
	public int counter;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ByteBuf buf = (ByteBuf) msg;
		// readableBytes方法可以获取缓冲区可读的字节数，根据可读的字节数创建byte数组
		//byte[] req = new byte[buf.readableBytes()];

		// 通过readBytes方法将缓冲区中的字节数组复制到新建的bytes数组中
		//buf.readBytes(req);

		// 将req里的消息字节数组按指定编码生成字符串。
//		String body = new String(req, "UTF-8").substring(0,req.length-System.getProperty("line.separator")
//				.length());
		String body = msg.toString();
		System.out.println("the timeServer recive order：" + body+"；the counter is："+ ++counter);
		
		
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()
				: "BAD ORDER";
		currentTime = currentTime+System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

		// write方法并不直接将消息写入SocketChannel，调用write方法只是把待发送的消息放到发送缓冲区中
		// 在通过调用flush方法，将发送缓冲区中的消息全部写到客户端(SocketChannel中)
		ctx.writeAndFlush(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}

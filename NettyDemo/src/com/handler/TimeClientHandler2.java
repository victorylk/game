package com.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * 摸你Tcp 粘包拆包问题
 * @author like
 *
 */
public class TimeClientHandler2 extends ChannelHandlerAdapter {
	
	
	private int counter;
	private byte[] req;
	public TimeClientHandler2() {
		req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
	}

	// 链接建立之后
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}

	// 服务端返回应答消息的时候被调用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		ByteBuf buf = (ByteBuf) msg;
//		// 根据ByteBUf中有多少可读的字节的大小创建byte数组
//		byte[] req = new byte[buf.readableBytes()];
//
//		// 将内容写到byte数组中
//		buf.readBytes(req);		
		//String body = new String(msg2, "UTF-8");
		String body=(String) msg;
		System.out.println("Now is：" + body+"；the counter is ："+ ++counter);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
	
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("客户端退出~！");
		super.close(ctx, promise);
	}
}

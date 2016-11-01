package com.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	//服务器的链接被建立后调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//当被通知该 channel 是活动的时候就发送信息
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8));
	}
	
	//数据后从服务器接收到调用
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
		ByteBuf in = (ByteBuf) arg1;
		System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));    //3
	}
	
	//捕获一个异常时调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		 cause.printStackTrace();
	     ctx.close();
	}
	
	@Override
	protected void messageReceived(io.netty.channel.ChannelHandlerContext arg0, ByteBuf arg1) throws Exception {
		
	}
	
	
//	//当绑定到服务器端的时候出发
//	@Override
//	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//		System.out.println("hello world i.m a client");
//	}
	
}

package com.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

@Sharable// 标识这类的实例之间可以在 channel 里面共享
public class ServerHandler extends ChannelHandlerAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	
	//有客户端链接的时候触发
//	@Override
//	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//		logger.info("有链接");
//		System.out.println("hello world,i,m server");
//	}
	
	//每个信息入站都会调用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("有客户端链接到服务器时调用了");
		ByteBuf in = (ByteBuf) msg;
		System.out.println("Server recived："+in.toString(CharsetUtil.UTF_8));
		ctx.write(in);//将所接收的消息返回给发送者。注意，这还没有冲刷数据
	}
	
	//通知处理器最后的 channelread() 是当前批处理中的最后一条消息时调用
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelRead执行完后调用");
		//冲刷所有待审消息到远程节点。关闭通道后，操作完成
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	//读操作时捕获到异常时调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

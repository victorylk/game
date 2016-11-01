package com.threadTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.util.CharsetUtil;

public class NettyBioServer {
	public void server(int port){
		final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HI \r\n",CharsetUtil.UTF_8));
		EventLoopGroup group = new OioEventLoopGroup();
		
	}
}

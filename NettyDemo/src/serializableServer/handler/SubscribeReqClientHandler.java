package serializableServer.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import serializableServer.SubscribeReq;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class SubscribeReqClientHandler extends ChannelHandlerAdapter {
	
	private SubscribeReq sendReq(int i){
		SubscribeReq req = new SubscribeReq();
		req.setAddress("北京市海淀区花园桥南");
		req.setPhoneNumber("13260030471");
		req.setProductName("Netty 权威指南");
		req.setSubReqId(i);
		req.setUserName("like");
		return req;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.write(sendReq(i));
		}
		ctx.flush();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive server response ：["+msg+"]");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}

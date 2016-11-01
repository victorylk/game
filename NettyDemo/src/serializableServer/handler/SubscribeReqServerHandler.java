package serializableServer.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import serializableServer.SubscribeReq;
import serializableServer.SubscribeResp;

public class SubscribeReqServerHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SubscribeReq req = (SubscribeReq) msg;
		if("like".equalsIgnoreCase(req.getUserName())){
			System.out.println("service accept client subscribe req£º["+req.toString()+"]");
		}
		ctx.writeAndFlush(resp(req.getSubReqId()));
	}
	
	private SubscribeResp resp(int subReqID){
		SubscribeResp resp = new SubscribeResp();
		resp.setSubReqId(subReqID);
		resp.setRespCode(0);
		resp.setDesc("Netty book order succed,3 Days later,sent to the designated address");
		return resp;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

package bytebufferCode;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userName;
	private int userId;
	
	public UserInfo buildUserName(String userName){
		this.userName = userName;
		return this;
	}
	public UserInfo buildUserId(int userId){
		this.userId = userId;
		return this;
	}
	
	public final String getUserName(){
		return userName;
	}
	
	public final int getUserId(){
		return userId;
	}
	
	
	public byte[] codec(){
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userId);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}
	public byte[] codec(ByteBuffer buffer){
		buffer.clear();
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userId);
		buffer.flip();
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}
}

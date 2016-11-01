package bytebufferCode;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * 普通长度测试
 * @author like
 */
public class Test {
	public static void main(String[] args) throws Exception {
		
		UserInfo info = new UserInfo();
		info.buildUserName("wlecome to netty").buildUserId(100);
		
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bas);
		oos.writeObject(info);
		oos.flush();
		oos.close();
		bas.close();
		//jdk本身的序列化机制码流偏大，存储的时候占空间，硬件成本就越高，网络传输时更占带宽。吞吐量降低。
		System.out.println("Serializable："+bas.toByteArray().length);
		System.out.println("Nio："+info.codec().length);
	}
}

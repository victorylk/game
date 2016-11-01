package bytebufferCode;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * 序列化性能时间测试
 * @author like
 */
public class Test2 {
	
	public static void main(String[] args) {
		UserInfo info  = new UserInfo();
		info.buildUserId(100).buildUserName("welcome to netty");
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			long star = System.currentTimeMillis();
			int loop = 1000000;
			for (int i = 0; i < loop; i++) {
				oos.writeObject(info);
				oos.flush();
				oos.close();
				byte[] b = bos.toByteArray();
				bos.close();
			}
			long end = System.currentTimeMillis();
			System.out.println("JDK Serializable："+(end-star));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------------------------");
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		long star = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			byte[] b = info.codec(buffer);
		}
		long end = System.currentTimeMillis();
		System.out.println("NIO："+(end-star));
	}
}

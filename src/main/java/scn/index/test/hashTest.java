package scn.index.test;

import java.util.concurrent.ConcurrentHashMap;

import scn.index.amq.UserSession;

public class hashTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserSession s1 = new UserSession("user1","device752","1");
		UserSession s2 = new UserSession("user1","device752","1");
		
		System.out.println("s1 hash:"+s1.hashCode());
		System.out.println("s2 hash:"+s2.hashCode());
		int high = 2100000000;
		int low = 2000000000;
		System.out.println("mid using >>> 1 = " + ((low + high) >>> 1));
		System.out.println("mid using / 2   = " + ((low + high) >>1));
		
		ConcurrentHashMap<UserSession,String> maps = new ConcurrentHashMap<UserSession,String>();
		maps.put(s1,"hello");
		System.out.println( "get value from user session:"+maps.get(s2));
		
	}

}

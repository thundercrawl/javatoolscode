package scn.index.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.builder.HashCodeBuilder;

import bg.index.amq.UserSession;

public class hashTest {

	
	public static long hashCode(String str)
	{
		
		 try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			if(str!=null)
			{
				long result = -1;
				String input = str;
				byte[] hash =  digest.digest(input.getBytes());
				char[] output = new char[hash.length];
				int index = 0;
				
				for(Byte b:hash)
				{
					output[index++]= (char)( ( (b.intValue()%10+10)%10)+48);
				}
				result = new Long(new String(output).substring(0, 16));
				
			
				 
				return result;
			}
		
		} catch (NoSuchAlgorithmException e) {
		
			e.printStackTrace();
		}
		return -1;
		
		
	
	}
	
	public static String generateSeed()
	{
		char[] seeds = {'A','B','C','D','E','F','0','1','2','3','4','5','6','7','8','9'};
	
		
		
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<16;i++)
			sb.append(seeds[(int)(Math.random()*seeds.length)]);
		//System.out.println(sb.toString());
		return sb.toString();
	}
	public static void main(String[] args)
	{
		generateSeed();
		System.out.println("hash:"+new HashCodeBuilder().append(generateSeed()).toHashCode());
		
		long counters[] = new long[200];
		for(int i=0;i<200;i++)
			counters[i] = 0;
		
		long rt = hashCode("") %200 ;
		System.out.println("empty hash is："+rt);
		HashMap<Long,String> key2hash = new HashMap<Long,String>();
		for( int idx=0;idx<2000;idx++)
		{
			String seed = generateSeed();
			long hash = hashCode(seed) ;
			long zx =hash %200 ;
			if(key2hash.containsKey(hash))
			{
				System.out.println("duplicate find:"+key2hash.get(hash)+" vs "+seed);
			}
			else
				key2hash.put(hash, seed);
		//long zx = hashCode(generateSeed())%200;
		//if(zx <0) zx = zx+200;
			//System.out.println(zx);
			counters[(int)zx]++;
		}
		
		System.out.println("distribution is: "+key2hash.size());
		for(int i=0;i<200;i++)
			System.out.print(counters[i]+" ");
		
	}
	public  void main1(String[] args) {
		// TODO Auto-generated method stub
		UserSession s1 = new UserSession("user1","device752","1");
		UserSession s2 = new UserSession("user1","device753","1");
		
		System.out.println("s1 hash:"+s1.hashCode());
		System.out.println("s2 hash:"+s2.hashCode());
		int high = 2100000000;
		int low = 2000000000;
		System.out.println("mid using >>> 1 = " + ((low + high) >>> 1));
		System.out.println("mid using / 2   = " + ((low + high) >>1));
		
		ConcurrentHashMap<UserSession,String> maps = new ConcurrentHashMap<UserSession,String>();
		maps.put(s1,"hello");
		System.out.println( "get value from user session:"+maps.get(s2));
		
		int oddcounter = 0;
		int evencounter = 0;
		int counters[] = new int[200];
		for(int i:counters)
			counters[i]=0;
		for( int idx=0;idx<400;idx++)
		{
			int rt = new UserSession("","device"+idx,"").hashCode() %200 ;
			
			counters[rt]++;
		}
		System.out.println("distribution is: ");
		for(int i:counters)
			System.out.print(counters[i]+" ");
	}

}

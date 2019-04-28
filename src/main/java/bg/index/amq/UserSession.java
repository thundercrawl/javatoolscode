package bg.index.amq;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserSession {
	private String username;
	private String deviceID;
	private String topicName;
	public UserSession(String username,String deviceID,String topicName)
	{
		this.username = username;
		this.deviceID = deviceID;
		this.topicName = topicName;
		
	}
	
	
	public String getClientID()
	{
		return username+"#"+deviceID;
	}
	
	@Override
	public  boolean equals(Object obj)
	{
		if(obj instanceof UserSession)
		{
			UserSession comp =  (UserSession) obj;
			if(this.username.equals(comp.getUsername())&&this.getDeviceID().equals(comp.getDeviceID()))
			{
				return true;
			}
		}
		return false;
		
	}
	@Override
	public int hashCode()
	{
		
		 try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			if(username!=null&&deviceID!=null)
			{
				int result = -1;
				String input = getClientID();
				byte[] hash =  digest.digest(input.getBytes());
				char[] output = new char[hash.length];
				int index = 0;
				
				for(Byte b:hash)
				{
					
					output[index++]= (char)( ( (b.intValue()%10+10)%10)+48);
				}
			
		//		System.out.println(Integer.MAX_VALUE);
				result = new Integer(new String(output).substring(0, 8));
				
			
				 
				return result;
			}
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return hashCode();
	}

    /**
     * @return String return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String return the deviceID
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * @param deviceID the deviceID to set
     */
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * @return String return the topicName
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * @param topicName the topicName to set
     */
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

}

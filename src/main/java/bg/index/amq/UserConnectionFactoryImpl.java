package bg.index.amq;

import java.util.concurrent.ConcurrentHashMap;

public class UserConnectionFactoryImpl extends UserConnectionFactory {

	private static ConcurrentHashMap<UserSession,UserConnection> user2conn =new ConcurrentHashMap<UserSession,UserConnection>();
	@Override
	public UserConnection GetConnection(UserSession session) {

		if(user2conn.get(session) != null)
			return user2conn.get(session);
		MessageAMQConsumer consumer = new MessageAMQConsumer(session);
		UserConnection cnn = new UserConnection(consumer);
	
		user2conn.put(session, cnn);
		return cnn;
	}
	@Override
	public UserConnection RegisterConnection(UserSession session) {
		if(user2conn.get(session) != null)
		{
			UserConnection cnn = user2conn.get(session);
			cnn.reconnect();
			return cnn;
		}
		MessageAMQConsumer consumer = new MessageAMQConsumer(session);
		UserConnection cnn = new UserConnection(consumer);
	
		user2conn.put(session, cnn);
		return cnn;
	}

}

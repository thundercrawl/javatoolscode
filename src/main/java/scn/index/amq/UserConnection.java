package scn.index.amq;

public class UserConnection  {

private MessageConsumer consumer;
	private static UserConnectionFactory factory;
	public UserConnection(MessageConsumer csm)
	{
		consumer = csm;
		init();
	}
	public String getMessage()
	{
		return consumer.getMessage();
	}

	public void Ack()
	{
		consumer.sendACK();;
	}
	
	private void init()
	{
		consumer.init();
	}
	public static UserConnection getConnection(UserSession session)
	{
		if(factory==null)
		{
			factory= new UserConnectionFactoryImpl();
		}
		
		return factory.GetConnection(session);
		
	}
	public static UserConnection registerConnection(UserSession session)
	{
		if(factory==null)
		{
			factory= new UserConnectionFactoryImpl();
		}
		
		return factory.RegisterConnection(session);
		
	}
	public void reconnect()
	{
		consumer.close();
		consumer.init();
	}
}

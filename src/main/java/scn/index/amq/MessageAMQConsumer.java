package scn.index.amq;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import scn.index.domino.DominoLogger;



public class MessageAMQConsumer implements MessageConsumer {

	private UserSession sess;
	private javax.jms.MessageConsumer consumer ;
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    private static final String BROKEN_URL = Constants.brokeruri;
	private static ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEN_URL);
	public MessageAMQConsumer(UserSession session)
	{
		sess = session;
	}
	


    
    private boolean durableConsumer = false;
    private TextMessage lastMSG = null;


    Connection connection;

    Session session;
    
    MessageHandler handler=null;
    ThreadLocal<MessageConsumer> threadLocal = new ThreadLocal<>();
    AtomicInteger count = new AtomicInteger();
    
    
   
 
    public  void  setMessageHandler(MessageHandler handler)
    {
    	this.handler = handler;
    }

    
	@Override
	public void init() {
		  try {
        
      
            connection  = connectionFactory.createConnection();
            
            connection.setClientID(new Integer(sess.hashCode()).toString());
            connection.start();
            session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
            if(sess.getTopicName()==""||sess.getTopicName()==null)
            {
            	DominoLogger.consolePrint(" topic not set, use default");
            	sess.setTopicName("scn_domino");
            }
           Topic topic = session.createTopic(sess.getTopicName());
            consumer = session.createDurableSubscriber(topic, sess.getClientID());
            
            
        } catch (JMSException e) {
            e.printStackTrace();
        }
	}

	@Override
	public String getMessage()  {
		try {
			if(consumer == null)
			{
				String errorMessage = "consumer created failed, that been hold by server";
				DominoLogger.consolePrint(errorMessage);
				
				return errorMessage;
			}
			
			lastMSG = (TextMessage) consumer.receiveNoWait();
			
        if(lastMSG!=null) {
        	return lastMSG.getText();
        }
		}
		catch(JMSException e)
		{
			e.printStackTrace();
		}
    return "";
	}

	@Override
	public void sendACK() {
		try {
			lastMSG.acknowledge();
		} catch (JMSException e) {
			
			e.printStackTrace();
		}

	}

	@Override
	public void ListenOnTopic(String topic) {
		// TODO Auto-generated method stub

	}


	@Override
	public void close() {

		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	


	
}

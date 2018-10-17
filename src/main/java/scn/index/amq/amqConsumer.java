package scn.index.amq;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class amqConsumer {


    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    private static final String BROKEN_URL = Constants.brokeruri;
    
    private boolean durableConsumer = false;
    ConnectionFactory connectionFactory;

    Connection connection;

    Session session;
    MessageHandler handler=null;
    ThreadLocal<MessageConsumer> threadLocal = new ThreadLocal<>();
    AtomicInteger count = new AtomicInteger();
    
    
    public amqConsumer()
    {
    	durableConsumer = false;
    }
    
    public amqConsumer(boolean durable)
    {
    	this.durableConsumer = durable;
    }
    public void init(){
        try {
        	connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEN_URL);
        	
            connection  = connectionFactory.createConnection();
            
            connection.setClientID(Constants.consumerMain);
            connection.start();
            session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
            
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    public  void  setMessageHandler(MessageHandler handler)
    {
    	this.handler = handler;
    }

    public void getMessage(String disname ,String selector){
        try {
        	
            Topic topic = session.createTopic(disname);
            MessageConsumer consumer = null;

            if(threadLocal.get()!=null){
                consumer = threadLocal.get();
            }else{
            	if(!this.durableConsumer)
            	{
            	
            	if(selector.equals(""))
            		consumer=session.createConsumer(topic);
            	else
            		consumer = session.createConsumer(topic,selector);
            	}
            	else
            	{
            		if(selector.equals(""))
            		{
            			System.out.println("create durable consumer");
            			consumer= session.createDurableSubscriber(topic, disname);
            			
            		}
                	else
                		consumer = session.createDurableSubscriber(topic,disname,selector,false);
            	}
            	
                threadLocal.set(consumer);
            }
            while(true&&!Thread.interrupted()){
            	try {
                Thread.sleep(100);
                TextMessage msg = (TextMessage) consumer.receive();
                if(msg!=null) {
                	
                    //System.out.println(Thread.currentThread().getName()+": Consumer Msg"+msg.getText()+"--->"+count.getAndIncrement()+"selector="+selector);
                   if(!selector.equals(""))System.out.println(Thread.currentThread().getName()+": Consumer Msg "+msg.getText()+"--->"+count.getAndIncrement()+" selector="+selector+" msgtype="+MessageType.getEventType(msg.getIntProperty(Constants.eventtype)));
                   else
                   {
                //	   System.out.println(Thread.currentThread().getName()+": Consumer Msg: "+msg.getText());
                	//   System.out.println("read a input  to acknowledge message");
                	   byte[] buffer = new byte[1024];
                 //  	System.in.read(buffer);
                       msg.acknowledge();
                       //System.out.println("send back acknowledge to server");
                	   if(handler!=null)
                	   {
                		   handler.onMessageHandler(msg.getText());
                	   }
                   }
                }else {
                    break;
                }
                
            	}
            	catch (InterruptedException e) {
            		System.out.println("received interrupted, exit consumer");
            		return;
                }
            	catch(Exception e)
            	{
            		System.out.println(e.getMessage());
            	}
            	
            	
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } 
        finally
    	{
    		try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    }

}

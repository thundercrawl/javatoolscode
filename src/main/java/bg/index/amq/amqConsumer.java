package bg.index.amq;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.EmptyBroker;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.ManagedRegionBroker;
import org.apache.activemq.broker.jmx.ManagedTopicRegion;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.broker.region.RegionBroker;
import org.apache.activemq.command.ActiveMQDestination;

public class amqConsumer {


    private static final String USERNAME = "admin";

    private static final String PASSWORD = "passw0rd";

    private static final String BROKEN_URL = Constants.brokeruri;
    
    private boolean durableConsumer = false;
    static  ConnectionFactory connectionFactory;

    static Connection connection;
    Topic topic;
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
    	init();
    }
    
    @SuppressWarnings("unused")
	public static void checkTopicName(String n) throws IOException, MalformedObjectNameException, InstanceNotFoundException, MBeanException, ReflectionException
    {
    	JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
    	JMXConnector jmxc = JMXConnectorFactory.connect(url);
    	MBeanServerConnection conn = jmxc.getMBeanServerConnection();
    	ObjectName activeMQ = new ObjectName("org.apache.activemq:type=Broker,brokerName=odpubsub");
    	BrokerViewMBean mbean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(conn, activeMQ,BrokerViewMBean.class, true);
    	Boolean find = false;
    	for(    ObjectName name:mbean.getTopicProducers())
    	{
    		System.out.println(name.getCanonicalName());
    	}
    	for (ObjectName name : mbean.getTopics()) {
    		
//    	    QueueViewMBean queueMbean = (QueueViewMBean)
//    	           MBeanServerInvocationHandler.newProxyInstance(, name, TopicViewMBean.class, true);
    		if(name.getCanonicalName().lastIndexOf(n) >0)
    		{
    			System.out.println(name.getCanonicalName());
    			System.out.println("find the topic name");
    			find = true;
    			break;
    		}
    	} 
    	if(!find)System.out.println("not find the topic %s, that is an error "+n);
    	
    	
    }
    private  synchronized Connection getConnection() throws JMSException
    {
    	if(connection !=null)
    	{
    	
    	}
    	else
    	{
    		if(connectionFactory!=null)
    		{
    			connection = connectionFactory.createConnection(amqConsumer.USERNAME,amqConsumer.PASSWORD);
    		}
    		else
    		{
    			connectionFactory = new ActiveMQConnectionFactory();
    			connection = connectionFactory.createConnection(USERNAME,PASSWORD);
    		}
    		connection.setClientID(Constants.consumerMain);
            connection.start();
    	}
    	
    	return connection;
    }
    public void init(){
        try {
        
        	getConnection();

            session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
           
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    public void setListener()
    {
    	
    }
    public  void  setMessageHandler(MessageHandler handler)
    {
    	this.handler = handler;
    }

    public void getMessage(String disname ,String selector){
        try {
        	if(topic==null)
        		topic = session.createTopic(disname);
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

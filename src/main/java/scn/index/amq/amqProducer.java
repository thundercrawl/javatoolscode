package scn.index.amq;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class amqProducer {

    //ActiveMq user
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //ActiveMq default pwd
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //ActiveMQ address
    private static final String BROKEN_URL = Constants.brokeruri;

    AtomicInteger count = new AtomicInteger(0);
  
    ConnectionFactory connectionFactory;
   
    Connection connection;
   
    Session session;
    ThreadLocal<MessageProducer> threadLocal = new ThreadLocal<>();

    public void init(){
        try {
           
            connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEN_URL);
          
            connection  = connectionFactory.createConnection();
           
            connection.start();
         
            session = connection.createSession(true,Session.SESSION_TRANSACTED);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String disname){
        try {
           
            Topic tp = session.createTopic(disname);
           
            MessageProducer messageProducer = null;
            if(threadLocal.get()!=null){
                messageProducer = threadLocal.get();
            }else{
                messageProducer = session.createProducer(tp);
                threadLocal.set(messageProducer);
            }
           while(true){
                //Thread.sleep(1000);
                int num = count.getAndIncrement();
                
                TextMessage msg = session.createTextMessage(Thread.currentThread().getName()+
                        "Domino01/App"+num);
                int type = MessageType.getEventType((int)( Math.random()*4)).toInt();
                msg.setIntProperty(Constants.eventtype,  type);
                System.out.println(Thread.currentThread().getName()+
                        "productor，,count:"+num+" messagetype="+MessageType.getEventType(type).toString());
               
                messageProducer.send(msg);
                
                session.commit();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
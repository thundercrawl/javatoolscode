package scn.index.main;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import bg.index.amq.Constants;
import bg.index.amq.amqConsumer;
import bg.index.amq.amqProducer;
import lotus.notes.NotesThread;

/**
 *Set listener model
 *
 */
public class App 
{
	
	public final static String msgbody="";
	public final static String topic="test_005";
    public static void main( String[] args )
    {
        if( args.length != 0)
        {
        	System.out.println(args[0]);
        }
        else
        	System.out.println("need args, producer or comumer");
        
        if(args[0].equals("jmx"))
        {
            try {
    			try {
    				amqConsumer.checkTopicName(topic);
    			} catch (MalformedObjectNameException | InstanceNotFoundException | MBeanException
    					| ReflectionException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}finally
            {
    			return;
            }
        }
        
       // amqProducer producer = new amqProducer();
       // producer.init();
        amqConsumer consumer = new amqConsumer(true);
    
       consumer.init();
       
        ExecutorService executor = Executors.newFixedThreadPool(2000);
        if(args[0].equals(Constants.producer))
        {	System.out.println("--producer generate ADD/DELETE/CREATE/MODIFY events in random--");
        	Integer index = new Integer((int) (Math.random()*500));
        	
        //	executor.execute(new App().new ProductorMq(producer,App.topic+1,10));
        
        }
        else if(args[0].equals(Constants.consumer))
        {
        	System.out.println("--consumer register for  ADD/DELETE/CREATE/MODIFY --");
        	int index = 0;
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	/*
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	executor.execute(new App().new ConsumerMq(new amqConsumer(true),"",App.topic+(index++)));
        	
        	/*
	        executor.execute(new App().new ConsumerMq(consumer,Constants.addSelector) );
	        executor.execute(new App().new ConsumerMq(consumer,Constants.createSelector) );
	        executor.execute(new App().new ConsumerMq(consumer,Constants.addSelector) );
	        executor.execute(new App().new ConsumerMq(consumer,Constants.deleteSelector) );
	        executor.execute(new App().new ConsumerMq(consumer,Constants.unknowTypeSelector) );
	        executor.execute(new App().new ConsumerMq(consumer,""));
	        */
        }
        
    }

    
    public class ProductorMq implements Runnable{
    	amqProducer producter;
    	private String tp;
    	private Integer mesageNum;
        public ProductorMq(amqProducer producter,String topicname,Integer messageNum){
            this.producter = producter;
            this.tp = topicname;
            this.mesageNum = messageNum;
        }

        @Override
        public void run() {
            while(true){
                try {
                    producter.sendMessage(tp,mesageNum);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
  

     public class ConsumerMq implements Runnable{
    	 amqConsumer comsumer;
    	 String selector;
    	 String topicname;
    	 private boolean quit =false;
         public ConsumerMq(amqConsumer comsumer,String selector,String topicname){
        	 this.topicname = topicname;
             this.comsumer = comsumer;
             this.selector = selector;
         }
       
         @Override
         public void run() {
        	 try {
        	 //NotesThread.sinitThread();
       
             while(true&&!quit){
                 try {
                    comsumer.getMessage(topicname,selector);
                	
                     Thread.sleep(100);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
            
         }
         
         catch(Exception e)
         {
        	 
         }
        finally
        {
        	// NotesThread.stermThread();
        }
        	 
     }

     }

}

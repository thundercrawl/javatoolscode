package scn.index.amq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lotus.notes.NotesThread;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public final static String msgbody="";
	public final static String topic="scn_domino";
    public static void main( String[] args )
    {
        if( args.length != 0)
        {
        	System.out.println(args[0]);
        }
        else
        	System.out.println("need args, producer or comumer");
        amqProducer producer = new amqProducer();
        producer.init();
        amqConsumer consumer = new amqConsumer(true);
        consumer.init();
        ExecutorService executor = Executors.newFixedThreadPool(6);
        if(args[0].equals(Constants.producer))
        {	System.out.println("--producer generate ADD/DELETE/CREATE/MODIFY events in random--");
        	executor.execute(new App().new ProductorMq(producer));
        }
        else if(args[0].equals(Constants.consumer))
        {
        	System.out.println("--consumer register for  ADD/DELETE/CREATE/MODIFY --");
        	executor.execute(new App().new ConsumerMq(consumer,""));
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
        public ProductorMq(amqProducer producter){
            this.producter = producter;
        }

        @Override
        public void run() {
            while(true){
                try {
                    producter.sendMessage(App.topic);
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
    	 private boolean quit =false;
         public ConsumerMq(amqConsumer comsumer,String selector){
             this.comsumer = comsumer;
             this.selector = selector;
         }
       
         @Override
         public void run() {
        	 try {
        	 //NotesThread.sinitThread();
       
             while(true&&!quit){
                 try {
                     comsumer.getMessage(App.topic,selector);
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

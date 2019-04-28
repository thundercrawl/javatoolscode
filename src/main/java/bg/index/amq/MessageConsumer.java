package bg.index.amq;

public interface MessageConsumer {

	public void init();
	public String getMessage();
	public void sendACK();
	public void ListenOnTopic(String topic);
	public void close();
}

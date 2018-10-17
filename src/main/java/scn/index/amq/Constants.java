package scn.index.amq;

public interface Constants {
	
	/*app type*/
	public static final String consumer="consumer";
	public static final String producer="producer";
	
	/*For server define*/
	public static final String brokeruri = "tcp://10.134.100.185:61616";
	/*For event defination*/
	public static final String eventtype="EVENT_TYPE";
	public static final String eventuser="EVENT_USER";
	
	
	
	/*For event selector*/
	public static final String deleteSelector=eventtype+"=0";
	public static final String addSelector=eventtype+"=1";
	public static final String createSelector=eventtype+"=2";
	public static final String modifySelector=eventtype+"=3";
	public static final String unknowTypeSelector=eventtype+"=-1";
	
	/* connection client id*/
	public static final String consumerMain="SCNConsumerMain";
}

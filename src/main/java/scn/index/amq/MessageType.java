package scn.index.amq;

public enum MessageType {
	Delete(0),ADD(1),Create(2),Modify(3),UnKnownEvent(-1);
	
	private int type;
	MessageType(int type)
	{
		this.type = type;
		
	}
	
	public static MessageType getEventType(int value)
	   {
	      if (value == 0)
	         return Delete;
	      else if (value == 1) return ADD;
	      else if (value == 2) return Create;
	      else if (value == 3) return Modify;

	      return UnKnownEvent;
	   }
	public int toInt()
	{
		return type;
	}
	public String toString()
	{
		if (type == 0)
	         return "Delete";
	      else if (type == 1) return "ADD";
	      else if (type == 2) return "Create";
	      else if (type == 3) return "Modify";
		return "UnKnownEvent";
	}
}

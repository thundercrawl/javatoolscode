package bg.index.amq;

public abstract   class UserConnectionFactory {

	
	 public abstract  UserConnection  GetConnection(UserSession session);
	 
	 public abstract  UserConnection  RegisterConnection(UserSession session);
}

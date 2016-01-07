package basic.e2evalidation;

/**
 * an abstract class for users to follow to implement themselves' EndpointObject
 * class that can be used by {@link clients.E2EValidationClient}
 * 
 * @author zhangyutao
 *
 */
public abstract class EndpointObject {
	public abstract String[] getValues();

	public abstract String[] getValues(String key);
}

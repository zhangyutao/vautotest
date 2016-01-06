package basic;

public abstract class ServerComparison extends Comparison {
	public abstract void compare(EndpointObject expObj, EndpointObject actObj) throws Exception;

	public abstract String getComparisonResult() throws Exception;
}

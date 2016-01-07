package basic;

import basic.e2evalidation.EndpointObject;

public abstract class Comparison {
	public abstract void compare(EndpointObject expObj, EndpointObject actObj) throws Exception;

	public abstract String getComparisonResult() throws Exception;
}

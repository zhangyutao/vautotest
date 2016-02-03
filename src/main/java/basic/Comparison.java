package basic;

import basic.e2evalidation.EndpointObject;

public abstract class Comparison {
	public abstract void compare(EndpointObject expObj, EndpointObject actObj);

	public abstract String getComparisonResult();
}

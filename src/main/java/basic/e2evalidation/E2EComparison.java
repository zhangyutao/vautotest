package basic.e2evalidation;

import basic.Comparison;

public abstract class E2EComparison extends Comparison {
	public abstract void compare(EndpointObject expObj, EndpointObject actObj) throws Exception;

	public abstract String getComparisonResult() throws Exception;
}

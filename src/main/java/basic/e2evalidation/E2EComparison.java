package basic.e2evalidation;

import basic.Comparison;

/**
 * an abstract class for users to follow to implement themselves' E2EComparison
 * class that can be used by {@link clients.E2EValidationClient}
 * 
 * @author zhangyutao
 *
 */
public abstract class E2EComparison extends Comparison {
	public abstract void compare(EndpointObject expObj, EndpointObject actObj);

	public abstract String getComparisonResult();
}

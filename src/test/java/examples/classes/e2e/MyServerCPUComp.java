package examples.classes.e2e;

import basic.e2evalidation.EndpointObject;
import basic.e2evalidation.E2EComparison;

public class MyServerCPUComp extends E2EComparison {
	private String result;

	@Override
	public void compare(EndpointObject expObj, EndpointObject actObj) throws Exception {
		// TODO Auto-generated method stub
		if (expObj.getValues()[0].equals(expObj.getValues()[0])) {
			this.result = "passed";
		} else {
			this.result = "failed";
		}

	}

	@Override
	public String getComparisonResult() throws Exception {
		// TODO Auto-generated method stub
		return this.result;
	}

}

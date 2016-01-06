package business;

import basic.EndpointObject;
import basic.ServerComparison;

public class MyServerCPUComp extends ServerComparison {
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

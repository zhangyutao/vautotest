package examples.classes.e2e;

import basic.e2evalidation.EndpointObject;

public class MyServerObj extends EndpointObject {

	@Override
	public String[] getValues() {
		// TODO Auto-generated method stub
		return new String[] { "CPU is 4 core" };
	}

	@Override
	public String[] getValues(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}

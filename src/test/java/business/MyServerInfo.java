package business;

import basic.EndpointObject;

public class MyServerInfo extends EndpointObject {

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

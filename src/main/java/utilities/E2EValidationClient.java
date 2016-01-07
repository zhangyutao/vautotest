package utilities;

import java.util.ArrayList;

import basic.e2evalidation.EndpointObject;
import basic.e2evalidation.E2EComparison;
import basic.Client;
import basic.E2ECheckpointElements;

/**
 * the client used to execute server checkpoint command.
 * 
 * @author zhangyutao
 *
 */
public class E2EValidationClient implements Client {

	private String result;

	public E2EValidationClient() {
	}

	@Override
	public void execute(Object serverCheckpointElements) throws Exception {
		System.out.println("Start E2E validation.");
		ArrayList<Object> elements = ((E2ECheckpointElements) serverCheckpointElements).getElements();
		E2EComparison serverComparison = (E2EComparison) elements.get(2);
		serverComparison.compare((EndpointObject) elements.get(0), (EndpointObject) elements.get(1));
		result = serverComparison.getComparisonResult();
		System.out.println("E2E validation is over.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getResponse() throws Exception {
		return result;
	}

	@Override
	public void close() throws Exception {

	}
}

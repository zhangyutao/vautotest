package utilities;

import java.util.ArrayList;
import basic.ServerCheckpointElements;
import basic.Comparison;
import basic.EndpointObject;
import basic.Client;

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
		System.out.println("starts to end to end validation.");
		ArrayList<Object> elements = ((ServerCheckpointElements) serverCheckpointElements).getElements();
		Comparison serverComparison = (Comparison) elements.get(2);
		serverComparison.compare((EndpointObject) elements.get(0), (EndpointObject) elements.get(1));
		result = serverComparison.getComparisonResult();
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

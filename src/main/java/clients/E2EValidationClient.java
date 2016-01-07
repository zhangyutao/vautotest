package clients;

import java.util.ArrayList;
import basic.e2evalidation.EndpointObject;
import basic.e2evalidation.E2EComparison;
import basic.Client;
import basic.E2ECheckpointElements;

/**
 * the client used to execute {@link requests.E2EValidationRequest} and also can
 * be single instated to handle a end to end validation
 * 
 * @author zhangyutao
 *
 */
public class E2EValidationClient implements Client {

	private String result;

	public E2EValidationClient() {
	}

	/**
	 * @param Object
	 *            - it must be a E2ECheckpointElements class.
	 */
	@Override
	public void execute(Object serverCheckpointElements) throws Exception {
		System.out.println("Start E2E validation.");
		E2ECheckpointElements e2eCheckpointElements = (E2ECheckpointElements) serverCheckpointElements;
		ArrayList<Object> elements = ((E2ECheckpointElements) e2eCheckpointElements).getElements();
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

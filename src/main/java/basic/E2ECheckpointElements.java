package basic;

import java.util.ArrayList;

import basic.e2evalidation.E2EComparison;
import basic.e2evalidation.EndpointObject;

/**
 * an class for package information of E2E checkpoint that can be used by
 * {@link clients.E2EValidationClient}
 * 
 * @author zhangyutao
 *
 */
public class E2ECheckpointElements implements CheckpointElements {
	private ArrayList<Object> elements = new ArrayList<Object>();

	@Override
	public void setElements(ArrayList<Object> a) throws Exception {
		if (a.size() != 3) {
			throw new Exception("ServerCheckpointElements's element number must equal to 3.");
		} else if (EndpointObject.class.isAssignableFrom((Class<?>) a.get(0).getClass())
				& EndpointObject.class.isAssignableFrom((Class<?>) a.get(1).getClass())
				& E2EComparison.class.isAssignableFrom((Class<?>) a.get(2).getClass())) {
			this.elements = a;
		} else {
			throw new Exception("ServerCheckpointElements must be {ServerObject,ServerObject,ServerComparison}");
		}

	};

	@Override
	public ArrayList<Object> getElements() {
		return this.elements;
	};
}

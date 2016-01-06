package basic;

import java.util.ArrayList;

public class ServerCheckpointElements implements CheckpointElements {
	private ArrayList<Object> elements;

	@Override
	public void setElements(ArrayList<Object> a) throws Exception {
		if (a.size() != 3) {
			throw new Exception("ServerCheckpointElements's element number must equal to 3.");
		} else if (a.get(0) instanceof EndpointObject & a.get(1) instanceof EndpointObject
				& a.get(2) instanceof ServerComparison) {
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

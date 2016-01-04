package basic;

import java.util.ArrayList;

public interface ScenarioIO {
	// use to get a value by a key
	String getValue(String key);

	// use to get a value list by a key
	ArrayList<String> getValues(String key);
}

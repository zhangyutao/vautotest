package basic;

import java.util.ArrayList;

public abstract class ScenarioIO {
	// use to get a value by a key
	abstract String getValues();

	// use to get a value list by a key
	abstract ArrayList<String> getValues(String key);
}

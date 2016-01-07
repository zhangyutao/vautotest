package basic.scenario;

/**
 * an abstract class for users to follow to implement themselves' ScenarioIO
 * class that can be used by {@link clients.Scenario}
 * 
 * @author zhangyutao
 *
 */
public abstract class ScenarioIO {
	// use to get a values
	public abstract String[] getValues();

	// use to get a values by a key
	public abstract String[] getValues(String key);
}

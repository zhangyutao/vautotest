package basic.scenario;

public abstract class Scenario {

	/**
	 * Get the output. the output variable means the data after method "execute"
	 * and the variable would be defined by your class instance.and the method
	 * is a gate to external method to get the output of method "execute".
	 * 
	 * @return the data you got after invoke method "execute".
	 */
	public abstract ScenarioIO getOutput();

	/**
	 * set the Iutput. the input variable means the data you would use in method
	 * "execute" and the variable would be defined by your class instance.and
	 * the method is a a gate to external class to set your variable.
	 * 
	 * @param input
	 *            the data you will used in method "execute".
	 */
	public abstract void setInput(ScenarioIO input);

	/**
	 * get the status of the scenario. the variable would be defined by your
	 * class instance.and the method is a gate to exteranl method to get the
	 * status of method "execute".
	 * 
	 * @return
	 */
	public abstract ScenarioStatus getStatus();

	/**
	 * Define the steps which the would be executed.
	 * 
	 */
	public abstract void execute();
}

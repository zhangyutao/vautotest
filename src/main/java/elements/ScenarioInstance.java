
package elements;

import java.util.HashMap;
import basic.Scenario;
import basic.ScenarioIO;
import basic.ScenarioResult;
import utilities.ScenarioClient;

/**
 * the client used to execute a Scenario.java
 * 
 * @author zhangyutao
 * 
 * 
 */
public class ScenarioInstance {
	public ScenarioInstance() {

	}

	ScenarioClient scenarioClient;
	Scenario scenario;
	ScenarioIO datainput;
	int iteration;
	boolean isConcurrent;
	int timeout;

	public ScenarioClient getScenarioClient() {
		return scenarioClient;
	}

	public void setScenarioClient(ScenarioClient scenarioClient) {
		this.scenarioClient = scenarioClient;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public ScenarioIO getDatainput() {
		return datainput;
	}

	public void setDatainput(ScenarioIO datainput) {
		this.datainput = datainput;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public boolean isConcurrent() {
		return isConcurrent;
	}

	public void setConcurrent(boolean isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public HashMap<Integer, ScenarioResult> execute() {
		return this.scenarioClient.execute(this.scenario, this.datainput, this.iteration, this.isConcurrent,
				this.timeout);
	}
}

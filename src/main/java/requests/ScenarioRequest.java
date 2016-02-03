
package requests;

import java.util.ArrayList;
import java.util.HashMap;

import basic.scenario.Scenario;
import basic.scenario.ScenarioIO;
import basic.scenario.ScenarioResult;
import clients.ScenarioClient;

/**
 * this class is for defining a scenario which can be identified by
 * {@link factories.RequestsFactory} and it can be annotated by
 * {@link annotations.request.scenario.Properties}.
 * 
 * @author zhangyutao
 * 
 * 
 */
public class ScenarioRequest {
	public ScenarioRequest() {

	}

	ScenarioClient scenarioClient;
	Scenario scenario;
	ScenarioIO[] inputDatas;
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

	public ScenarioIO[] getInputDatas() {
		return inputDatas;
	}

	public void setInputDatas(ScenarioIO[] inputDatas) {
		this.inputDatas = inputDatas;
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

	public void execute() {
		ArrayList<Object> arg = new ArrayList<Object>();
		arg.add(this.scenario);
		arg.add(this.inputDatas);
		arg.add(this.iteration);
		arg.add(this.isConcurrent);
		arg.add(this.timeout);
		this.scenarioClient.execute(arg);

	}

	public HashMap<Integer, ScenarioResult> getResult() {
		// TODO Auto-generated method stub
		return this.scenarioClient.getResponse();

	}
}

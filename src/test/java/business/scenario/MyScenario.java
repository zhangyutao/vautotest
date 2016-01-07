package business.scenario;

import basic.scenario.Scenario;
import basic.scenario.ScenarioIO;
import basic.scenario.ScenarioStatus;

public class MyScenario extends Scenario {
	MyScenarioInput input;
	MyScenarioOutput output;
	ScenarioStatus status;

	@Override
	public ScenarioIO getOutput() {
		// TODO Auto-generated method stub
		return this.output;
	}

	@Override
	public void setInput(ScenarioIO input) {
		// TODO Auto-generated method stub
		this.input = (MyScenarioInput) input;
	}

	@Override
	public ScenarioStatus getStatus() {
		// TODO Auto-generated method stub
		return this.status;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		// use input here
		System.out.println(input.getValues()[0]);

		// write the steps to implement the scenario's all steps

		// return the output you want
		output = new MyScenarioOutput();

		// mark the status of the execution.
		this.status = ScenarioStatus.Passed;
	}

}

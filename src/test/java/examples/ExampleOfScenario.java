package examples;

import org.testng.annotations.Test;

import basic.scenario.ScenarioStatus;
import business.MyList;
import clients.ScenarioClient;
import factories.RequestsFactory;
import requests.ScenarioRequest;

public class ExampleOfScenario {

	@Test
	public void test() {

		// initiate all elements.

		ScenarioClient scenarioClient = new ScenarioClient();
		MyList list = RequestsFactory.initElements(scenarioClient, MyList.class);
		ScenarioRequest myCase = list.myScenario;

		// execute the case
		try {
			myCase.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// print some result of Scenario

		try {
			System.out.println("Iteration 1 result:" + myCase.getResult().get(1).getOutput().getValues()[0]);
			if (myCase.getResult().get(1).getStatus() == ScenarioStatus.Passed) {
				System.out.println("Passed");
			} else {
				System.out.println("not Passed");
			}

			System.out.println("Iteration 2 result:" + myCase.getResult().get(2).getOutput().getValues()[0]);
			if (myCase.getResult().get(2).getStatus() == ScenarioStatus.Passed) {
				System.out.println("Passed");
			} else {
				System.out.println("not Passed");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

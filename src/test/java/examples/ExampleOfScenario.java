package examples;

import org.testng.annotations.Test;

import business.MyList;
import elements.ScenarioInstance;
import elements.ServerCheckpoint;
import factories.ListFactory;
import utilities.E2EValidationClient;
import utilities.ScenarioClient;

public class ExampleOfScenario {

	@Test
	public void test() {

		// initiate all elements.

		ScenarioClient scenarioClient = new ScenarioClient();
		MyList validationList = ListFactory.initElements(scenarioClient, MyList.class);
		ScenarioInstance myValidation = validationList.myScenarioInstance;

		// print some information of elements

		// execute all elements
		try {
			myValidation.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// print some information of elements
		try {
			System.out.println(myValidation.getResult());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

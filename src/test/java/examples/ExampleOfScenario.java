package examples;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.testng.annotations.Test;

import basic.scenario.ScenarioStatus;
import clients.ScenarioClient;
import examples.classes.e2e.MyServerObj;
import examples.classes.lists.MyList;
import factories.RequestsFactory;
import javassist.bytecode.annotation.Annotation;
import requests.ScenarioRequest;

public class ExampleOfScenario {

	@Test
	public void test() {

		// initiate all elements.

		ScenarioClient scenarioClient = new ScenarioClient();

		HashMap<Field, Annotation[]> oldMap = RequestsFactory.getAnnotationsMap(MyList.class);
		HashMap<Field, Annotation[]> newAnnoMap = RequestsFactory.updateAnnotationMap(oldMap, "myserverCheckpoint",
				"Items", 1, new String[] { "expObj" }, new Class[] { MyServerObj.class });
		MyList list = RequestsFactory.initElementsOfUpdatedRequestList(scenarioClient, MyList.class, newAnnoMap);

		ScenarioRequest myCase = list.myScenario;

		// execute the case
		myCase.execute();

		// print some result of Scenario
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

	}
}

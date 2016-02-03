package examples;

import org.testng.annotations.Test;

import clients.E2EValidationClient;
import examples.classes.lists.MyList;
import factories.RequestsFactory;
import requests.E2EValidationRequest;

public class ExampleOfE2EValidation {

	@Test
	public void test() {

		// initiate all elements.
		E2EValidationClient e2eValidationClient = new E2EValidationClient();
		MyList list = RequestsFactory.initElements(e2eValidationClient, MyList.class);
		E2EValidationRequest myCheckpoint = list.myserverCheckpoint;

		// print some information of elements
		// execute the checkpoint
		myCheckpoint.execute();

		// print some result of the Checkpoint
		System.out.println(myCheckpoint.getResult());

	}
}

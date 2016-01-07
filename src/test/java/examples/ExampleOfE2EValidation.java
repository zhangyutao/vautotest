package examples;

import org.testng.annotations.Test;

import business.MyList;
import elements.E2ECheckpoint;
import factories.ListFactory;
import utilities.E2EValidationClient;

public class ExampleOfE2EValidation {

	@Test
	public void test() {

		// initiate all elements.

		E2EValidationClient e2eValidationClient = new E2EValidationClient();
		MyList validationList = ListFactory.initElements(e2eValidationClient, MyList.class);
		E2ECheckpoint myCheckpoint = validationList.myserverCheckpoint;

		// print some information of elements

		// execute the checkpoint
		try {
			myCheckpoint.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// print some result of the Checkpoint
		try {
			System.out.println(myCheckpoint.getResult());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

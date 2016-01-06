package examples;

import org.testng.annotations.Test;

import business.MyList;
import elements.ServerCheckpoint;
import factories.ListFactory;
import utilities.E2EValidationClient;

public class ExampleOfE2EValidation {

	@Test
	public void test() {

		// initiate all elements.

		E2EValidationClient e2eValidationClient = new E2EValidationClient();
		MyList validationList = ListFactory.initElements(e2eValidationClient, MyList.class);
		ServerCheckpoint myValidation = validationList.myserverCheckpoint;

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

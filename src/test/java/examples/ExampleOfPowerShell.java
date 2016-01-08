package examples;

import org.testng.annotations.Test;

import clients.PSClient;
import examples.classes.lists.MyList;
import factories.RequestsFactory;
import requests.CommandRequest;

public class ExampleOfPowerShell {

	@Test
	public void test() {

		// initiate all elements.
		PSClient psc = new PSClient();
		MyList list = RequestsFactory.initElements(psc, MyList.class);
		CommandRequest myPS = list.myCL;

		// print some information of command
		System.out.println(myPS.getLines());

		// execute command
		try {
			myPS.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// print some response of command
		try {
			System.out.println((String) myPS.getResponse());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

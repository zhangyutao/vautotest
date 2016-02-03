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
		myPS.execute();

		// print some response of command
		System.out.println((String) myPS.getResponse());

	}
}

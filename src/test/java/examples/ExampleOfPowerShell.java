package examples;

import org.testng.annotations.Test;

import business.MyList;
import elements.Command;
import factories.ListFactory;
import utilities.PSClient;

public class ExampleOfPowerShell {

	@Test
	public void test() {

		// initiate all elements.
		PSClient psc = new PSClient();
		MyList list = ListFactory.initElements(psc, MyList.class);
		Command myPS = list.myCL;

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

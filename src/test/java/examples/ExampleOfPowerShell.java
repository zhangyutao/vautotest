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
		MyList psList = ListFactory.initElements(psc, MyList.class);
		Command myPS = psList.myCL;

		// print some information of elements
		System.out.println(myPS.getLines());

		// execute all elements
		try {
			myPS.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// print some information of elements
		try {
			System.out.println((String) myPS.getResponse());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

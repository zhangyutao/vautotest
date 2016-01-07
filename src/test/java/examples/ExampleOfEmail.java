package examples;

import org.testng.annotations.Test;
import basic.EmailProvider;
import business.MyList;
import elements.Email;
import factories.ListFactory;
import utilities.EmailClient;

public class ExampleOfEmail {

	@Test
	public void test() {

		// initiate all elements.
		EmailClient ec = new EmailClient(EmailProvider.SMTP, "smtp.xxx.com");
		MyList list = ListFactory.initElements(ec, MyList.class);
		Email myEmail = list.myEmail;

		// print some information of elements

		System.out.println(String.valueOf(myEmail.getMessages()));

		// execute email
		try {

			myEmail.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// print some response of email
		try {
			System.out.println(String.valueOf(myEmail.getResponses()));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

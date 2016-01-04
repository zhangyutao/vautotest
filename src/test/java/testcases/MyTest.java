package testcases;

import org.testng.annotations.Test;

import basic.EmailProvider;
import elements.Command;
import elements.Email;
import elements.RestfRequest;
import factories.ListFactory;
import utilities.EmailClient;
import utilities.PSClient;
import utilities.RestfClient;

public class MyTest {

	@Test
	public void test() {

		PSClient psc = new PSClient();
		RestfClient rfc = new RestfClient();
		EmailClient ec = new EmailClient(EmailProvider.SMTP, "smtp3.hp.com");
		// initiate all elements.
		MyList psList = ListFactory.initElements(psc, MyList.class);
		Command myPS = psList.myCL;

		MyList rfList = ListFactory.initElements(rfc, MyList.class);
		RestfRequest myReq = rfList.myRF;

		MyList emailList = ListFactory.initElements(ec, MyList.class);
		Email myEmail = emailList.myEmail;

		// print some information of elements
		System.out.println(myPS.getLines());
		System.out.println(myReq.getRequest().getMethod().name());
		System.out.println(myReq.getRequest().getHeaders().get("content-type"));
		System.out.println(myReq.getRequest().getHeaders().get("accept-type"));
		System.out.println(myReq.getRequest().getCookies().get(0).getName());
		System.out.println(String.valueOf(myEmail.getMessages()));

		// execute all elements
		try {
			myPS.execute();
			myReq.execute();
			myEmail.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// print some information of elements
		try {
			System.out.println((String) myPS.getResponse());
			System.out.println(new
			String(myReq.getResponse().getResponseBody(), "gb2312"));
			System.out.println(String.valueOf(myEmail.getResponses()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

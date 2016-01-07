package examples;

import org.testng.annotations.Test;

import clients.RestfClient;
import factories.RequestsFactory;
import requests.RestfRequest;
import samples.MyList;

public class ExampleOfRestfulWebservice {

	@Test
	public void test() {

		// initiate all elements.
		RestfClient rfc = new RestfClient();
		MyList list = RequestsFactory.initElements(rfc, MyList.class);
		RestfRequest myReq = list.myRF;

		// print some information of Restful-Webservice request
		System.out.println(myReq.getRequest().getMethod().name());
		System.out.println(myReq.getRequest().getHeaders().get("content-type"));
		System.out.println(myReq.getRequest().getHeaders().get("accept-type"));
		System.out.println(myReq.getRequest().getCookies().get(0).getName());

		// execute the Restful-Webservice request
		try {
			myReq.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// print some response of RestfulWebservice request
		try {
			System.out.println(new String(myReq.getResponse().getResponseBody(), "gb2312"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

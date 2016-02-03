package examples;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.testng.annotations.Test;
import clients.RestfClient;
import examples.classes.lists.MyList;
import factories.RequestsFactory;
import javassist.bytecode.annotation.Annotation;
import requests.RestfRequest;

public class ExampleOfRestfulWebservice {

	@Test
	public void test() {

		// initiate all elements.
		RestfClient rfc = new RestfClient();
		HashMap<Field, Annotation[]> oldMap = RequestsFactory.getAnnotationsMap(MyList.class);
		HashMap<Field, Annotation[]> newAnnoMap = RequestsFactory.updateAnnotationMap(oldMap, "myRF", "Header", 1,
				new String[] { "value" }, new String[] { "accept jsonxx" });
		MyList list = RequestsFactory.initElementsOfUpdatedRequestList(rfc, MyList.class, newAnnoMap);

		RestfRequest myReq = list.myRF;
		// print some information of Restful-Webservice request
		System.out.println(myReq.getRequest().getMethod().name());
		System.out.println(myReq.getRequest().getUrl());
		System.out.println(myReq.getRequest().getCookies().get(0).getName());
		System.out.println(myReq.getRequest().getHeaders().get("content-type"));
		System.out.println(myReq.getRequest().getHeaders().get("accept-type"));

		// execute the Restful-Webservice request
		myReq.execute();

		// print some response of RestfulWebservice request
		try {
			System.out.println(new String(myReq.getResponse().getResponseBody(), "gb2312"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

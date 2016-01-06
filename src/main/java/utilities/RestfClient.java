
package utilities;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.wiztools.restclient.bean.Request;
import org.wiztools.restclient.bean.Response;
import com.hp.ecs.restful.RestfulClient;

import basic.Client;

/**
 * the client used to execute restful webservice request. this class is using a
 * restclient tool to handle the requst and reponse. some properites like proxy
 * you can configured in the file which path is
 * "%userprofile%\.rest-client\rest-client.properties", that file will be
 * generated after you executed a request on time. if it does not appear, please
 * add it mannually.
 * 
 * @author zhangyutao
 *
 */
public class RestfClient implements Client {

	private Response rsp = null; // Response
	private RestfulClient restfulClient;

	public RestfClient() {
		this.restfulClient = new RestfulClient();
	}

	/**
	 * to return the object {@link #rsp}. author: zhangyutao create on
	 * 2014-03-04
	 * 
	 * @return return the object {@link #rsp}
	 */
	@SuppressWarnings("unchecked")
	public Response getResponse() {
		return rsp;
	}

	/**
	 * to return the response body in string format. author: zhangyutao create
	 * on 2015-01-05
	 * 
	 * @return return the responsebody
	 */
	public String getResponseBody() {
		String body = new String(rsp.getResponseBody());
		return body;
	}

	/**
	 * to be called in child class and get the child class's parameters for
	 * other method. author: zhangyutao create on 2014-03-04
	 * 
	 * @param methodName
	 *            the method of request like post, update and so on
	 * @param requestUrl
	 *            the request url
	 * @param requestHeaders
	 *            a map put the request headers
	 * @param requestCookies
	 *            a map put the request cookies
	 * @param stringBody
	 *            stringBody
	 * @param isSsl
	 *            isSsl
	 * @param auth
	 *            auth a map put inforamtion of authrioty.
	 * @see com.hp.ecs.restful.RestfulClient
	 */
	public Request setupRequest(String methodName, String requestUrl, Map<String, String> requestHeaders,
			Map<String, String> requestCookies, String stringBody, Boolean isSsl, Map<String, String> auth) {

		return restfulClient.setupRequest(methodName, requestUrl, requestHeaders, requestCookies, stringBody, isSsl,
				auth);
	}

	/**
	 * to be called in child class and get the child class's parameters for
	 * other method. author: wei.sun13@hp.com
	 * 
	 * @param methodName
	 *            the method of request like post, update and so on
	 * @param requestUrl
	 *            the request url
	 * @param requestHeaders
	 *            a map put the request headers
	 * @param requestCookies
	 *            a map put the request cookies
	 * @param stringBody
	 *            stringBody
	 * @param isSsl
	 *            isSsl a map put inforamtion of authrioty.
	 * @see com.hp.ecs.restful.RestfulClient
	 */
	public Request setupRequest(String methodName, String requestUrl, Map<String, String> requestHeaders,
			Map<String, String> requestCookies, String stringBody, Boolean isSsl) {
		return restfulClient.setupRequest(methodName, requestUrl, requestHeaders, requestCookies, stringBody, isSsl);
	}

	/**
	 * To execute request according to you provide and then get response and put
	 * in {@link #rsp}<br>
	 * this method will send diferent request according to the request
	 * information you give in method "setCard()" like
	 * {@link #setCard(String, String, String, String, Map, Map, String, boolean, Map)}
	 * . author: zhangyutao create on 2014-03-04
	 * 
	 * @throws Exception
	 * 
	 * @see #setCard(String, String, String, String, Map, Map, String, boolean,
	 *      Map)
	 */
	@Override
	public void execute(Object request) throws Exception {
		System.out.println("Execute Resf-webservice request");

		Response response = null;
		response = restfulClient.executeRequest((Request) request);
		// Reporter.reportAMessage("API request execution end");

		if (response != null) {
			System.out.println("Got response");
			if (response.getStatusCode() == 200) {
				System.out.println("StatusCode is 200");
			} else {
				String body = "";
				try {
					body = new String(response.getResponseBody(), "gb2312");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				throw new Exception("StatusCode shall be 200, but it now is: " + response.getStatusLine()
						+ "  and Response body is \"" + body + "\"");
				// Reporter.reportAMessage("Response body is
				// \""+response.getResponseBody().toString()+"\"");
			}
		} else {
			throw new Exception("Cannot get response");
		}
		this.rsp = response;
		System.out.println("Get Resf-webservice response.");
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

}

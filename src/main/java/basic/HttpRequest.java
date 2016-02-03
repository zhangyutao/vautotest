package basic;

import java.util.Map;

import org.wiztools.restclient.bean.Request;
import org.wiztools.restclient.bean.Response;

/**
 * this class would support all http/https type's request
 * 
 * @author zhanyuta
 *
 */
public interface HttpRequest {
	public Request getRequest();

	public void setRequest(String methodName, String requestUrl, Map<String, String> requestHeaders,
			Map<String, String> requestCookies, String stringBody, Boolean isSsl, Map<String, String> auth);

	public void setRequest(String methodName, String requestUrl, Map<String, String> requestHeaders,
			Map<String, String> requestCookies, String stringBody, Boolean isSsl);

	public Client getClient();

	public void setClient(Client cc);

	public void execute();

	public Response getResponse();
}

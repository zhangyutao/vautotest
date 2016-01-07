package requests;

import java.util.Map;

import org.wiztools.restclient.bean.Request;
import org.wiztools.restclient.bean.Response;

import basic.Client;
import basic.HttpRequest;
import clients.RestfClient;

/**
 * this class is for defining a restful webservice request which can be
 * identified by {@link factories.RequestsFactory} and it can be annotated by
 * {@link annotations.request.restf.Auth} {@link annotations.request.restf.Body}
 * {@link annotations.request.restf.Cookie}
 * {@link annotations.request.restf.Header}
 * {@link annotations.request.restf.Method}
 * {@link annotations.request.restf.URL}
 * {@link annotations.request.restf.UseSSL}
 * 
 * @author Linus
 *
 */
public class RestfRequest implements HttpRequest {
	private Request content;
	private RestfClient client;

	@Override
	public Request getRequest() {
		// TODO Auto-generated method stub
		return content;
	}

	@Override
	public void setRequest(String methodName, String requestUrl, Map<String, String> requestHeaders,
			Map<String, String> requestCookies, String stringBody, Boolean isSsl, Map<String, String> auth) {
		this.content = client.setupRequest(methodName, requestUrl, requestHeaders, requestCookies, stringBody, isSsl,
				auth);
	}

	@Override
	public void setRequest(String methodName, String requestUrl, Map<String, String> requestHeaders,
			Map<String, String> requestCookies, String stringBody, Boolean isSsl) {
		this.content = client.setupRequest(methodName, requestUrl, requestHeaders, requestCookies, stringBody, isSsl);
	}

	@Override
	public RestfClient getClient() {
		return this.client;
	}

	@Override
	public void setClient(Client cc) {
		this.client = (RestfClient) cc;
	}

	@Override
	public void execute() throws Exception {
		this.client.execute(this.content);

	}

	@Override
	public Response getResponse() throws Exception {
		return this.client.getResponse();

	}

}

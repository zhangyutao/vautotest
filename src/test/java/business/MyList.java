package business;

import annotations.checkpoint.Elements;
import annotations.cmd.Line;
import annotations.email.Message;
import annotations.httprequest.Body;
import annotations.httprequest.Cookie;
import annotations.httprequest.Header;
import annotations.httprequest.Method;
import annotations.httprequest.URL;
import annotations.httprequest.UseSSL;
import elements.Command;
import elements.Email;
import elements.RestfRequest;
import elements.ScenarioInstance;
import elements.ServerCheckpoint;

public class MyList {

	@Line("echo \"test1\"")
	@Line("echo \"test2\"")
	public Command myCL;
	@Method("get")
	@URL("https://www.xxx.com")
	@Body("a test")
	@UseSSL(false)
	@Header(key = "content-type", value = "json content")
	@Header(key = "accept-type", value = "accept json")
	@Cookie(key = "testcookie", value = "it is a test")
	public RestfRequest myRF;

	@Message(from = "yu-tao.zhang@xxx.com", to = "yu-tao.zhang@xxx.com", subject = "test1", textBody = "test body", attachmentsPath = {
			"c:\\test.txt", "c:\\test1.txt" })
	@Message(from = "yu-tao.zhang@xxx.com", to = "yu-tao.zhang@xxx.com", subject = "test2", textBody = "test body")
	public Email myEmail;

	@Elements(expObj = MyCustomerDBInfo.class, actObj = MyServerInfo.class, comparison = MyServerCPUComp.class)
	public ServerCheckpoint myserverCheckpoint;
	
	@Elements(expObj = MyCustomerDBInfo.class, actObj = MyServerInfo.class, comparison = MyServerCPUComp.class)
	public ScenarioInstance myScenarioInstance;
}

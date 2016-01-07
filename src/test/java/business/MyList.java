package business;

import annotations.checkpoint.Items;
import annotations.cmd.Line;
import annotations.email.Message;
import annotations.httprequest.Body;
import annotations.httprequest.Cookie;
import annotations.httprequest.Header;
import annotations.httprequest.Method;
import annotations.httprequest.URL;
import annotations.httprequest.UseSSL;
import annotations.scenario.Properties;
import business.e2e.MyCustomerDBObj;
import business.e2e.MyServerCPUComp;
import business.e2e.MyServerObj;
import business.scenario.MyScenario;
import business.scenario.MyScenarioInput;
import elements.Command;
import elements.Email;
import elements.RestfRequest;
import elements.Case;
import elements.E2ECheckpoint;

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

	@Items(expObj = MyCustomerDBObj.class, actObj = MyServerObj.class, comparison = MyServerCPUComp.class)
	public E2ECheckpoint myserverCheckpoint;

	@Properties(scenarioClass = MyScenario.class, scenarioInput = MyScenarioInput.class, iteration = 2, isConcurrent = false, timeout = 180000)
	public Case myCase;
}

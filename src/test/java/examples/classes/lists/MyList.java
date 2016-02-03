package examples.classes.lists;

import annotations.request.cmd.Line;
import annotations.request.e2evalidation.Items;
import annotations.request.email.Message;
import annotations.request.restf.Body;
import annotations.request.restf.Cookie;
import annotations.request.restf.Header;
import annotations.request.restf.Method;
import annotations.request.restf.URL;
import annotations.request.restf.UseSSL;
import annotations.request.scenario.Properties;
import examples.classes.e2e.MyCustomerDBObj;
import examples.classes.e2e.MyServerCPUComp;
import examples.classes.e2e.MyServerObj;
import examples.classes.scenario.MyScenario;
import examples.classes.scenario.MyScenarioInput;
import requests.CommandRequest;
import requests.E2EValidationRequest;
import requests.EmailRequest;
import requests.RestfRequest;
import requests.ScenarioRequest;

public class MyList {

	@Line("echo \"test1\"")
	@Line("echo \"test2\"")
	public CommandRequest myCL;
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
	public EmailRequest myEmail;

	@Items(expObj = MyCustomerDBObj.class, actObj = MyServerObj.class, comparison = MyServerCPUComp.class)
	public E2EValidationRequest myserverCheckpoint;

	@Properties(scenario = MyScenario.class, inputDatas = { MyScenarioInput.class,
			MyScenarioInput.class }, iteration = 2, isConcurrent = false, timeout = 180000)
	public ScenarioRequest myScenario;

}

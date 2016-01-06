package testcases;

import org.testng.annotations.Test;

public class Mytest2 {
	@Test
	public void test() throws Exception {
	String th = "sf0.0 0.0.0.0 sdfsd";
	String re = "0.0.0.0".replaceAll("\\.", "\\\\.");
	th = th.replaceAll(re, "123.22.12.34");
	System.out.println(th);
	}
}

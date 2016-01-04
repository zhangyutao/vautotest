package testcases;



import org.testng.annotations.Test;

import basic.Config;
import utilities.WinAuto;

public class Mytest2 {
	@Test
	public void test() throws Exception {
		try {
    		WinAuto me = new WinAuto("output");
    	String w = Config.artifactId;
			w ="ee";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

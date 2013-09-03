package com.org.testautomation.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

@RunWith(Parameterized.class)
public class DOHRunner extends BaseTest{
	
	private String testKey;
	private WebDriver driver;
	private String lastTestKey;
	
	public DOHRunner(JSONObject env,WebDriver driver,String testKey,String lastTestKey){
		super(env);
		this.driver = driver;
		this.testKey = testKey;
		this.lastTestKey = lastTestKey;
	}
	
	@Parameters(name = "{2}")
	public static Collection<Object[]> data() {
		//Get execution environments 
		Object[] executionEnvironments;
		WebDriver driver = null;
		try {
			executionEnvironments = BaseTest.createExecutionEnvironments("com/ibm/testautomation/config/DOHConfig.json");
			List<Object[]> testResults = new ArrayList<Object[]>();
			List<WebDriver> drivers = new ArrayList<WebDriver>();
			for(int x = 0; x < executionEnvironments.length; x++){
				JSONObject env = (JSONObject)executionEnvironments[x];
				driver = BaseTest.getDriver(env);
				drivers.add(driver);
				//register tests for each driver
				driver.manage().timeouts().setScriptTimeout(Long.parseLong(getProperty("scriptTimeoutSec",env)), TimeUnit.SECONDS);
				driver.get(getProperty("DOHRunnerUrl",env));
				
				try{	
				Object response = ((JavascriptExecutor)driver).executeAsyncScript("registerModule(arguments[0],arguments[1],arguments[2],arguments[arguments.length -1]);",getProperty("DOHModule",env),getProperty("environmentName",env),getProperty("browser",env));
				JSONArray testKeys = JSONArray.parse((String)response);
				if(testKeys.size() == 0){
					System.out.println("numTests = 0");
					driver.quit();
				}//end if
				for(int y = 0; y < testKeys.size(); y++){
					Object[] data = new Object[4];
					data[0] = env;
					data[1] = driver;
					data[2] = testKeys.get(y);
					data[3] = testKeys.get(testKeys.size() - 1);
					testResults.add(data);
				}//end for
				}catch(Exception e){
					System.out.println("Could not register packages: " + e.getMessage());
					driver.quit();
				}//end try
			}//end for
			for(WebDriver d : drivers){
				((JavascriptExecutor)d).executeScript("runTests();");
			}//end for
			return testResults;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("DOHRunner.data() failed: " + e.getMessage());
			return null;
		}//end try
	}
	
	@Test
	public void test(){
		try {
			Object response = ((JavascriptExecutor)this.driver).executeAsyncScript("getTestResult(arguments[0],arguments[arguments.length - 1]);",this.testKey);
			JSONObject result = JSONObject.parse((String)response);
			StringBuffer b = new StringBuffer(this.testKey + " - ");
			if((Boolean)result.get("success")){
				b.append("PASS");
			}else{
				b.append("FAIL");
			}//end if
			System.out.println(b.toString());
			
			//Get test logs
			if(!(Boolean)result.get("success")){
				if(result.containsKey("hint")){
					Assert.fail((String)result.get("hint"));
				}else{
					Assert.fail();
				}//end if
			}//end if
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}finally{
			BaseTest.printJSLogs(this.driver,this.testKey);
			if(this.testKey.equals(this.lastTestKey)){
				this.driver.quit();
			}//end if
		}//end try
	}
	

}

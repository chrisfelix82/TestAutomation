package com.ibm.testautomation.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;


public class BaseTest {
	@Rule public TestName testName = new TestName();
	private JSONObject env;
	
	public BaseTest(JSONObject env) {
			this.env = env;
	}
	
	public String getProperty(String propertyName){
		//Check if property is system property
		String value = System.getenv("qm_" + propertyName);//Search for RQM injected property
		if(value == null){
			value = (String)this.env.get(propertyName);
		}//end if
		return value;
	}
	
	public static String getProperty(String propertyName,JSONObject env){
		//Check if property is system property
		String value = System.getenv("qm_" + propertyName);//Search for RQM injected property
		if(value == null){
			value = (String)env.get(propertyName);
		}//end if
		return value;
	}
	
	public synchronized void setProperty(String propertyName,String value){
		if(System.getenv().containsKey("qm_" + propertyName)){
			System.getenv().put("qm_" + propertyName, value);
		}else{
			this.env.put(propertyName,value);
		}//end if
	}
	
	protected static Object[] createExecutionEnvironments(String configFileClasspath) throws IOException{
		JSONObject config = JSONObject.parse(ClassLoader.getSystemResourceAsStream(configFileClasspath));
		if(System.getenv().containsKey("qm_env")){
			config.put("env",System.getenv("qm_env"));
		}//end if
		if(System.getenv().containsKey("qm_capabilities")){
			config.put("capabilities", JSONArray.parse(System.getenv("qm_capabilities")));
		}//end if
		
		JSONObject env = (JSONObject)config.get((String)config.get("env"));
		JSONArray capabilities = (JSONArray)config.get("capabilities");
		JSONArray executionEnvironments = new JSONArray();
		for(int x = 0; x < capabilities.size();x++){
			JSONObject o = JSONObject.parse(env.toString());
			JSONObject cap = (JSONObject)capabilities.get(x);
			@SuppressWarnings("rawtypes")
			Iterator iter = cap.keySet().iterator();
		    while(iter.hasNext()){
		    	String key = (String)iter.next();
		    	o.put(key,cap.get(key));
		    }//end while
			executionEnvironments.add(o);
		}//end for
		return executionEnvironments.toArray();
	}
	
	@SuppressWarnings("rawtypes")
	protected static Object[] createExecutionEnvironments(String configFileClasspath,Class testClass) throws IOException{
		Object[] environments = BaseTest.createExecutionEnvironments(configFileClasspath);
		//Now see if there is a config file for the test class
		InputStream is = ClassLoader.getSystemResourceAsStream(testClass.getName().replace(".","/") + ".json");
		if(is == null){
			return environments;
		}else{
			JSONArray runs = new JSONArray();
			JSONArray testData = (JSONArray)JSONObject.parse(is).get("testData");
			for(int x = 0; x < testData.size(); x++){
				for(Object env : environments){
					JSONObject run = JSONObject.parse(testData.get(x).toString());
					Iterator iter = ((JSONObject)env).keySet().iterator();
					while(iter.hasNext()){
						String key = (String)iter.next();
						if(!run.containsKey(key)){
							//This allows test class to override global config
							run.put(key,((JSONObject)env).get(key));
						}//end if
					}//end while
					runs.add(run);
				}//end for
			}//end for
			is.close();
			return runs.toArray();
		}//end if
	}
	
	protected String getTestName(){
		return this.testName.getMethodName();
	}
	
	public static WebDriver getDriver(JSONObject env){
		// Get a handle to the driver. This will throw an exception
		// if a matching driver cannot be located
		WebDriver driver = null;
		try {
			if(getProperty("hubUrl",env) != null){
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setJavascriptEnabled(true);
				capabilities.setBrowserName(getProperty("browser",env));
				if(getProperty("platform",env) != null){
					capabilities.setPlatform(Platform.valueOf(getProperty("platform",env)));
				}//end if
				if(getProperty("browserVersion",env) != null){
					capabilities.setVersion(getProperty("browserVersion",env));
				}//end if
				driver = new RemoteWebDriver(new URL(getProperty("hubUrl",env)), capabilities);
			}else{
				if(getProperty("browser",env).equals(BrowserType.FIREFOX)){
					driver = new FirefoxDriver();
				}else if(getProperty("browser",env).equals(BrowserType.IEXPLORE)){
					driver = new InternetExplorerDriver();
				}else if(getProperty("browser",env).equals(BrowserType.CHROME)){
					driver = new ChromeDriver();
				}else if(getProperty("browser",env).equals(BrowserType.SAFARI)){
					driver = new SafariDriver();
				}else{
					System.out.println("Not a supported local browser.  Modify BaseTest to add it.  Using Firefox as default: " + getProperty("browser",env));
					driver = new FirefoxDriver();
				}//end if
			}//end if
				WebDriver ad = new Augmenter().augment(driver);
				// And now use it
				return ad;
		} catch (MalformedURLException e) {
			System.out.println("Could not creae a web driver: " + e.getMessage());
			return null;
		}//end try
	}
	
	public WebDriver getDriver() {

		// Get a handle to the driver. This will throw an exception
		// if a matching driver cannot be located
		WebDriver driver = null;
		try {
			if(getProperty("hubUrl") != null){
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setJavascriptEnabled(true);
				capabilities.setBrowserName(getProperty("browser"));
				if(getProperty("platform") != null){
					capabilities.setPlatform(Platform.valueOf(getProperty("platform")));
				}//end if 
				if(getProperty("browserVersion") != null){
					capabilities.setVersion(getProperty("browserVersion"));
				}//end if
				if(getProperty("app") != null){
					capabilities.setCapability("app",getProperty("app"));
				}//end if
				
				driver = new RemoteWebDriver(new URL(getProperty("hubUrl")), capabilities);
			}else{
				if(getProperty("browser").equals(BrowserType.FIREFOX)){
					driver = new FirefoxDriver();
				}else if(getProperty("browser").equals(BrowserType.IEXPLORE)){
					driver = new InternetExplorerDriver();
				}else if(getProperty("browser").equals(BrowserType.CHROME)){
					driver = new ChromeDriver();
				}else if(getProperty("browser").equals(BrowserType.SAFARI)){
					driver = new SafariDriver();
				}else{
					System.out.println("Not a supported local browser.  Modify BaseTest to add it.  Using Firefox as default: " + getProperty("browser"));
					driver = new FirefoxDriver();
				}//end if
			}//end if
			WebDriver ad = new Augmenter().augment(driver);
			// And now use it
			return ad;
		} catch (MalformedURLException e) {
			System.out.println("Could not creae a web driver: " + e.getMessage());
			return null;
		}//end try
	}
	
	public static void printJSLogs(WebDriver driver,String testKey){
		@SuppressWarnings("unchecked")
		List<String> logs = (List<String>)((JavascriptExecutor)driver).executeScript("return getLogs(arguments[0]);",testKey);
		if(logs != null){
		for(String msg : logs){
			System.out.println(msg);
		}//end for
		}//end if
	}
	
	protected void takeScreenshot(WebDriver driver,String fileName) {
		File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String imageFileDir = System.getProperty("selenium.screenshot.dir"); 
		if(imageFileDir == null){
			imageFileDir = System.getProperty("java.io.tmpdir");
		}//end if
		try {
			fileName =  fileName + ".png";
			FileUtils.copyFile(screenShot,new File(imageFileDir,fileName));
		} catch (IOException ioe) {
			throw new RuntimeException(ioe.getMessage(), ioe);
		}//end try
	}
	

}

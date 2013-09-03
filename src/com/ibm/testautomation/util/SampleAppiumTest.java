package com.ibm.testautomation.util;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.json.java.JSONObject;

public class SampleAppiumTest extends BaseUITest{
	
    private WebDriver driver;
    
	public SampleAppiumTest(JSONObject env,String environmentName,String browserName) {
		super(env);
	}

	@Parameters(name="{1},{2}")
	public static Collection<Object[]> data(){
		return BaseUITest.data(SampleAppiumTest.class);
	}

	@Before
	public void setup() throws Exception{
		this.driver = super.getDriver();
		this.driver.getWindowHandles();
		this.driver.switchTo().window("1");
	}
	
	@After
	public void tearDown() throws Exception {
		this.driver.quit();
	}

	@Test
	public void testEnterText() throws Exception {
		List<WebElement> inputs = this.driver.findElements(By.cssSelector(".mblIdxLoginPane .mblTextBox"));
		inputs.get(0).sendKeys(super.getProperty("username"));
		inputs.get(1).sendKeys(super.getProperty("password"));
		super.takeScreenshot(this.driver,"iOSScreen");
	}


}
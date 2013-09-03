package com.org.testautomation.util;

import java.util.Collection;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.json.java.JSONObject;

public class SampleUITest extends BaseUITest{
	
	public SampleUITest(JSONObject env,String environmentName,String browserName) {
		super(env);
	}
	
	@Parameters(name="{1},{2}")
	public static Collection<Object[]> data(){
		return BaseUITest.data(SampleUITest.class);
	}

	@Test
	public void google() {
		WebDriver driver = super.getDriver();
		try{

		// And now use this to visit Google
		driver.get("http://www.google.com");
		// Alternatively the same thing can be done like this
		// driver.navigate().to("http://www.google.com");

		// Find the text input element by its name
		WebElement element = driver.findElement(By.name("q"));

		// Enter something to search for
		element.sendKeys(super.getProperty("x"));    
		// Now submit the form. WebDriver will find the form for us from the
		// element
		element.submit();
		super.takeScreenshot(driver,"ValidateGoolgeSearch");
		}finally{
			driver.quit();
		}//end if
		

	}
	
}

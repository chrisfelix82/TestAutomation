package com.ibm.testautomation.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ibm.json.java.JSONObject;

@RunWith(Parameterized.class)
public class BaseUITest extends BaseTest{

	public BaseUITest(JSONObject env) {
		super(env);
	}
	
	@SuppressWarnings("rawtypes")
	public static Collection<Object[]> data(Class subclass) {
		//Get execution environments 
		Object[] executionEnvironments;
		try {
			executionEnvironments = BaseTest.createExecutionEnvironments("com/ibm/testautomation/config/SeleniumConfig.json",subclass);
			Object[][] data = new Object[executionEnvironments.length][3];
			for(int x = 0; x < executionEnvironments.length;x++){
				JSONObject env = (JSONObject)executionEnvironments[x];
				data[x][0] = env;
				data[x][1] = getProperty("environmentName",env);
				data[x][2] = getProperty("browser",env);
			}//end for
			return Arrays.asList(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}//end try
	}


	
}

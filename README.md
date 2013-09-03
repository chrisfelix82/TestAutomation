TestAutomation
==============

Provides a Selenium WebDriver with JUnit 4 framework for testing Web, hybrid and native apps.  The project intends to add a framework to better manage automated tests, and supports IBM Rational Quality Manager (RQM).

See com.org.testautomation.util.SampleAppiumTest for an example of hybrid application testing.  SampleUITest.java contains an example of regular web testing.

If you are using Rational Quality Manager (RQM) for test management, you may also be interested in having Dojo Objective Harness (DOH) test case results reflected in RQM reports.
The framework also provides this.  To set this up do the following:

1. Create a JUnit 4 Selenium based test in RQM, and point it to com.org.testautomation.util.DOHRunner
2. On an HTTP server of your choosing, add a source distribution of Dojo (see DOHConfig.json) for an example.  
3. On the HTTP server, also add the resources directory contents to the doh package
4. Register your custom app packages in the DOHRunnerUrl that you see in the DOHConfig.json file.
5. Run the RQM test case, and the results will be available in RQM!

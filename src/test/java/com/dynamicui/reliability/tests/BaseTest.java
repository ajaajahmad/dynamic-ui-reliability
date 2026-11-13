package com.dynamicui.reliability.tests;

import java.lang.reflect.Method;

import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.dynamicui.reliability.driver.DriverManager;

public abstract class BaseTest {

	@BeforeMethod(alwaysRun = true)
	public void startBrowser(Method method) {
		// Tags every log line from this thread; log4j2.xml routes tagged lines to a per-test file.
		ThreadContext.put("test", method.getName());
		DriverManager.start();
	}

	@AfterMethod(alwaysRun = true)
	public void stopBrowser() {
		try {
			DriverManager.quit();
		} finally {
			ThreadContext.remove("test");
		}
	}

	protected WebDriver driver() {
		return DriverManager.driver();
	}
}

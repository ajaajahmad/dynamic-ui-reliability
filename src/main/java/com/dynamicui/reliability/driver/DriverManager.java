package com.dynamicui.reliability.driver;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.dynamicui.reliability.config.TestConfig;

public final class DriverManager {

	private static final Logger LOG = LogManager.getLogger(DriverManager.class);

	// One browser per test thread, so parallel tests never share page state.
	private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

	private DriverManager() {
	}

	public static void start() {
		ChromeDriver driver = new ChromeDriver(chromeOptions());
		DRIVER.set(driver);
		LOG.debug("Started Chrome {} (headless={})", driver.getCapabilities().getBrowserVersion(),
				TestConfig.headless());
	}

	public static WebDriver driver() {
		return DRIVER.get();
	}

	public static void quit() {
		WebDriver driver = DRIVER.get();
		if (driver != null) {
			driver.quit();
			DRIVER.remove();
			LOG.debug("Closed the browser");
		}
	}

	private static ChromeOptions chromeOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--window-size=1440,900");
		// A date input's typing order follows the browser locale, so pin it to month/day/year everywhere.
		options.addArguments("--lang=en-US");
		options.setExperimentalOption("prefs", Map.of("intl.accept_languages", "en-US"));
		if (TestConfig.headless()) {
			options.addArguments("--headless=new");
		}
		return options;
	}
}

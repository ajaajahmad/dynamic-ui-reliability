package com.dynamicui.reliability.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dynamicui.reliability.config.TestConfig;

public class FormConfirmationPage {

	private static final Logger LOG = LogManager.getLogger(FormConfirmationPage.class);

	private final By confirmation = By.cssSelector("div.alert-info[role='alert']");

	private final WebDriverWait wait;

	public FormConfirmationPage(WebDriver driver) {
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public String message() {
		String text = wait.until(visibilityOfElementLocated(confirmation)).getText();
		LOG.debug("Confirmation shown: {}", text);
		return text;
	}
}

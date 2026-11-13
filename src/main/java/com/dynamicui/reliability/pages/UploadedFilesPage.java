package com.dynamicui.reliability.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dynamicui.reliability.config.TestConfig;

public class UploadedFilesPage {

	private static final Logger LOG = LogManager.getLogger(UploadedFilesPage.class);

	private final By heading = By.tagName("h1");
	private final By uploadedFiles = By.cssSelector("#uploaded-files p");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public UploadedFilesPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public String heading() {
		return wait.until(visibilityOfElementLocated(heading)).getText();
	}

	// The site stores each file as <timestamp>_<original name>, and this returns those stored names.
	public List<String> uploadedFileNames() {
		wait.until(visibilityOfElementLocated(uploadedFiles));
		List<String> names = driver.findElements(uploadedFiles).stream().map(WebElement::getText).toList();
		LOG.debug("Uploaded files listed: {}", names);
		return names;
	}
}

package com.dynamicui.reliability.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dynamicui.reliability.config.TestConfig;

public class UploadPage {

	private static final Logger LOG = LogManager.getLogger(UploadPage.class);

	private final By fileInput = By.cssSelector("[data-testid='file-input']");
	private final By uploadButton = By.cssSelector("[data-testid='file-submit']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public UploadPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public UploadPage open() {
		LOG.info("Opening the upload page");
		driver.get(TestConfig.baseUrl() + "/upload");
		wait.until(visibilityOfElementLocated(uploadButton));
		return this;
	}

	// Typing the path into the file input avoids the operating system's file dialog, which
	// WebDriver cannot control.
	public UploadedFilesPage upload(Path file) {
		LOG.info("Uploading {}", file.getFileName());
		driver.findElement(fileInput).sendKeys(file.toAbsolutePath().toString());
		wait.until(elementToBeClickable(uploadButton)).click();
		return new UploadedFilesPage(driver);
	}
}

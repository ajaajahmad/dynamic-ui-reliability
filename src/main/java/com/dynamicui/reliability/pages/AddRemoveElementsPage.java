package com.dynamicui.reliability.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dynamicui.reliability.config.TestConfig;

public class AddRemoveElementsPage {

	private static final Logger LOG = LogManager.getLogger(AddRemoveElementsPage.class);

	private final By addButton = By.xpath("//button[normalize-space()='Add Element']");
	private final By deleteButtons = By.cssSelector("#elements button.added-manually");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public AddRemoveElementsPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public AddRemoveElementsPage open() {
		LOG.info("Opening the add/remove elements page");
		driver.get(TestConfig.baseUrl() + "/add-remove-elements");
		wait.until(visibilityOfElementLocated(addButton));
		return this;
	}

	public AddRemoveElementsPage addElement() {
		int before = elementCount();
		LOG.info("Adding an element");
		wait.until(elementToBeClickable(addButton)).click();
		wait.until(numberOfElementsToBe(deleteButtons, before + 1));
		return this;
	}

	// Waits for the clicked button itself to leave the page, not just for the count to drop, so the
	// test proves that this element was removed rather than some other one.
	public AddRemoveElementsPage deleteLastAddedElement() {
		List<WebElement> buttons = driver.findElements(deleteButtons);
		WebElement lastAdded = buttons.get(buttons.size() - 1);
		LOG.info("Deleting the last added element ({} on the page)", buttons.size());
		lastAdded.click();
		wait.until(stalenessOf(lastAdded));
		return this;
	}

	public int elementCount() {
		int count = driver.findElements(deleteButtons).size();
		LOG.debug("Elements on the page: {}", count);
		return count;
	}
}

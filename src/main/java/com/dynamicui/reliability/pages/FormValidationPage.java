package com.dynamicui.reliability.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dynamicui.reliability.config.TestConfig;

public class FormValidationPage {

	private static final Logger LOG = LogManager.getLogger(FormValidationPage.class);

	// Chrome's date input takes digits in the browser locale's order; DriverManager pins it to en-US.
	private static final DateTimeFormatter DATE_KEYS = DateTimeFormatter.ofPattern("MMddyyyy");

	/** The form's required fields, located by name because two of them share the same id. */
	public enum Field {
		CONTACT_NAME("ContactName"), CONTACT_NUMBER("contactnumber"), PICKUP_DATE("pickupdate"),
		PAYMENT_METHOD("payment");

		private final String name;

		Field(String name) {
			this.name = name;
		}

		By locator() {
			return By.name(name);
		}
	}

	private final By form = By.cssSelector("form.needs-validation");
	private final By registerButton = By.cssSelector("form.needs-validation button[type='submit']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public FormValidationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public FormValidationPage open() {
		LOG.info("Opening the form validation page");
		driver.get(TestConfig.baseUrl() + "/form-validation");
		wait.until(visibilityOfElementLocated(registerButton));
		return this;
	}

	// The page pre-fills the contact name, so a blank-form test has to clear it first.
	public FormValidationPage clearContactName() {
		LOG.info("Clearing the pre-filled contact name");
		driver.findElement(Field.CONTACT_NAME.locator()).clear();
		return this;
	}

	public FormValidationPage fill(String contactName, String contactNumber, LocalDate pickupDate,
			String paymentMethod) {
		LOG.info("Filling the form: name '{}', number '{}', date {}, payment '{}'", contactName, contactNumber,
				pickupDate, paymentMethod);
		WebElement name = driver.findElement(Field.CONTACT_NAME.locator());
		name.clear();
		name.sendKeys(contactName);
		driver.findElement(Field.CONTACT_NUMBER.locator()).sendKeys(contactNumber);
		WebElement date = driver.findElement(Field.PICKUP_DATE.locator());
		date.sendKeys(pickupDate.format(DATE_KEYS));
		LOG.debug("Pickup date field value is '{}'", date.getDomProperty("value"));
		new Select(driver.findElement(Field.PAYMENT_METHOD.locator())).selectByVisibleText(paymentMethod);
		return this;
	}

	public FormConfirmationPage register() {
		LOG.info("Submitting the form");
		wait.until(elementToBeClickable(registerButton)).click();
		return new FormConfirmationPage(driver);
	}

	// The page blocks an invalid submit in the browser and marks the form as validated, which is
	// the state to wait for: there is no request or navigation to wait on.
	public FormValidationPage registerExpectingErrors() {
		LOG.info("Submitting the form, expecting it to be blocked");
		wait.until(elementToBeClickable(registerButton)).click();
		wait.until(attributeContains(form, "class", "was-validated"));
		return this;
	}

	// Reads the browser's own constraint validation, not the styling, so a field counts as invalid
	// only if the browser would refuse it.
	public List<Field> invalidFields() {
		List<Field> invalid = Arrays.stream(Field.values())
				.filter(field -> !driver.findElement(field.locator()).getDomProperty("validationMessage").isEmpty())
				.toList();
		LOG.debug("Invalid fields: {}", invalid);
		return invalid;
	}

	public String errorMessageFor(Field field) {
		By feedback = By.xpath("//*[@name='" + field.name + "']/following-sibling::div[@class='invalid-feedback']");
		String text = wait.until(visibilityOfElementLocated(feedback)).getText();
		LOG.debug("{} error shown: {}", field, text);
		return text;
	}

	public boolean isStillOnForm() {
		return driver.getCurrentUrl().endsWith("/form-validation")
				&& driver.findElement(registerButton).isDisplayed();
	}
}

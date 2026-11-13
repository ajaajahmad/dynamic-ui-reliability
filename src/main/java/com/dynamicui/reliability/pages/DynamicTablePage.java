package com.dynamicui.reliability.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dynamicui.reliability.config.TestConfig;

public class DynamicTablePage {

	private static final Logger LOG = LogManager.getLogger(DynamicTablePage.class);

	private static final String NAME_COLUMN = "Name";
	private static final Pattern CHROME_CPU_LABEL = Pattern.compile("Chrome CPU:\\s*(\\d+(?:\\.\\d+)?%)");

	private final By headerCells = By.cssSelector("table thead th");
	private final By bodyRows = By.cssSelector("table tbody tr");
	private final By chromeCpuSummary = By.id("chrome-cpu");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public DynamicTablePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, TestConfig.waitTimeout());
	}

	public DynamicTablePage open() {
		LOG.info("Opening the dynamic table page");
		driver.get(TestConfig.baseUrl() + "/dynamic-table");
		wait.until(visibilityOfElementLocated(chromeCpuSummary));
		return this;
	}

	// The page shuffles rows and columns and regenerates every value on each load.
	public DynamicTablePage reload() {
		LOG.info("Reloading the table");
		return open();
	}

	/**
	 * Returns one row as header name to cell text. Columns are matched by their header text and the
	 * row by its Name cell, because both positions change on every load.
	 */
	public Map<String, String> rowFor(String name) {
		List<String> headers = driver.findElements(headerCells).stream().map(WebElement::getText).toList();
		LOG.debug("Column order: {}", headers);

		for (WebElement row : driver.findElements(bodyRows)) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			Map<String, String> byHeader = new LinkedHashMap<>();
			for (int i = 0; i < headers.size(); i++) {
				byHeader.put(headers.get(i), cells.get(i).getText());
			}
			if (name.equals(byHeader.get(NAME_COLUMN))) {
				LOG.debug("{} row: {}", name, byHeader);
				return byHeader;
			}
		}
		throw new IllegalStateException("No row named '" + name + "' in the table");
	}

	// Extracts only the percentage, because ad scripts can inject link text into the label.
	public String chromeCpuSummary() {
		String text = driver.findElement(chromeCpuSummary).getText();
		LOG.debug("Summary label: {}", text);
		Matcher matcher = CHROME_CPU_LABEL.matcher(text);
		if (!matcher.find()) {
			throw new IllegalStateException("Summary label has no CPU percentage: '" + text + "'");
		}
		return matcher.group(1);
	}
}

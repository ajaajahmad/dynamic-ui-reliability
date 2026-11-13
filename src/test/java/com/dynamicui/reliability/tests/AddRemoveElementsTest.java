package com.dynamicui.reliability.tests;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.dynamicui.reliability.pages.AddRemoveElementsPage;

public class AddRemoveElementsTest extends BaseTest {

	@Test(description = "TC05 Adding then deleting an element returns the count to where it started")
	public void addThenDeleteRestoresCount() {
		AddRemoveElementsPage page = new AddRemoveElementsPage(driver()).open();
		int initial = page.elementCount();

		page.addElement();
		assertEquals(page.elementCount(), initial + 1, "Count after adding");

		page.deleteLastAddedElement();
		assertEquals(page.elementCount(), initial, "Count after deleting");
	}
}

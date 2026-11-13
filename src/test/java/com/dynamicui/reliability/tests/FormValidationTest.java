package com.dynamicui.reliability.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.dynamicui.reliability.data.TestData;
import com.dynamicui.reliability.pages.FormValidationPage;
import com.dynamicui.reliability.pages.FormValidationPage.Field;

public class FormValidationTest extends BaseTest {

	@Test(description = "TC01 An empty form is blocked with every required field invalid")
	public void emptyFormIsBlocked() {
		FormValidationPage form = new FormValidationPage(driver()).open()
				.clearContactName()
				.registerExpectingErrors();

		assertEquals(form.invalidFields(), List.of(Field.values()), "Invalid fields");
		assertTrue(form.isStillOnForm(), "The form should not be submitted");
	}

	@Test(description = "TC02 A fully valid form is accepted")
	public void validFormIsAccepted() {
		String message = new FormValidationPage(driver()).open()
				.fill(TestData.CONTACT_NAME, TestData.VALID_CONTACT_NUMBER, TestData.PICKUP_DATE,
						TestData.PAYMENT_METHOD)
				.register()
				.message();

		assertEquals(message, TestData.FORM_CONFIRMATION, "Confirmation message");
	}

	@Test(description = "TC03 A contact number in the wrong format blocks the form")
	public void wrongContactNumberFormatIsBlocked() {
		FormValidationPage form = new FormValidationPage(driver()).open()
				.fill(TestData.CONTACT_NAME, TestData.INVALID_CONTACT_NUMBER, TestData.PICKUP_DATE,
						TestData.PAYMENT_METHOD)
				.registerExpectingErrors();

		// Only the contact number is wrong, so any other invalid field would mean the test data broke.
		assertEquals(form.invalidFields(), List.of(Field.CONTACT_NUMBER), "Invalid fields");
		assertEquals(form.errorMessageFor(Field.CONTACT_NUMBER), TestData.CONTACT_NUMBER_ERROR,
				"Contact number error");
		assertTrue(form.isStillOnForm(), "The form should not be submitted");
	}
}

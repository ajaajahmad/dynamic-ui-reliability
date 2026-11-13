package com.dynamicui.reliability.data;

import java.time.LocalDate;

public final class TestData {

	// Fictional contact details for the practice form.
	public static final String CONTACT_NAME = "Asha Rao";
	public static final String VALID_CONTACT_NUMBER = "080-4567890";
	// Same digits without the dash the field's pattern requires.
	public static final String INVALID_CONTACT_NUMBER = "0804567890";
	public static final LocalDate PICKUP_DATE = LocalDate.of(2026, 12, 15);
	public static final String PAYMENT_METHOD = "card";

	public static final String FORM_CONFIRMATION = "Thank you for validating your ticket";
	public static final String CONTACT_NUMBER_ERROR = "Please provide your Contact number.";

	public static final String TABLE_ROW = "Chrome";
	public static final String TABLE_COLUMN = "CPU";
	// Each load shuffles the table, so several loads test the mapping against several layouts.
	public static final int TABLE_LOADS = 5;

	public static final String UPLOAD_CONTENT = "QA upload sample";
	public static final String UPLOAD_HEADING = "File Uploaded!";

	private TestData() {
	}
}

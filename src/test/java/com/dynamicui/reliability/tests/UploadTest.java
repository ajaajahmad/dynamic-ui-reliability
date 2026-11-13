package com.dynamicui.reliability.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.dynamicui.reliability.data.TestData;
import com.dynamicui.reliability.pages.UploadPage;
import com.dynamicui.reliability.pages.UploadedFilesPage;

public class UploadTest extends BaseTest {

	private Path uploadFile;

	@Test(description = "TC06 Uploading a file confirms that same file by name")
	public void uploadConfirmsTheSelectedFile() throws IOException {
		// A fresh temporary file each run gives a unique name, so the confirmation cannot be
		// matching a file someone else uploaded.
		uploadFile = Files.createTempFile("qa-upload-", ".txt");
		Files.writeString(uploadFile, TestData.UPLOAD_CONTENT);
		String fileName = uploadFile.getFileName().toString();

		UploadedFilesPage result = new UploadPage(driver()).open().upload(uploadFile);

		assertEquals(result.heading(), TestData.UPLOAD_HEADING, "Page heading");
		List<String> uploaded = result.uploadedFileNames();
		assertEquals(uploaded.size(), 1, "Files listed: " + uploaded);
		assertTrue(uploaded.get(0).matches("\\d+_" + Pattern.quote(fileName)),
				"Expected '<timestamp>_" + fileName + "' but found '" + uploaded.get(0) + "'");
	}

	@AfterMethod(alwaysRun = true)
	public void deleteUploadFile() throws IOException {
		if (uploadFile != null) {
			Files.deleteIfExists(uploadFile);
		}
	}
}

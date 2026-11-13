package com.dynamicui.reliability.tests;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.dynamicui.reliability.data.TestData;
import com.dynamicui.reliability.pages.DynamicTablePage;

public class DynamicTableTest extends BaseTest {

	@Test(description = "TC04 The Chrome CPU cell matches the summary label on every table layout")
	public void chromeCpuMatchesSummaryAcrossReloads() {
		DynamicTablePage table = new DynamicTablePage(driver()).open();

		for (int load = 1; load <= TestData.TABLE_LOADS; load++) {
			if (load > 1) {
				table.reload();
			}
			String cpuCell = table.rowFor(TestData.TABLE_ROW).get(TestData.TABLE_COLUMN);

			assertEquals(cpuCell, table.chromeCpuSummary(), "Chrome CPU on load " + load);
		}
	}
}

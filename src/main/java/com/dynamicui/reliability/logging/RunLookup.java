package com.dynamicui.reliability.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

/**
 * Resolves ${run:started} in log4j2.xml to the time the run began.
 *
 * Log4j2 creates per-test log files lazily, and ${date:...} is evaluated each time a file is
 * created, so it would scatter one run's logs across several folders. This value is fixed once
 * per JVM, so every file from the run lands in the same folder.
 */
@Plugin(name = "run", category = StrLookup.CATEGORY)
public class RunLookup implements StrLookup {

	private static final String STARTED = LocalDateTime.now()
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

	@Override
	public String lookup(String key) {
		return "started".equals(key) ? STARTED : null;
	}

	@Override
	public String lookup(LogEvent event, String key) {
		return lookup(key);
	}
}

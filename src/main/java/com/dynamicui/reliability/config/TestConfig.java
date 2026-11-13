package com.dynamicui.reliability.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Properties;

public final class TestConfig {

	private static final Properties PROPERTIES = load();

	private TestConfig() {
	}

	public static String baseUrl() {
		return value("base.url");
	}

	public static boolean headless() {
		return Boolean.parseBoolean(value("headless"));
	}

	public static Duration waitTimeout() {
		return Duration.ofSeconds(Long.parseLong(value("wait.seconds")));
	}

	public static String summary() {
		return "base.url=%s, headless=%s, wait=%ss".formatted(baseUrl(), headless(), waitTimeout().toSeconds());
	}

	private static String value(String key) {
		return System.getProperty(key, PROPERTIES.getProperty(key));
	}

	private static Properties load() {
		Properties properties = new Properties();
		try (InputStream stream = TestConfig.class.getResourceAsStream("/config.properties")) {
			properties.load(stream);
			return properties;
		} catch (IOException e) {
			throw new UncheckedIOException("Could not read config.properties", e);
		}
	}
}

package com.quantcast.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import com.quantcast.model.CookieDetail;

public class CookieUtilsTest {

	@Test
	public void testReadAndParseCSV() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cookie_log.csv").getFile());
		String fileName = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");

		List<CookieDetail> cookieDetails = CookieUtils.readAndParseCSV(fileName);
		assertNotNull(cookieDetails);
		assertEquals(9, cookieDetails.size());
	}

	@Test
	public void testReadAndParseCSV_WhenInvalidFileNameThenExpectEmptyList() throws Exception {

		List<CookieDetail> cookieDetails = CookieUtils.readAndParseCSV("invalid_file.csv");
		assertEquals(0, cookieDetails.size());
	}

	@Test
	public void testFetchMostActiveCookies() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cookie_log.csv").getFile());
		String fileName = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");

		List<CookieDetail> cookieDetails = CookieUtils.readAndParseCSV(fileName);
		List<String> mostActiveCookies = CookieUtils.fetchMostActiveCookies(cookieDetails,
				LocalDate.parse("2018-12-09"));
		assertNotNull(mostActiveCookies);
		assertEquals(2, mostActiveCookies.size());

		mostActiveCookies = CookieUtils.fetchMostActiveCookies(cookieDetails, LocalDate.parse("2018-12-12"));
		assertEquals(0, mostActiveCookies.size());
	}
}

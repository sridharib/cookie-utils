package com.quantcast.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import com.quantcast.model.CookieDetail.CookieDetailBuilder;

public class CookieDetailTest {

	@Test
	public void testCookieDetail() {

		String cookieName = "testCookie";
		LocalDateTime localDateTime = LocalDateTime.now();
		CookieDetail cookieDetail = new CookieDetailBuilder().cookieName(cookieName).cookieTime(localDateTime).build();

		assertEquals(cookieName, cookieDetail.getCookieName());
		assertEquals(localDateTime, cookieDetail.getCookieTime());
	}

}

package com.quantcast;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class MainTest {

	private Logger fooLogger = null;
	private ListAppender<ILoggingEvent> listAppender = null;

	@Before
	public void setUp() {
		// get Logback Logger
		fooLogger = (Logger) LoggerFactory.getLogger(Main.class);

		// create and start a ListAppender
		listAppender = new ListAppender<>();
		listAppender.start();

		// add the appender to the logger
		fooLogger.addAppender(listAppender);
	}

	@Test
	public void testMain() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cookie_log.csv").getFile());
		String fileName = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
		String[] args = { "-f", fileName, "-d", "2018-12-09" };

		Main.main(args);
		List<ILoggingEvent> logsList = listAppender.list;
		assertEquals("AtY0laUfhglK3lC7", logsList.get(0).getMessage());
		assertEquals("SAZuXPGUrfbcn5UA", logsList.get(1).getMessage());

		args = new String[] { "-f", fileName, "-d", "2018-12-07" };
		Main.main(args);
		assertEquals("4sMM2LxV07bPJzwf", logsList.get(2).getMessage());
	}

	@Test
	public void testMain_WhenInvalidArgsThenExpectErrorLog() throws Exception {

		String[] args = { "-f", "-d" };

		Main.main(args);
		List<ILoggingEvent> logsList = listAppender.list;
		assertEquals("Invalid arguments. Example: ./run-cookie-utils.sh -f cookie_log.csv -d 2018-12-09",
				logsList.get(0).getMessage());
	}

	@Test
	public void testMain_WhenInvalidFileNameThenExpectErrorLog() throws Exception {

		String[] args = { "-f", "invalid_file.csv", "-d", "2018-12-09" };

		Main.main(args);
		List<ILoggingEvent> logsList = listAppender.list;
		assertEquals("File invalid_file.csv doesn't exists", logsList.get(0).getMessage());
	}

	@Test
	public void testMain_WhenInvalidDateThenExpectErrorLog() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("cookie_log.csv").getFile());
		String fileName = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
		String[] args = { "-f", fileName, "-d", "20-12-2020" };

		Main.main(args);
		List<ILoggingEvent> logsList = listAppender.list;
		assertEquals("Invalid date format 20-12-2020. Valid format is yyyy-mm-dd", logsList.get(0).getMessage());
	}

}

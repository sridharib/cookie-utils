package com.quantcast;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantcast.model.CookieDetail;
import com.quantcast.utils.CookieUtils;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		String fileStr = parseArgs(args, "fileName");
		String dateStr = parseArgs(args, "date");

		if (fileStr == "" || dateStr == "") {
			LOGGER.warn(
					"Invalid arguments. Run the command like this ./run-cookie-utils.sh -f cookie_log.csv -d 2018-12-09");
			return;
		}

		Path filePath = Paths.get(fileStr);
		if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
			LOGGER.warn(String.format("File %s doesn't exists", fileStr));
			return;
		}

		LocalDate cookieDate = null;
		try {
			cookieDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
		} catch (DateTimeParseException e) {
			LOGGER.warn(String.format("Invalid date format %s. Valid format is yyyy-mm-dd", dateStr));
			return;
		}

		List<CookieDetail> cookieDetails = CookieUtils.readAndParseCSV(fileStr);
		List<String> mostActiveCookies = CookieUtils.fetchMostActiveCookies(cookieDetails, cookieDate);
		for (String mostActiveCookie : mostActiveCookies) {
			LOGGER.info(mostActiveCookie);
		}
	}

	private static String parseArgs(String[] args, String key) {
		boolean returnArg = false;
		for (String arg : args) {
			if (key.equalsIgnoreCase("fileName") && "-f".equalsIgnoreCase(arg)
					|| key.equalsIgnoreCase("date") && "-d".equalsIgnoreCase(arg)) {
				returnArg = true;
				continue;
			}

			if (returnArg) {
				return arg;
			}
		}
		return "";
	}

}

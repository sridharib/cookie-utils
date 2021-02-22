package com.quantcast.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantcast.model.CookieDetail;

/**
 * Cookie Utils.
 * 
 */
public class CookieUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtils.class);

	private static final String CSV_HEADER = "cookie,timestamp";
	private static final String CSV_DELIMITER = ",";

	/**
	 * Read and parse CSV file
	 * 
	 * @param fileName
	 * @return The list of parsed cookie detail objects
	 */
	public static List<CookieDetail> readAndParseCSV(String fileName) {

		List<CookieDetail> cookieDetails = new ArrayList<>();
		Path filePath = Paths.get(fileName);
		if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
			try (Stream<String> stream = Files.lines(filePath)) {

				// 1. filter the header
				// 2. convert it into a list of CookieDetail object
				cookieDetails = stream.filter(line -> !line.startsWith(CSV_HEADER)).map(line -> {
					if (line != null) {
						String[] cookieDetailArr = line.split(CSV_DELIMITER);
						if (cookieDetailArr.length == 2) {
							String cookieName = cookieDetailArr[0].strip();
							LocalDateTime cookieTime = LocalDateTime.parse(cookieDetailArr[1].strip(),
									DateTimeFormatter.ISO_OFFSET_DATE_TIME);
							return new CookieDetail.CookieDetailBuilder().cookieName(cookieName).cookieTime(cookieTime)
									.build();
						}
					}
					return null;
				}).filter(Objects::nonNull).collect(Collectors.toList());

			} catch (Exception e) {
				String errorMsg = String.format("Unable to parse the file %s. Error message: %s", fileName,
						e.getMessage());
				LOGGER.warn(errorMsg);
			}
		} else {
			LOGGER.warn(String.format("File %s not found", fileName));
		}

		return cookieDetails;
	}

	/**
	 * Fetches the most active cookie for a given date
	 * 
	 * @param cookieDetails
	 * @param cookieDate
	 * @return The list of most active cookies
	 */
	public static List<String> fetchMostActiveCookies(List<CookieDetail> cookieDetails, LocalDate cookieDate) {

		List<String> activeCookieDetails = new ArrayList<>();

		Long maxNoOfActiveCookie = Long.MIN_VALUE;
		// Filter the cookie name and number of occurrences
		Map<String, Long> cookieMap = cookieDetails.stream()
				.filter(cookieDetail -> cookieDate.isEqual(cookieDetail.getCookieTime().toLocalDate()))
				.collect(Collectors.groupingBy(CookieDetail::getCookieName, Collectors.counting()));

		// Sort in reverse order so that most active cookies come at the top
		LinkedHashMap<String, Long> collect = cookieMap.entrySet().stream()
				.sorted(Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

		for (Entry<String, Long> entry : collect.entrySet()) {
			// Add the most active cookie to the list
			if (entry.getValue() >= maxNoOfActiveCookie) {
				activeCookieDetails.add(entry.getKey());
				maxNoOfActiveCookie = entry.getValue();
			} else {
				break;
			}
		}

		return activeCookieDetails;
	}

}

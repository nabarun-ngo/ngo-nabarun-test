package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javafaker.Faker;

public class DataUtils {
	private static final Faker faker = new Faker();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static String firstDayOfCurrentMonth() {
		LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
		return firstDay.format(FORMATTER);
	}

	public static String lastDayOfCurrentMonth() {
		LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		return lastDay.format(FORMATTER);
	}

	public static String replacePlaceholders(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		if (!input.contains("{") || !input.contains("}")) {
			return input;
		}

		if (containsPlaceholder(input, "{RandomName}")) {
			input = input.replace("{RandomName}", faker.name().fullName());
		}
		if (containsPlaceholder(input, "{RandomEmail}")) {
			input = input.replace("{RandomEmail}", faker.internet().emailAddress());
		}
		if (containsPlaceholder(input, "{SystemDate}")) {
			input = input.replace("{SystemDate}", dateFormat.format(new Date()));
		}
		if (containsPlaceholder(input, "{FirstOfCurrentMonth}")) {
			input = input.replace("{FirstOfCurrentMonth}", firstDayOfCurrentMonth());
		}
		if (containsPlaceholder(input, "{LastOfCurrentMonth}")) {
			input = input.replace("{LastOfCurrentMonth}", lastDayOfCurrentMonth());
		}

		if (containsPlaceholder(input, "{RandomFirstName}")) {
			input = input.replace("{RandomFirstName}", faker.name().firstName());
		}
		if (containsPlaceholder(input, "{RandomLastName}")) {
			input = input.replace("{RandomLastName}", faker.name().lastName());
		}
		if (containsPlaceholder(input, "{RandomText}")) {
			input = input.replace("{RandomText}", faker.lorem().sentence());
		}
		if (containsPlaceholder(input, "{RandomLocation}")) {
			input = input.replace("{RandomLocation}", faker.address().cityName());
		}

		Pattern datePattern = Pattern.compile("\\{SystemDate([+-]\\d+)\\}");
		Matcher dateMatcher = datePattern.matcher(input);
		while (dateMatcher.find()) {
			int offset = Integer.parseInt(dateMatcher.group(1));
			input = input.replace(dateMatcher.group(), getDateWithOffset(offset));
		}

		Pattern phonePattern = Pattern.compile("\\{RandomNumber:(\\d+)\\}");
		Matcher phoneMatcher = phonePattern.matcher(input);
		while (phoneMatcher.find()) {
			int digits = Integer.parseInt(phoneMatcher.group(1));
			input = input.replace(phoneMatcher.group(), generateRandomNumber(digits));
		}

		return input;
	}

	private static String generateRandomNumber(int digits) {
		StringBuilder phoneNumber = new StringBuilder();
		for (int i = 0; i < digits; i++) {
			phoneNumber.append(faker.number().randomDigit());
		}
		return phoneNumber.toString();
	}

	private static String getDateWithOffset(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return dateFormat.format(calendar.getTime());
	}

	private static boolean containsPlaceholder(String input, String placeholder) {
		return input.contains(placeholder);
	}
}

package ngo.nabarun.test.ngo_nabarun_test.utils;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

/**
 * Utility class for data generation and placeholder replacement.
 */
public class DataUtils {
	private static final Faker faker = new Faker();
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static String firstDayOfCurrentMonth() {
		LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
		return firstDay.format(FORMATTER);
	}

	public static String lastDayOfCurrentMonth() {
		LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		return lastDay.format(FORMATTER);
	}

	/**
	 * Replaces all recognized placeholders within the given input string with
	 * dynamically generated data.
	 * 
	 * <p>
	 * <b>Supported Placeholders:</b>
	 * </p>
	 * 
	 * <table border="1">
	 * <tr>
	 * <th>Category</th>
	 * <th>Placeholders</th>
	 * </tr>
	 * <tr>
	 * <td><b>Personal</b></td>
	 * <td>{@code {RandomName}}, {@code {RandomFirstName}},
	 * {@code {RandomLastName}},
	 * {@code {RandomTitle}}, {@code {RandomJobTitle}},
	 * {@code {RandomUsername}}</td>
	 * </tr>
	 * <tr>
	 * <td><b>Contact/Location</b></td>
	 * <td>{@code {RandomEmail}}, {@code {RandomPhoneNumber}},
	 * {@code {RandomStreetAddress}}, {@code {RandomCity}}, {@code {RandomState}},
	 * {@code {RandomZipCode}}, {@code {RandomCountry}},
	 * {@code {RandomLocation}}</td>
	 * </tr>
	 * <tr>
	 * <td><b>Dates/Time</b></td>
	 * <td>{@code {SystemDate}}, {@code {SystemTime}},
	 * {@code {FirstOfCurrentMonth}}, {@code {LastOfCurrentMonth}},
	 * {@code {SystemDate[format]}}, {@code {SystemDate+-N}},
	 * {@code {SystemDate[format]+-N}}, {@code {BeginningOfMonth[format]}},
	 * {@code {EndOfMonth[format]}}, {@code {BeginningOfYear[format]}},
	 * {@code {EndOfYear[format]}}</td>
	 * </tr>
	 * <tr>
	 * <td><b>Parameterized</b></td>
	 * <td>{@code {RandomNumber:N}}, {@code {RandomAlphanumeric:N}},
	 * {@code {RandomAlphabetic:N}}, {@code {RandomWord:N}},
	 * {@code {RandomInt:min,max}}, {@code {RandomPrice:min,max}}</td>
	 * </tr>
	 * <tr>
	 * <td><b>System/Internet</b></td>
	 * <td>{@code {RandomURL}}, {@code {RandomIPAddress}}, {@code {RandomUUID}},
	 * {@code {RandomBoolean}}, {@code ${env:VAR}}, {@code ${sys:prop}}</td>
	 * </tr>
	 * <tr>
	 * <td><b>Misc</b></td>
	 * <td>{@code {RandomColor}}, {@code {RandomAnimal}},
	 * {@code {RandomCreditCardNumber}}, {@code {RandomCompany}},
	 * {@code {RandomWord}}</td>
	 * </tr>
	 * </table>
	 * 
	 * <p>
	 * <b>Note:</b> All placeholders support optional leading and trailing
	 * whitespace inside the braces (e.g., {@code { RandomName }}).
	 * </p>
	 * 
	 * @param input The raw string containing placeholders (e.g., "Hello
	 *              {RandomFirstName}").
	 * @return A new string with all identified placeholders replaced by generated
	 *         values.
	 */
	public static String replacePlaceholders(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}

		// Simple String Replacements with optional whitespace handling
		input = replaceSimplePlaceholder(input, "RandomName", faker.name().fullName());
		input = replaceSimplePlaceholder(input, "RandomFirstName", faker.name().firstName());
		input = replaceSimplePlaceholder(input, "RandomLastName", faker.name().lastName());
		input = replaceSimplePlaceholder(input, "RandomTitle", faker.name().title());
		input = replaceSimplePlaceholder(input, "RandomJobTitle", faker.job().title());

		input = replaceSimplePlaceholder(input, "RandomEmail", faker.internet().emailAddress());
		input = replaceSimplePlaceholder(input, "RandomUsername", faker.internet().slug());
		input = replaceSimplePlaceholder(input, "RandomURL", faker.internet().url());
		input = replaceSimplePlaceholder(input, "RandomIPAddress", faker.internet().ipV4Address());

		input = replaceSimplePlaceholder(input, "RandomPhoneNumber", faker.phoneNumber().phoneNumber());
		input = replaceSimplePlaceholder(input, "RandomStreetAddress", faker.address().streetAddress());
		input = replaceSimplePlaceholder(input, "RandomCity", faker.address().city());
		input = replaceSimplePlaceholder(input, "RandomState", faker.address().state());
		input = replaceSimplePlaceholder(input, "RandomZipCode", faker.address().zipCode());
		input = replaceSimplePlaceholder(input, "RandomCountry", faker.address().country());
		input = replaceSimplePlaceholder(input, "RandomLocation", faker.address().cityName());

		input = replaceSimplePlaceholder(input, "RandomCompany", faker.company().name());
		input = replaceSimplePlaceholder(input, "RandomUUID", UUID.randomUUID().toString());
		input = replaceSimplePlaceholder(input, "RandomBoolean", String.valueOf(faker.bool().bool()));
		input = replaceSimplePlaceholder(input, "RandomColor", faker.color().name());
		input = replaceSimplePlaceholder(input, "RandomAnimal", faker.animal().name());
		input = replaceSimplePlaceholder(input, "RandomCreditCardNumber", faker.business().creditCardNumber());

		input = replaceSimplePlaceholder(input, "RandomWord", faker.lorem().word());
		input = replaceSimplePlaceholder(input, "SystemDate", DEFAULT_DATE_FORMAT.format(new Date()));
		input = replaceSimplePlaceholder(input, "SystemTime", DEFAULT_TIME_FORMAT.format(new Date()));
		input = replaceSimplePlaceholder(input, "FirstOfCurrentMonth", firstDayOfCurrentMonth());
		input = replaceSimplePlaceholder(input, "LastOfCurrentMonth", lastDayOfCurrentMonth());

		// Regex based Replacements with whitespace handling

		// { RandomNumber : N }
		Pattern numberPattern = Pattern.compile("\\{\\s*RandomNumber\\s*:\\s*(\\d+)\\s*\\}");
		Matcher numberMatcher = numberPattern.matcher(input);
		while (numberMatcher.find()) {
			int digits = Integer.parseInt(numberMatcher.group(1));
			input = input.replace(numberMatcher.group(), generateRandomNumber(digits));
		}

		// { RandomAlphanumeric : N }
		Pattern alphanumericPattern = Pattern.compile("\\{\\s*RandomAlphanumeric\\s*:\\s*(\\d+)\\s*\\}");
		Matcher alphanumericMatcher = alphanumericPattern.matcher(input);
		while (alphanumericMatcher.find()) {
			int length = Integer.parseInt(alphanumericMatcher.group(1));
			input = input.replace(alphanumericMatcher.group(), faker.lorem().characters(length, true));
		}

		// { RandomAlphabetic : N }
		Pattern alphabeticPattern = Pattern.compile("\\{\\s*RandomAlphabetic\\s*:\\s*(\\d+)\\s*\\}");
		Matcher alphabeticMatcher = alphabeticPattern.matcher(input);
		while (alphabeticMatcher.find()) {
			int length = Integer.parseInt(alphabeticMatcher.group(1));
			input = input.replace(alphabeticMatcher.group(), faker.lorem().characters(length, false));
		}

		// { RandomWord : N }
		Pattern textPattern = Pattern.compile("\\{\\s*RandomWord\\s*:\\s*(\\d+)\\s*\\}");
		Matcher textMatcher = textPattern.matcher(input);
		while (textMatcher.find()) {
			int wordCount = Integer.parseInt(textMatcher.group(1));
			input = input.replace(textMatcher.group(), faker.lorem().sentence(wordCount));
		}

		// { RandomInt : min , max }
		Pattern intRangePattern = Pattern.compile("\\{\\s*RandomInt\\s*:\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\}");
		Matcher intRangeMatcher = intRangePattern.matcher(input);
		while (intRangeMatcher.find()) {
			int min = Integer.parseInt(intRangeMatcher.group(1));
			int max = Integer.parseInt(intRangeMatcher.group(2));
			input = input.replace(intRangeMatcher.group(), String.valueOf(faker.number().numberBetween(min, max)));
		}

		// { RandomPrice : min , max }
		Pattern pricePattern = Pattern.compile("\\{\\s*RandomPrice\\s*:\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\}");
		Matcher priceMatcher = pricePattern.matcher(input);
		while (priceMatcher.find()) {
			double min = Double.parseDouble(priceMatcher.group(1));
			double max = Double.parseDouble(priceMatcher.group(2));
			input = input.replace(priceMatcher.group(), faker.commerce().price(min, max));
		}

		// Advanced Date Placeholders: { BeginningOfMonth [format] }, { EndOfMonth
		// [format] }, etc.
		input = replaceDateCalculation(input, "BeginningOfMonth", TemporalAdjusters.firstDayOfMonth());
		input = replaceDateCalculation(input, "EndOfMonth", TemporalAdjusters.lastDayOfMonth());
		input = replaceDateCalculation(input, "BeginningOfYear", TemporalAdjusters.firstDayOfYear());
		input = replaceDateCalculation(input, "EndOfYear", TemporalAdjusters.lastDayOfYear());

		// { SystemDate [format] +- N } or { SystemDate +- N } or { SystemDate [format]
		// }
		Pattern dateOffsetPattern = Pattern
				.compile("\\{\\s*SystemDate\\s*(\\[\\s*(.*?)\\s*\\])?\\s*([+-]\\d+)?\\s*\\}");
		Matcher dateOffsetMatcher = dateOffsetPattern.matcher(input);
		while (dateOffsetMatcher.find()) {
			String format = dateOffsetMatcher.group(2);
			String offsetStr = dateOffsetMatcher.group(3);

			SimpleDateFormat sdf = (format == null || format.isEmpty()) ? DEFAULT_DATE_FORMAT
					: new SimpleDateFormat(format);
			int offset = (offsetStr == null) ? 0 : Integer.parseInt(offsetStr);

			input = input.replace(dateOffsetMatcher.group(), sdf.format(getDateWithOffset(offset)));
		}

		// ${ env : VAR_NAME } for Environment Variables
		Pattern envPattern = Pattern.compile("\\$\\{\\s*env\\s*:\\s*(.*?)\\s*\\}");
		Matcher envMatcher = envPattern.matcher(input);
		while (envMatcher.find()) {
			String varName = envMatcher.group(1);
			String value = System.getenv(varName);
			if (value != null) {
				input = input.replace(envMatcher.group(), value);
			}
		}

		// ${ sys : PROP_NAME } for System Properties
		Pattern sysPattern = Pattern.compile("\\$\\{\\s*sys\\s*:\\s*(.*?)\\s*\\}");
		Matcher sysMatcher = sysPattern.matcher(input);
		while (sysMatcher.find()) {
			String propName = sysMatcher.group(1);
			String value = System.getProperty(propName);
			if (value != null) {
				input = input.replace(sysMatcher.group(), value);
			}
		}

		return input;
	}

	private static String replaceSimplePlaceholder(String input, String placeholderName, String replacement) {
		String regex = "\\{\\s*" + Pattern.quote(placeholderName) + "\\s*\\}";
		return input.replaceAll(regex, replacement);
	}

	private static String replaceDateCalculation(String input, String prefix, TemporalAdjuster adjuster) {
		Pattern pattern = Pattern.compile("\\{\\s*" + Pattern.quote(prefix) + "\\s*(\\[\\s*(.*?)\\s*\\])?\\s*\\}");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String format = matcher.group(2);
			DateTimeFormatter dtf = (format == null || format.isEmpty()) ? FORMATTER
					: DateTimeFormatter.ofPattern(format);
			LocalDate date = LocalDate.now().with(adjuster);
			input = input.replace(matcher.group(), date.format(dtf));
		}
		return input;
	}

	public static String generateRandomNumber(int digits) {
		StringBuilder number = new StringBuilder();
		for (int i = 0; i < digits; i++) {
			number.append(faker.number().randomDigit());
		}
		return number.toString();
	}

	/**
	 * Returns a java.util.Date object for today plus/minus the given number of
	 * days.
	 * 
	 * @param daysOffset Number of days to add (positive) or subtract (negative)
	 *                   from today
	 * @return java.util.Date object
	 */
	public static Date getDateWithOffset(int daysOffset) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, daysOffset);
		return calendar.getTime();
	}

	/**
	 * Resolves custom variables stored in ScenarioContext.
	 * 
	 * @param input   The string containing variables (e.g., "{myVariable}")
	 * @param context The ScenarioContext to look up variables
	 * @return The resolved string
	 */
	public static String resolveVariables(String input, ScenarioContext context) {
		if (input == null || !input.contains("{") || context == null) {
			return input;
		}
		Pattern pattern = Pattern.compile("\\{\\s*([^}]*?)\\s*\\}");
		Matcher matcher = pattern.matcher(input);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String key = matcher.group(1).trim();
			if (context.containsCustomKey(key)) {
				String val = context.getCustomValue(key, String.class);
				matcher.appendReplacement(sb, Matcher.quoteReplacement(val));
			} else {
				matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group()));
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Fully resolves data by first replacing system placeholders and then resolving
	 * context variables.
	 * 
	 * @param input   The raw input string
	 * @param context The ScenarioContext for variable lookup
	 * @return The fully resolved string
	 */
	public static String resolveData(String input, ScenarioContext context) {
		String step1 = replacePlaceholders(input);
		return resolveVariables(step1, context);
	}

	public static String extractValueByPath(String json, String path) {
		try {
			ObjectMapper mapper = CommonUtils.getObjectMapper();
			JsonNode node = mapper.readTree(json);
			String[] parts = path.split("\\.");
			for (String part : parts) {
				if (part.contains("[") && part.contains("]")) {
					String fieldName = part.substring(0, part.indexOf("["));
					int index = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
					node = fieldName.isEmpty() ? node.get(index) : node.get(fieldName).get(index);
				} else {
					node = node.get(part);
				}
				if (node == null || node.isMissingNode())
					throw new RuntimeException("Path not found: " + path + " at part: " + part);
			}
			return node.isValueNode() ? node.asText() : node.toString();
		} catch (Exception e) {
			throw new RuntimeException("Failed to extract value from response: " + e.getMessage(), e);
		}
	}

}

package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.io.File;
import java.net.URL;
import java.text.Normalizer;

import ngo.nabarun.common.util.CommonUtil;

public class CommonUtils extends CommonUtil {

	private static final String INVALID_CHARACTERS = "[<>:\"/\\|?*]";

	/**
	 * Sanitizes the given string to make it a valid file name by removing or
	 * replacing invalid characters.
	 *
	 * @param fileName The input string to sanitize.
	 * @return A sanitized string suitable for use as a file name.
	 */
	public static String sanitizeFileName(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return fileName;
		}
		fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
		fileName = fileName.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		fileName = fileName.replaceAll(INVALID_CHARACTERS, "");
		fileName = fileName.trim();
		if (fileName.length() > 255) {
			fileName = fileName.substring(0, 255);
		}
		return fileName;
	}

	public static String getFileFromResources(String fileName) {
		URL resource = CommonUtils.class.getClassLoader().getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("File not found: " + fileName);
		}
		return new File(resource.getFile()).getAbsolutePath();
	}

}

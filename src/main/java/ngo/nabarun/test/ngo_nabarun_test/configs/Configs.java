package ngo.nabarun.test.ngo_nabarun_test.configs;

import java.util.Arrays;
import java.util.List;

public class Configs {
	private static final List<String> TRUE_MATRIX = Arrays.asList("Y", "y", "true", "TRUE", "yes", "YES", "1");

	public static final String ROOT_URL = ConfigManager.get("ROOT_URL");
	public static final String APP_URL = ConfigManager.get("APP_URL");
	public static final String BROWSER = ConfigManager.get("TEST_BROWSER", "chrome");
	public static final Integer IMPLICIT_WAIT = Integer.parseInt(ConfigManager.get("TEST_IMPLICIT_WAIT", "10")) * 1000;
	public static final Integer GLOBAL_EXPLICIT_WAIT = Integer
			.parseInt(ConfigManager.get("TEST_GLOBAL_EXPLICIT_WAIT", "30")) * 1000;
	public static final String TEST_DEFAULTPASSWORD = ConfigManager.get("TEST_DEFAULTPASSWORD");
	public static final String DB_URL = ConfigManager.get("POSTGRES_URI");
	public static final boolean IS_HEADLESS = TRUE_MATRIX.contains(ConfigManager.get("HEADLESS", "false"));
	public static final boolean IS_DEBUG_HIGHLIGHT = TRUE_MATRIX.contains(ConfigManager.get("DEBUG_HIGHLIGHT", "true"));
	public static final boolean IS_AUTO_HEAL = TRUE_MATRIX.contains(ConfigManager.get("AUTO_HEAL", "false"));
	public static final boolean IS_SHOW_STEP_OVERLAY = TRUE_MATRIX.contains(ConfigManager.get("SHOW_STEP_OVERLAY", "false"));

	public static final String AUTH0_CLIENT_ID = ConfigManager.get("AUTH0_CLIENT_ID", "");
	public static final String AUTH0_CLIENT_SECRET = ConfigManager.get("AUTH0_CLIENT_SECRET", "");
	public static final String AUTH0_DOMAIN = ConfigManager.get("AUTH0_DOMAIN", "");
	public static final String AUTH0_AUDIENCE = ConfigManager.get("AUTH0_AUDIENCE", "");
}

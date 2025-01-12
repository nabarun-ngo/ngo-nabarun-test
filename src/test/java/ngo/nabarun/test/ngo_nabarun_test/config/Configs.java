package ngo.nabarun.test.ngo_nabarun_test.config;

public class Configs {

	public static final String ROOT_URL = ConfigManager.get("ROOT_URL");
	public static final String BROWSER = ConfigManager.get("TEST_BROWSER");
	public static final Integer IMPLICIT_WAIT = Integer.parseInt(ConfigManager.get("TEST_IMPLICIT_WAIT"));
	public static final Integer GLOBAL_EXPLICIT_WAIT = Integer.parseInt(ConfigManager.get("TEST_GLOBAL_EXPLICIT_WAIT"));
	public static final String TEST_DEFAULTPASSWORD = ConfigManager.get("TEST_DEFAULTPASSWORD");
	public static final String TEST_APIKEY = ConfigManager.get("TEST_APIKEY");
	public static final String TEST_QMETRY_APIKEY = ConfigManager.get("TEST_QMETRY_APIKEY");

}

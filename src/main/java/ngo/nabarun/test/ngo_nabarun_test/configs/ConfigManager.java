package ngo.nabarun.test.ngo_nabarun_test.configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import ngo.nabarun.doppler.api.ConfigsApi;
import ngo.nabarun.doppler.model.Secret;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;

public class ConfigManager {
	private static final Logger logger = LogManager.getLogger(ConfigManager.class);
	private static final String DOPPLER_PROJECT_NAME = "DOPPLER_PROJECT_NAME";
	private static final String DOPPLER_SERVICE_TOKEN = "DOPPLER_SERVICE_TOKEN";
	private static final String ENVIRONMENT = "ENVIRONMENT";
	private static final String CONFIG_SOURCE = "CONFIG_SOURCE";

	private static final Map<String, Object> configMap = new java.util.HashMap<>();
	static {
		// Load .env file if exists for system properties
		// e.g. ENVIRONMENT, CONFIG_SOURCE, DOPPLER_PROJECT_NAME, DOPPLER_SERVICE_TOKEN
		java.io.File envFile = new java.io.File(".env");
		if (envFile.exists()) {
			try (InputStream envStream = new java.io.FileInputStream(envFile)) {
				java.util.Properties props = new java.util.Properties();
				props.load(envStream);
				for (String name : props.stringPropertyNames()) {
					if(System.getProperty(name) != null){
						logger.info("Property '{}' already exists.",name);
					}else{
						System.setProperty(name, props.getProperty(name));
						logger.info("Property '{}' loaded from .env file.",name);
					}
				}
				logger.info("Loaded variables from .env file");
			} catch (IOException e) {
                logger.error("Found .env but failed to read it: {}", e.getMessage());
			}
		}
		String DEFAULT_ENVIRONMENT= "dev";
		String config_env = CommonUtils.getEnvProperty(ENVIRONMENT, DEFAULT_ENVIRONMENT);
		String DEFAULT_CONFIG_SOURCE=config_env.equalsIgnoreCase("dev") ? "file" : "doppler";
		String config_source = CommonUtils.getEnvProperty(CONFIG_SOURCE,DEFAULT_CONFIG_SOURCE);

		if (config_source.equalsIgnoreCase("doppler")) {
			String projectName = CommonUtils.getEnvProperty(DOPPLER_PROJECT_NAME);
			String token = CommonUtils.getEnvProperty(DOPPLER_SERVICE_TOKEN);

			if (projectName == null)
				throw new RuntimeException("DOPPLER_PROJECT_NAME must be set (via .env or system property)");
			if (token == null)
				throw new RuntimeException("DOPPLER_SERVICE_TOKEN must be set (via .env or system property)");

			try {
				ConfigsApi configApi = new ConfigsApi(projectName, token);
				List<Secret> secrets = configApi.getSecrets(config_env);
				configMap.putAll(secrets.stream().collect(
						Collectors.toMap(Secret::getKey, Secret::getValue)));
			} catch (Exception e) {
				logger.error("Error loading properties from Doppler", e);
				throw new RuntimeException("Failed to load configuration", e);
			}
		} else {
			// 2. Load environment specific config (overrides common)
			String configFilePath = "test_config/test-config-" + config_env + ".json";
			loadConfigFile("test_config/test-config.json", false);
			loadConfigFile(configFilePath, true);
		}
	}

	private static void loadConfigFile(String path, boolean required) {
		try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(path)) {
			if (inputStream == null) {
				if (required) {
					throw new RuntimeException("Configuration file not found: " + path
							+ ". [TIP: Check ENVIRONMENT system property or ensure file exists in test_config/]");
				}
				return;
			}
			Map<String, Object> newConfig = CommonUtils.getObjectMapper().readValue(inputStream,
					new TypeReference<Map<String, Object>>() {
					});
			configMap.putAll(newConfig);
		} catch (IOException e) {
            logger.error("Failed to load configuration from file: {}", path, e);
			if (required)
				throw new RuntimeException("Failed to load configuration", e);
		}
	}

	static String get(String key, String defaultValue) {
		Object value = configMap.get(key);
		if (value == null) {
			value = CommonUtils.getEnvProperty(key);
		}
		if (value == null) {
			return defaultValue;
		}
		return value.toString();
	}

	/**
	 * First checks environment variables, then falls back to configMap loaded from Doppler or files.
	 * This allows environment variables to override any config source, which is useful for CI/CD pipelines or local
	 */
	static String get(String key) {
		Object value = getRaw(key) ;
		return value == null ? null : value.toString();
	}

	static <T> T get(String key, Class<T> type, T defaultValue) {
		Object value = getRaw(key);
		return value == null ? defaultValue : type.cast(value);
	}

	static <T> T get(String key, Class<T> type) {
		Object value = getRaw(key);
		if (value == null) {
			throw new IllegalArgumentException("Key not found in configuration: " + key);
		}
		return type.cast(value);
	}

	private static Object getRaw(String key) {
		Object value = CommonUtils.getEnvProperty(key);
		return value == null ? configMap.get(key) : value;
	}
}
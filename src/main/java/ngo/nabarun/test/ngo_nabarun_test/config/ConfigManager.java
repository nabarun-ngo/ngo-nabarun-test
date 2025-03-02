package ngo.nabarun.test.ngo_nabarun_test.config;

import com.fasterxml.jackson.core.type.TypeReference;

import ngo.nabarun.test.ngo_nabarun_test.helpers.CommonHelpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class ConfigManager {
	private static Map<String, Object> config;
	private static final String DOPPLER_PROJECT_NAME = "DOPPLER_PROJECT_NAME";
	private static final String DOPPLER_SERVICE_TOKEN = "DOPPLER_SERVICE_TOKEN";
	private static final String ENVIRONMENT = "ENVIRONMENT";
	private static final String CONFIG_SOURCE = "CONFIG_SOURCE";

	static {
		String config_source = System.getProperty(CONFIG_SOURCE);
		String config_env = System.getProperty(ENVIRONMENT,"stage");
		if(config_env == null) {
			throw new RuntimeException("ENVIRONMENT must be set as argument");
		}

		if (config_source != null && config_source.equalsIgnoreCase("doppler")) {
			String projectName=System.getenv(DOPPLER_PROJECT_NAME) == null ? System.getProperty(DOPPLER_PROJECT_NAME) : System.getenv(DOPPLER_PROJECT_NAME);
			String token=System.getenv(DOPPLER_SERVICE_TOKEN) == null ? System.getProperty(DOPPLER_SERVICE_TOKEN) : System.getenv(DOPPLER_SERVICE_TOKEN);
			if(projectName == null) {
				throw new RuntimeException("DOPPLER_PROJECT_NAME must be set as argument");
			}
			if(token == null) {
				throw new RuntimeException("DOPPLER_SERVICE_TOKEN must be set as argument");
			}
			try {
				DopplerPropertySource source= new  DopplerPropertySource(projectName, config_env, token);
				config=source.loadProperties();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String configFilePath = "test-config-"+config_env+".json";
			try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(configFilePath)) {
				if (inputStream == null) {
					throw new RuntimeException("Configuration file not found: " + configFilePath);
				}
				config = CommonHelpers.objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {
				});
			} catch (IOException e) {
				throw new RuntimeException("Failed to load configuration", e);
			}
		}

	}

	static String get(String key) {
		Object value = System.getProperty(key) != null ? System.getProperty(key) : config.get(key);
		if (value == null) {
			throw new IllegalArgumentException("Key not found in configuration: " + key);
		}
		return value.toString();
	}

	static <T> T get(String key, Class<T> type) {
		Object value = System.getProperty(key) != null ? System.getProperty(key) : config.get(key);
		if (value == null) {
			throw new IllegalArgumentException("Key not found in configuration: " + key);
		}
		return type.cast(value);
	}
}
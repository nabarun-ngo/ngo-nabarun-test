package ngo.nabarun.test.ngo_nabarun_test.utilities;

import com.microsoft.playwright.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DevToolsUtility {
	private final ScenarioContext scenarioContext;
	private static final Logger logger = LogManager.getLogger(DevToolsUtility.class);
	public static Map<String, Request> requestMap = new HashMap<>();

	public DevToolsUtility(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
		logger.info("DevToolsUtility initialized with ScenarioContext.");
	}

	/**
	 * Enables network logging to capture requests and responses.
	 */
	public void enableNetworkLogging() {
		scenarioContext.getPage().onRequest(request -> {
			logger.info("Request: " + request.url());
			requestMap.put(request.url(), request);
		});
		scenarioContext.getPage().onResponse(response -> {
			logger.info("Response: " + response.url() + " Status: " + response.status());
		});
	}

	public void enableConsoleLogging() {
		scenarioContext.getPage().onConsoleMessage(msg -> {
			logger.info("Console: " + msg.text());
		});
	}

	/**
	 * Generic method to wait for an XHR/Fetch response and extract a value using a
	 * JSON key.
	 * 
	 * @param urlPart The partial URL to match.
	 * @param jsonKey The key to extract (supports simple nested keys like
	 *                "data.id").
	 * @param trigger An optional action (like a click) that triggers the request.
	 * @return The extracted value as a String, or null if not found.
	 */
	public static String waitForXHRResponseAndExtract(Page page, String urlPart, String jsonKey, Runnable trigger) {
		logger.info("Waiting for XHR response containing: " + urlPart);
		Response response = page.waitForResponse(
				res -> res.url().contains(urlPart) &&
						(res.request().resourceType().equals("xhr") || res.request().resourceType().equals("fetch")),
				trigger != null ? trigger : () -> {
				});

		try {
			JsonNode node = new ObjectMapper().readTree(response.body());
			String[] keys = jsonKey.split("\\.");
			for (String key : keys) {
				if (node == null || node.isMissingNode())
					return null;
				node = node.get(key);
			}
			return node != null ? node.asText() : null;
		} catch (Exception e) {
			logger.error("Error extracting '{}' from XHR response: {}", jsonKey, urlPart, e);
			return null;
		}
	}
}

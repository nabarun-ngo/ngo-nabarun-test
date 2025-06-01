package ngo.nabarun.test.ngo_nabarun_test.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;
import org.openqa.selenium.devtools.v137.network.Network.GetResponseBodyResponse;
import org.openqa.selenium.devtools.v137.network.model.PostDataEntry;
import org.openqa.selenium.devtools.v137.network.model.Request;
import org.openqa.selenium.devtools.v137.network.model.ResourceType;
import org.openqa.selenium.devtools.v137.network.model.Response;
import org.openqa.selenium.edge.EdgeDriver;

import ngo.nabarun.test.ngo_nabarun_test.config.Configs;

import org.openqa.selenium.devtools.v137.log.Log;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DevToolsUtility {
	private final DevTools devTools;
	private static final Logger logger = LogManager.getLogger(DevToolsUtility.class);
	public static Map<String, Request> requestMap = new HashMap<>();

	public DevToolsUtility(WebDriver driver) {
        logger.info("Starting browser session...");
		String browser = Configs.BROWSER;
        this.devTools = switch (browser.toUpperCase()) {
        case "CHROME" -> ((ChromeDriver) driver).getDevTools();
        case "EDGE" -> ((EdgeDriver) driver).getDevTools();
        default -> throw new IllegalArgumentException("Unexpected value: " + browser.toUpperCase());
        };
		this.devTools.createSession();
	}

	/**
	 * Enables network logging to capture requests and responses.
	 */
	public void enableNetworkLogging() {
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

		// Capture network requests
		devTools.addListener(Network.requestWillBeSent(), request -> {
			Optional<ResourceType> resourceType = request.getType();

			if (resourceType.isEmpty() || resourceType.get() == ResourceType.FETCH
					|| resourceType.get() == ResourceType.XHR) {
				requestMap.put(request.getRequestId().toString(), request.getRequest());
			}

		});

		// Capture network responses
		devTools.addListener(Network.responseReceived(), response -> {
			ResourceType resourceType = response.getType();
			Response res = response.getResponse();

			if (res.getStatus() >= 400 && (resourceType == ResourceType.FETCH || resourceType == ResourceType.XHR)) {

				Request req = requestMap.get(response.getRequestId().toString());

				List<PostDataEntry> postDataEntries = req.getPostDataEntries().orElse(null);
				String requestBody = "No Body";
				if (postDataEntries != null && !postDataEntries.isEmpty()) {
					try {
						requestBody = postDataEntries.stream().map(PostDataEntry::getBytes)
								.map(bytes -> new String(Base64.getDecoder().decode(bytes.get())))
								.collect(Collectors.joining("\n"));
					} catch (Exception e) {
						logger.error("Error on ", e);
					}
				}

				String responseBody ="NA";
				try {
					GetResponseBodyResponse bodyResponse = devTools
							.send(Network.getResponseBody(response.getRequestId()));
					responseBody = bodyResponse.getBody();
//					if (bodyResponse.getBase64Encoded()) {
//						responseBody = new String(Base64.getDecoder().decode(responseBody));
//					}
				} catch (Exception e) {
					logger.error("Error fetching response body: {}", e.getMessage());
				}
		        logger.info("-----------------------------------");
				logger.info("REQUEST URL: {}", req.getUrl());
				logger.info("METHOD: {}", req.getMethod());
				logger.info("BODY: {}", requestBody);
				logger.info("STATUS: {} {}", res.getStatus(), res.getStatusText());
				logger.info("RESPONSE BODY: {}", responseBody);
		        logger.info("-----------------------------------");

			}

		});
	}

	/**
	 * Enables console logging to capture JavaScript errors.
	 */
	public void enableConsoleLogging() {
		devTools.send(Log.enable());

		// Capture console errors
		devTools.addListener(Log.entryAdded(), logEntry -> {
			if (logEntry.getLevel().toString().equalsIgnoreCase("ERROR")) {
				logger.error("CONSOLE ERROR: {}", logEntry.getText());
				logger.error("SEVERITY: {}", logEntry.getLevel());
			}
		});
	}
}

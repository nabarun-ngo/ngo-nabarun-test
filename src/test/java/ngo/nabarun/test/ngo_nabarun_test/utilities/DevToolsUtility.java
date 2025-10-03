package ngo.nabarun.test.ngo_nabarun_test.utilities;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DevToolsUtility {
	private final Page page;
	private static final Logger logger = LogManager.getLogger(DevToolsUtility.class);
	public static Map<String, Request> requestMap = new HashMap<>();

	public DevToolsUtility(Page page) {
        this.page = page;
        logger.info("Starting Playwright browser session...");
	}

	/**
	 * Enables network logging to capture requests and responses.
	 */
	public void enableNetworkLogging() {
	    page.onRequest(request -> {
	        logger.info("Request: " + request.url());
	        requestMap.put(request.url(), request);
	    });
	    page.onResponse(response -> {
	        logger.info("Response: " + response.url() + " Status: " + response.status());
	    });
	}

	public void enableConsoleLogging() {
	    page.onConsoleMessage(msg -> {
	        logger.info("Console: " + msg.text());
	    });
	}
}

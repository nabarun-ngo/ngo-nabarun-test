package ngo.nabarun.test.ngo_nabarun_test.hooks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLWarning;
import java.util.List;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ScreenshotType;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DevToolsUtility;
import ngo.nabarun.test.ngo_nabarun_test.utils.DBUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.MemoryAppender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class TestHooks {
    private static final Logger logger = LogManager.getLogger(TestHooks.class);
    private final ScenarioContext scenarioContext;
    private Browser browser;
    private BrowserContext context;
    private Playwright playwright;
    private APIRequestContext requestContext;
    private final ThreadLocal<Long> scenarioStartTime = new ThreadLocal<>();

    private static final String SCREENSHOTS_DIR = "target/screenshots";
    private static final String SCENARIO_LOGS_DIR = "target/scenario-logs";

    public TestHooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    // -----------------------------------------------------------------------
    // Suite lifecycle
    // -----------------------------------------------------------------------

    @BeforeAll
    public static void seedTestData() {
        String scriptPath = "db_scripts/test_data_insert.sql";
        logger.info("===== [SUITE SETUP] Executing test data seed script: {} =====", scriptPath);
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(scriptPath)) {
            if (is == null) {
                throw new IllegalStateException("Seed script not found on classpath: " + scriptPath);
            }
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            DBUtils.getJdbi().useHandle(handle -> {
                handle.execute(sql);
                SQLWarning warning = handle.getConnection().getWarnings();
                while (warning != null) {
                    logger.info("[DB NOTICE] {}", warning.getMessage());
                    warning = warning.getNextWarning();
                }
            });
            logger.info("===== [SUITE SETUP] Seed script executed successfully =====");
        } catch (Exception e) {
            logger.error("===== [SUITE SETUP] Seed script FAILED — tests may be running against incomplete data =====", e);
            throw new RuntimeException("Failed to execute test data seed script: " + scriptPath, e);
        }
    }

    @AfterAll()
    public static void releaseConnectionPool() {
        DBUtils.close();
        logger.info("Database connection pool closed.");
        generateAllureEnvironmentProperties();
    }

    private static void generateAllureEnvironmentProperties() {
        try {
            Path allureResultsDir = Paths.get("target/allure-results");
            Files.createDirectories(allureResultsDir);
            Path envPropsFile = allureResultsDir.resolve("environment.properties");
            java.util.Properties props = new java.util.Properties();
            props.setProperty("Project", "Nabarun");
            props.setProperty("Test_Type", "Automated");
            props.setProperty("Browser", Configs.BROWSER != null ? Configs.BROWSER : "Unknown");
            props.setProperty("Headless_Mode", String.valueOf(Configs.IS_HEADLESS));
            props.setProperty("Base_URL", Configs.API_BASE_URL != null ? Configs.API_BASE_URL : "Unknown");
            props.setProperty("OS", System.getProperty("os.name"));
            try (java.io.OutputStream os = Files.newOutputStream(envPropsFile)) {
                props.store(os, "Allure Environment Properties");
            }
            logger.info("Generated Allure environment.properties");
        } catch (IOException e) {
            logger.warn("Could not generate Allure environment.properties: {}", e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Scenario lifecycle
    // -----------------------------------------------------------------------

    @Before()
    public void initializeTestDriver(Scenario scenario) {
        // Sanitise scenario name for use as a filename and Log4j ThreadContext key.
        // The RoutingAppender in log4j2.xml uses this key to write
        // target/scenario-logs/<scenarioName>.log in real time.
        String sanitizedName = toSafeFilename(scenario.getName());
        ThreadContext.put("scenarioName", sanitizedName);

        MemoryAppender.clear();
        scenarioContext.reset();
        scenarioStartTime.set(System.currentTimeMillis());

        logger.info("***** Scenario Started  : {} *****", scenario.getName());
        logger.info("***** Sanitised name    : {} *****", sanitizedName);
        logger.info("***** Tags              : {} *****", scenario.getSourceTagNames());
        logger.info("***** Log file          : {}/{}.log *****", SCENARIO_LOGS_DIR, sanitizedName);

        playwright = Playwright.create();

        if (scenario.getSourceTagNames().contains("@api")) {
            logger.info("API test detected. Initialising API request context.");
            requestContext = playwright.request().newContext(new APIRequest.NewContextOptions()
                    .setBaseURL(Configs.API_BASE_URL)
                    .setTimeout(Configs.IMPLICIT_WAIT)
                    .setIgnoreHTTPSErrors(true));
            scenarioContext.setRequestContext(requestContext);
        } else {
            String browserName = Configs.BROWSER.toLowerCase();
            BrowserType.LaunchOptions launchOptions = switch (browserName) {
                case "chrome" -> new BrowserType.LaunchOptions().setChannel("chrome");
                case "edge"   -> new BrowserType.LaunchOptions().setChannel("msedge");
                default       -> throw new IllegalArgumentException("Unexpected browser: " + browserName);
            };

            launchOptions.setHeadless(Configs.IS_HEADLESS);
            launchOptions.setArgs(List.of("--start-maximized"));
            launchOptions.setSlowMo(250);

            logger.info("Launching browser: {} in {} mode.", browserName,
                    Configs.IS_HEADLESS ? "headless" : "headed");
            browser = playwright.chromium().launch(launchOptions);
            context = browser.newContext(new Browser.NewContextOptions()
                    .setPermissions(List.of("notifications"))
                    .setViewportSize(null));

            Page page = context.newPage();
            logger.info("New page created; default timeout set to {}ms.", Configs.IMPLICIT_WAIT);
            page.setDefaultTimeout(Configs.IMPLICIT_WAIT);
            scenarioContext.setPage(page);
            ngo.nabarun.test.ngo_nabarun_test.utils.StepState.setPage(page);
            DevToolsUtility devToolsUtility = new DevToolsUtility(scenarioContext);
            devToolsUtility.enableConsoleLogging(true);
            devToolsUtility.enableNetworkLogging(true);
        }
    }

    @After()
    public void collectLogsAndTearDown(Scenario scenario) throws InterruptedException, IOException {
        long duration = System.currentTimeMillis() - scenarioStartTime.get();
        Page page = scenarioContext.getPage();
        String sanitizedName = ThreadContext.get("scenarioName");

        logger.info("***** Scenario Finished : {} *****", scenario.getName());
        logger.info("***** Status            : {} *****", scenario.getStatus());
        logger.info("***** Duration          : {}ms *****", duration);

        // Collect the in-memory log captured by MemoryAppender for this thread.
        // This is written to a per-scenario file and, on failure, attached to both
        // Cucumber JSON and Allure results.
        String executionLog = MemoryAppender.getAndClearLog();

        // Always persist the per-scenario log file so it is available for both
        // passing and failing scenarios (useful for debugging flaky tests).
        flushScenarioLogToDisk(sanitizedName, executionLog);

        if (scenario.isFailed()) {
            collectFailureDiagnostics(scenario, page, sanitizedName, executionLog);
        }

        // ---- cleanup ----
        if (page != null)          page.close();
        if (context != null)       context.close();
        if (browser != null)       browser.close();
        if (requestContext != null) requestContext.dispose();
        if (playwright != null)    playwright.close();

        scenarioStartTime.remove();
        ThreadContext.clearMap();
        ngo.nabarun.test.ngo_nabarun_test.utils.StepState.clear();
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Always write the full scenario log to disk so it is available even when a
     * scenario passes (useful for post-run audits and debugging flaky scenarios).
     */
    private void flushScenarioLogToDisk(String sanitizedName, String content) {
        try {
            Files.createDirectories(Paths.get(SCENARIO_LOGS_DIR));
            Path logPath = Paths.get(SCENARIO_LOGS_DIR, sanitizedName + ".log");
            Files.writeString(logPath, content, StandardCharsets.UTF_8);
            logger.debug("Per-scenario log written to {}", logPath);
        } catch (IOException e) {
            logger.warn("Could not write per-scenario log file for '{}': {}", sanitizedName, e.getMessage());
        }
    }

    /**
     * On failure: capture every diagnostic artifact and attach it to both the
     * Cucumber report (JSON / HTML) and the Allure report.
     */
    private void collectFailureDiagnostics(Scenario scenario, Page page,
                                            String sanitizedName, String executionLog)
            throws IOException {

        logger.info(">>> Scenario FAILED — collecting diagnostic artifacts ...");

        // 1. Execution log (text) ─────────────────────────────────────────────
        byte[] logBytes = executionLog.getBytes(StandardCharsets.UTF_8);
        // Cucumber JSON / HTML
        scenario.attach(logBytes, "text/plain", "execution.log");
        // Allure
        Allure.addAttachment("Execution Log", "text/plain",
                new ByteArrayInputStream(logBytes), ".log");
        logger.info(">>> Attached: execution.log");

        if (page != null) {
            // 2. Screenshot (PNG) ─────────────────────────────────────────────
            captureAndAttachScreenshot(scenario, page, sanitizedName);

            // 3. Page source (HTML) ───────────────────────────────────────────
            captureAndAttachPageSource(scenario, page, sanitizedName);
        }
    }

    private void captureAndAttachScreenshot(Scenario scenario, Page page, String sanitizedName)
            throws IOException {
        try {
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions().setType(ScreenshotType.PNG).setFullPage(true));

            // Persist to disk
            Files.createDirectories(Paths.get(SCREENSHOTS_DIR));
            Path screenshotPath = Paths.get(SCREENSHOTS_DIR, sanitizedName + ".png");
            Files.write(screenshotPath, screenshot);

            // Cucumber JSON / HTML
            scenario.attach(screenshot, "image/png", sanitizedName + ".png");
            // Allure
            Allure.addAttachment("Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");

            logger.info(">>> Attached: screenshot → {}", screenshotPath);
        } catch (Exception e) {
            logger.warn(">>> Could not capture screenshot: {}", e.getMessage());
        }
    }

    private void captureAndAttachPageSource(Scenario scenario, Page page, String sanitizedName) {
        try {
            String pageSource = page.content();
            byte[] sourceBytes = pageSource.getBytes(StandardCharsets.UTF_8);

            // Cucumber JSON / HTML
            scenario.attach(sourceBytes, "text/html", "page-source.html");
            // Allure
            Allure.addAttachment("Page Source", "text/html",
                    new ByteArrayInputStream(sourceBytes), ".html");

            logger.info(">>> Attached: page-source.html ({} bytes)", sourceBytes.length);
        } catch (Exception e) {
            logger.warn(">>> Could not capture page source: {}", e.getMessage());
        }
    }

    /**
     * Produce a filename-safe string from any scenario name.
     * Replaces non-alphanumeric characters with underscores and trims leading/trailing underscores.
     */
    private static String toSafeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\-]", "_")
                   .replaceAll("_{2,}", "_")
                   .replaceAll("^_|_$", "")
                   .toLowerCase();
    }
}

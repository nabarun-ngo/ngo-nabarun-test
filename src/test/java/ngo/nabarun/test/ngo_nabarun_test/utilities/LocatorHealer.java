package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Auto-healing locator resolution: tries a list of strategies in order and returns
 * the first locator that finds a visible element. Enable with system property
 * {@code AUTO_HEAL=true} or env {@code AUTO_HEAL=true}.
 */
public final class LocatorHealer {

	private static final Logger log = LogManager.getLogger(LocatorHealer.class);
	private static final int HEAL_TIMEOUT_MS = 3_000;

	/** Enable fallback resolution when primary (Playwright role/label) fails. */
	public static boolean isHealingEnabled() {
		String v = System.getProperty("AUTO_HEAL", System.getenv("AUTO_HEAL"));
		return "true".equalsIgnoreCase(v);
	}

	/**
	 * Tries each strategy in order; returns the first locator that becomes visible within
	 * {@code timeoutMs}. If none succeed, returns the last strategy's locator so the
	 * subsequent failure message is clear. When healing is enabled and a fallback is used,
	 * logs a warning so teams can fix the primary locator.
	 *
	 * @param strategies list of locator suppliers (e.g. Playwright role first, then XPath)
	 * @param timeoutMs  max wait per strategy (milliseconds)
	 * @param elementDesc description for logging (e.g. "button 'Submit'")
	 * @return first locator that is visible, or last locator (may throw on use)
	 */
	public static Locator firstVisible(List<Supplier<Locator>> strategies, int timeoutMs, String elementDesc) {
		if (strategies == null || strategies.isEmpty()) {
			throw new IllegalArgumentException("At least one strategy required");
		}
		Exception lastError = null;
		for (int i = 0; i < strategies.size(); i++) {
			try {
				Locator loc = strategies.get(i).get();
				loc.waitFor(new Locator.WaitForOptions()
						.setTimeout(timeoutMs)
						.setState(WaitForSelectorState.VISIBLE));
				if (i > 0 && isHealingEnabled()) {
					log.warn("AUTO_HEAL: used fallback strategy {} for {}", i + 1, elementDesc);
				}
				return loc.count() > 1 ? loc.first() : loc;
			} catch (Exception e) {
				lastError = e;
			}
		}
		if (isHealingEnabled() && lastError != null) {
			log.warn("AUTO_HEAL: all strategies failed for {}: {}", elementDesc, lastError.getMessage());
		}
		return strategies.get(strategies.size() - 1).get();
	}

	public static Locator firstVisible(List<Supplier<Locator>> strategies, String elementDesc) {
		return firstVisible(strategies, HEAL_TIMEOUT_MS, elementDesc);
	}

	/** Escape single quote for use inside XPath single-quoted string (double the quote). */
	public static String escapeXPathString(String value) {
		return value == null ? "" : value.replace("'", "''");
	}
}

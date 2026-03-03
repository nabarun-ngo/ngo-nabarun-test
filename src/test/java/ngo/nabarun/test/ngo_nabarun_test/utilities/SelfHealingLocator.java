package ngo.nabarun.test.ngo_nabarun_test.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Healenium-like wrapper: when an action (click, fill, clear) fails, finds an
 * alternative locator from the live DOM and retries once. Enable with
 * {@code SELF_HEALING=true} (system property or env).
 */
public final class SelfHealingLocator {

	private static final Logger log = LogManager.getLogger(SelfHealingLocator.class);

	private final Page page;
	private final Locator parent;
	private final String elementType;
	private final String elementName;
	private final boolean healingEnabled;

	private Locator current;

	public SelfHealingLocator(Page page, Locator parent, Locator initialLocator,
			String elementType, String elementName) {
		this.page = page;
		this.parent = parent;
		this.current = initialLocator;
		this.elementType = elementType == null ? "" : elementType;
		this.elementName = elementName == null ? "" : elementName;
		this.healingEnabled = isHealingEnabled();
	}

	public static boolean isHealingEnabled() {
		String v = System.getProperty("SELF_HEALING", System.getenv("SELF_HEALING"));
		return "true".equalsIgnoreCase(v);
	}

	/** Returns the current locator (possibly replaced after a heal). Use for assertions and helpers. */
	public Locator getLocator() {
		return current;
	}

	public void click() {
		runWithHealing("click", () -> current.click());
	}

	public void fill(String value) {
		runWithHealing("fill", () -> current.fill(value));
	}

	public void clear() {
		runWithHealing("clear", () -> current.clear());
	}

	public void scrollIntoViewIfNeeded() {
		runWithHealing("scrollIntoViewIfNeeded", () -> current.scrollIntoViewIfNeeded());
	}

	public void hover() {
		runWithHealing("hover", () -> current.hover());
	}

	private void runWithHealing(String actionName, Runnable action) {
		try {
			action.run();
		} catch (Exception e) {
			if (!healingEnabled) throw e;
			Locator healed = RuntimeHealer.findAlternative(page, parent, elementType, elementName);
			if (healed == null) {
				log.warn("SELF_HEALING: no alternative found for {} '{}', rethrowing", elementType, elementName);
				throw e;
			}
			log.warn("SELF_HEALING: healed {} '{}' and retrying {}", elementType, elementName, actionName);
			this.current = healed;
			action.run();
		}
	}
}

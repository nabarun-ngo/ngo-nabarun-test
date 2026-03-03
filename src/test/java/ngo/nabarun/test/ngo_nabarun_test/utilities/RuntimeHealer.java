package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Healenium-like runtime healing: when a locator fails, search the live DOM
 * for a matching element by role, label, or text and return an alternative
 * locator.
 * Matching is case-insensitive and supports partial (substring) text.
 */
public final class RuntimeHealer {

	private static final Logger log = LogManager.getLogger(RuntimeHealer.class);
	private static final int HEAL_WAIT_MS = 2_000;

	/**
	 * Find an alternative locator for the given element type and name by querying
	 * the current page (or parent). Uses Playwright's role/label/text APIs.
	 * Matching is case-insensitive and partial (substring).
	 *
	 * @param page        current page
	 * @param parent      optional scope (e.g. accordion); use null for full page
	 * @param elementType button, link, text, textbox, dropdown, etc.
	 * @param elementName user-facing name (e.g. "Submit", "Donor name")
	 * @return alternative locator, or null if none found
	 */
	public static Locator findAlternative(Page page, Locator parent, String elementType, String elementName) {
		if (page == null)
			return null;
		Locator scope = parent != null ? parent : page.locator(":root");
		String type = elementType == null ? "" : elementType.toLowerCase().trim();
		String name = elementName == null ? "" : elementName.trim();
		if (name.isEmpty())
			return null;

		try {
			return switch (type) {
				case "button" -> findButton(scope, name);
				case "link" -> findLink(scope, name);
				case "text" -> findText(scope, name);
				case "textbox", "textarea" -> findInputByLabel(scope, name);
				case "dropdown" -> findDropdown(scope, name);
				case "fileinput" -> findFileInput(scope, name);
				default -> tryByRoleOrText(scope, name);
			};
		} catch (Exception e) {
			log.debug("RuntimeHealer: no alternative for {} '{}': {}", elementType, elementName, e.getMessage());
			return null;
		}
	}

	/**
	 * Builds a case-insensitive regex that matches any text containing {@code name}
	 * (partial).
	 */
	private static Pattern partialCaseInsensitivePattern(String name) {
		return Pattern.compile(".*" + Pattern.quote(name) + ".*", Pattern.CASE_INSENSITIVE);
	}

	/** Playwright :has-text() with regex for partial + case-insensitive match. */
	private static String hasTextRegexSelector(String tag, String name) {
		String regexPart = name.replace("\\", "\\\\").replace("/", "\\/");
		return tag + ":has-text(/" + regexPart + "/i)";
	}

	private static Locator findButton(Locator scope, String name) {
		// getByRole setName: default is already case-insensitive, partial
		Locator byRole = scope.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(name));
		if (waitAndCount(byRole) > 0)
			return byRole.first();
		Locator byText = scope.locator(hasTextRegexSelector("button", name));
		if (waitAndCount(byText) > 0)
			return byText.first();
		return null;
	}

	private static Locator findLink(Locator scope, String name) {
		Locator byRole = scope.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(name));
		if (waitAndCount(byRole) > 0)
			return byRole.first();
		Locator byText = scope.locator(hasTextRegexSelector("a", name));
		if (waitAndCount(byText) > 0)
			return byText.first();
		return null;
	}

	private static Locator findText(Locator scope, String name) {
		// Partial + case-insensitive via Pattern
		Locator byText = scope.getByText(partialCaseInsensitivePattern(name));
		if (waitAndCount(byText) > 0)
			return byText.first();
		Locator byRole = scope.getByRole(AriaRole.GENERIC, new Locator.GetByRoleOptions().setName(name));
		if (waitAndCount(byRole) > 0)
			return byRole.first();
		return null;
	}

	private static Locator findInputByLabel(Locator scope, String name) {
		// Exact first, then partial + case-insensitive
		Locator byLabel = scope.getByLabel(name);
		if (waitAndCount(byLabel) > 0)
			return byLabel.first();
		Locator byLabelPattern = scope.getByLabel(partialCaseInsensitivePattern(name));
		if (waitAndCount(byLabelPattern) > 0)
			return byLabelPattern.first();
		Locator byPlaceholder = scope.getByPlaceholder(name);
		if (waitAndCount(byPlaceholder) > 0)
			return byPlaceholder.first();
		Locator byPlaceholderPattern = scope.getByPlaceholder(partialCaseInsensitivePattern(name));
		if (waitAndCount(byPlaceholderPattern) > 0)
			return byPlaceholderPattern.first();
		return null;
	}

	private static Locator findDropdown(Locator scope, String name) {
		Locator byRole = scope.getByRole(AriaRole.COMBOBOX, new Locator.GetByRoleOptions().setName(name));
		if (waitAndCount(byRole) > 0)
			return byRole.first();
		Locator byLabel = scope.getByLabel(name);
		if (waitAndCount(byLabel) > 0)
			return byLabel.first();
		Locator byLabelPattern = scope.getByLabel(partialCaseInsensitivePattern(name));
		if (waitAndCount(byLabelPattern) > 0)
			return byLabelPattern.first();
		return null;
	}

	private static Locator findFileInput(Locator scope, String name) {
		Locator byLabel = scope.getByLabel(name);
		if (waitAndCount(byLabel) > 0)
			return byLabel.first();
		Locator byLabelPattern = scope.getByLabel(partialCaseInsensitivePattern(name));
		if (waitAndCount(byLabelPattern) > 0)
			return byLabelPattern.first();
		Locator byRole = scope.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(name));
		if (waitAndCount(byRole) > 0)
			return byRole.first();
		return null;
	}

	private static Locator tryByRoleOrText(Locator scope, String name) {
		Locator byText = scope.getByText(partialCaseInsensitivePattern(name));
		if (waitAndCount(byText) > 0)
			return byText.first();
		Locator byRole = scope.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(name));
		if (waitAndCount(byRole) > 0)
			return byRole.first();
		return null;
	}

	private static int waitAndCount(Locator locator) {
		try {
			locator.waitFor(new Locator.WaitForOptions().setTimeout(HEAL_WAIT_MS));
			return locator.count();
		} catch (Exception e) {
			return 0;
		}
	}
}

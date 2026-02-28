package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import ngo.nabarun.common.util.CommonUtil;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class BasePageObjects {
    public enum FindBy {
        TEXT, LABEL, XPATH, CSS, ANY
    }

    protected final ScenarioContext scenarioContext;

    public BasePageObjects(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    protected final Page page() {
        return scenarioContext.getPage();
    }

    protected final Locator scope(Locator parent) {
        return parent != null ? parent : page().locator(":root");
    }

    protected final Locator findLocator(String selector, Locator parent, FindBy selectBy) {
        Page root = page();
        Locator locator = switch (selectBy) {
            case TEXT -> scope(parent).getByText(selector);
            case LABEL -> scope(parent).getByLabel(selector);
            case XPATH -> parent != null ? parent.locator("xpath=." + selector) : root.locator("xpath=" + selector);
            case CSS -> parent != null ? parent.locator("css=" + selector) : root.locator("css=" + selector);
            case ANY -> parent != null ? parent.locator(selector) : root.locator(selector);
        };
        return maybeHighlight(locator);
    }

    protected final Locator findLocator(String selector) {
        return findLocator(selector, null, FindBy.ANY);
    }

    protected final Locator maybeHighlight(Locator locator) {
        if (!isHighlightEnabled())
            return locator;
        locator.evaluate("el => {" + "  let count = 0;" + "  const blink = () => {"
                + "    el.style.outline = (count % 2 === 0) ? '3px solid red' : '';" + "    count++;"
                + "    if (count < 6) setTimeout(blink, 250);" + "  };" + "  blink();" + "}");
        return locator;
    }

    /**
     * Set system property DEBUG_HIGHLIGHT=true to visually blink located elements
     * (for debugging).
     */
    private static boolean isHighlightEnabled() {
        return "true".equalsIgnoreCase(CommonUtil.getEnvProperty("DEBUG_HIGHLIGHT", "false"));
    }

}

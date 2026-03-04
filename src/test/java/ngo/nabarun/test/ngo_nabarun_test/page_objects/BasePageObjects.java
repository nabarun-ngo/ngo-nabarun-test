package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class BasePageObjects {
    public enum FindBy {
        TEXT, LABEL, XPATH, CSS, ANY
    }

    public enum HighlightMode {
        IDENTIFICATION, OPERATION, CLICK, ENTER, SELECT, UPLOAD
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
        if (isHighlightEnabled()) {
            highlight(locator, HighlightMode.IDENTIFICATION);
        }
        return locator;
    }

    /**
     * Highlights an element based on the mode.
     * IDENTIFICATION: Blink effect for finding elements.
     * OPERATION: Solid highlight for clicks/entries.
     */
    public static void highlight(Locator locator, HighlightMode mode) {
        if (!isHighlightEnabled())
            return;
        try {
            String color;
            String bgColor;
            boolean blink = false;

            switch (mode) {
                case IDENTIFICATION -> {
                    color = "#00ffff"; // Cyan
                    bgColor = "transparent";
                    blink = true;
                }
                case CLICK -> {
                    color = "#ff00ff"; // Magenta
                    bgColor = "rgba(255, 0, 255, 0.2)";
                }
                case ENTER -> {
                    color = "#00ff00"; // Green
                    bgColor = "rgba(0, 255, 0, 0.2)";
                }
                case SELECT -> {
                    color = "#ffa500"; // Orange
                    bgColor = "rgba(255, 165, 0, 0.2)";
                }
                case UPLOAD -> {
                    color = "#0000ff"; // Blue
                    bgColor = "rgba(0, 0, 255, 0.2)";
                }
                default -> { // OPERATION or fallback
                    color = "#ff00ff";
                    bgColor = "rgba(255, 0, 255, 0.2)";
                }
            }

            if (blink) {
                locator.evaluate("el => {" + "  let count = 0;" + "  const blinkFn = () => {"
                        + "    el.style.outline = (count % 2 === 0) ? '3px solid " + color + "' : '';" + "    count++;"
                        + "    if (count < 6) setTimeout(blinkFn, 200);" + "  };" + "  blinkFn();" + "}");
            } else {
                locator.evaluate("el => { " +
                        "  const originalOutline = el.style.outline; " +
                        "  const originalBg = el.style.backgroundColor; " +
                        "  el.style.outline = '3px solid " + color + "'; " +
                        "  el.style.backgroundColor = '" + bgColor + "'; " +
                        "  setTimeout(() => { " +
                        "    el.style.outline = originalOutline; " +
                        "    el.style.backgroundColor = originalBg; " +
                        "  }, 800); " +
                        "}");
            }
        } catch (Exception e) {
            // Silently ignore highlight errors
        }
    }

    /**
     * Set system property DEBUG_HIGHLIGHT=true to visually blink located elements
     * (for debugging).
     */
    private static boolean isHighlightEnabled() {
        return Configs.IS_DEBUG_HIGHLIGHT;
    }

}

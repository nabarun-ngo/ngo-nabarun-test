package ngo.nabarun.test.ngo_nabarun_test.utils;

import com.microsoft.playwright.Page;

public class StepState {
    private static final ThreadLocal<String> currentStep = new ThreadLocal<>();
    private static final ThreadLocal<Page> currentPage = new ThreadLocal<>();

    public static void setCurrentStep(String stepName) {
        currentStep.set(stepName);
    }

    public static String getCurrentStep() {
        return currentStep.get();
    }

    private static final ThreadLocal<Page> lastPageWithListener = new ThreadLocal<>();

    public static void setPage(Page page) {
        currentPage.set(page);
    }

    public static Page getPage() {
        return currentPage.get();
    }

    public static boolean hasListener(Page page) {
        return lastPageWithListener.get() == page;
    }

    public static void setListenerAdded(Page page) {
        lastPageWithListener.set(page);
    }

    public static void clear() {
        currentStep.remove();
        currentPage.remove();
        lastPageWithListener.remove();
    }
}

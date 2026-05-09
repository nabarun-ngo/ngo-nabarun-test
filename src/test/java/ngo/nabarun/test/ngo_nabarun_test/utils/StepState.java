package ngo.nabarun.test.ngo_nabarun_test.utils;

public class StepState {
    private static final ThreadLocal<String> currentStep = new ThreadLocal<>();

    public static void setCurrentStep(String stepName) {
        currentStep.set(stepName);
    }

    public static String getCurrentStep() {
        return currentStep.get();
    }

    public static void clear() {
        currentStep.remove();
    }
}

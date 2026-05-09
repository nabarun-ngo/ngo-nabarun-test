package ngo.nabarun.test.ngo_nabarun_test.hooks;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.cucumber.plugin.event.Status;

import com.microsoft.playwright.Page;

import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.utils.StepState;

public class EventListener implements ConcurrentEventListener {
    private static final Logger logger = LogManager.getLogger(EventListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishHandler);
    }

    private final EventHandler<TestStepStarted> stepStartedHandler = event -> {
        if (event.getTestStep() instanceof PickleStepTestStep pickleStep) {
            String stepName = pickleStep.getStep().getText();
            StepState.setCurrentStep(stepName);
            logger.info("-----Step '" + stepName + "' Started-----");
            updateOverlay(stepName);
        }
    };

    private void updateOverlay(String stepName) {
        if (Configs.IS_SHOW_STEP_OVERLAY) {
            Page page = StepState.getPage();
            if (page != null) {
                // Initial injection for current step
                injectOverlay(page, stepName);

                // Ensure overlay persists across navigations/reloads on this page
                if (!StepState.hasListener(page)) {
                    page.onDOMContentLoaded(p -> {
                        String currentStep = StepState.getCurrentStep();
                        if (currentStep != null) {
                            injectOverlay(p, currentStep);
                        }
                    });
                    StepState.setListenerAdded(page);
                }
            }
        }
    }

    private void injectOverlay(Page page, String stepName) {
        try {
            page.evaluate("step => {" +
                    "let el = document.getElementById('cucumber-step-overlay');" +
                    "if (!el) {" +
                    "  el = document.createElement('div');" +
                    "  el.id = 'cucumber-step-overlay';" +
                    "  el.style.position = 'fixed';" +
                    "  el.style.bottom = '10px';" +
                    "  el.style.right = '10px';" +
                    "  el.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';" +
                    "  el.style.color = 'white';" +
                    "  el.style.padding = '10px 15px';" +
                    "  el.style.borderRadius = '8px';" +
                    "  el.style.zIndex = '10000';" +
                    "  el.style.fontSize = '14px';" +
                    "  el.style.fontFamily = 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif';" +
                    "  el.style.boxShadow = '0 4px 6px rgba(0,0,0,0.3)';" +
                    "  el.style.pointerEvents = 'none';" +
                    "  el.style.transition = 'opacity 0.3s';" +
                    "  document.body.appendChild(el);" +
                    "}" +
                    "el.innerHTML = '<b>Current Step:</b><br/>' + step;" +
                    "}", stepName);
        } catch (Exception e) {
            // ignore if page is not ready or closed
        }
    }

    private final EventHandler<TestStepFinished> stepFinishHandler = event -> {
        if (event.getTestStep() instanceof PickleStepTestStep pickleStep) {
            String stepName = pickleStep.getStep().getText();
            Result result = event.getResult();
            Status status = result.getStatus();
            long duration = result.getDuration().toMillis();

            if (status.is(Status.FAILED) && result.getError() != null) {
                logger.error("Exception in Step '" + stepName + "': " + result.getError().getMessage(),
                        result.getError());
            }

            logger.info("-----Step '" + stepName + "' " + status.name() + " in " + duration + "ms-----");

        }
    };
}

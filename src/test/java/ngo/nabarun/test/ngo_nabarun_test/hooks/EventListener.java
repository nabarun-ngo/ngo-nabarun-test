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
                    "let host = document.getElementById('cucumber-step-overlay-host');" +
                    "if (!host) {" +
                    "  host = document.createElement('div');" +
                    "  host.id = 'cucumber-step-overlay-host';" +
                    "  document.body.appendChild(host);" +
                    "  const shadow = host.attachShadow({mode: 'open'});" +
                    "  const style = document.createElement('style');" +
                    "  style.textContent = `" +
                    "    #overlay {" +
                    "      position: fixed;" +
                    "      bottom: 10px;" +
                    "      right: 10px;" +
                    "      background-color: rgba(0, 0, 0, 0.7);" +
                    "      color: white;" +
                    "      padding: 10px 15px;" +
                    "      border-radius: 8px;" +
                    "      z-index: 2147483647;" +
                    "      font-size: 14px;" +
                    "      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
                    "      box-shadow: 0 4px 6px rgba(0,0,0,0.3);" +
                    "      pointer-events: none;" +
                    "      transition: opacity 0.3s;" +
                    "      white-space: pre-wrap;" +
                    "      line-height: 1.4;" +
                    "    }" +
                    "    #overlay::before {" +
                    "      content: 'Current Step:';" +
                    "      font-weight: bold;" +
                    "      display: block;" +
                    "      margin-bottom: 4px;" +
                    "      text-decoration: underline;" +
                    "    }" +
                    "    #overlay::after {" +
                    "      content: var(--step-name);" +
                    "    }`;" +
                    "  const el = document.createElement('div');" +
                    "  el.id = 'overlay';" +
                    "  shadow.appendChild(style);" +
                    "  shadow.appendChild(el);" +
                    "}" +
                    "host.shadowRoot.getElementById('overlay').style.setProperty('--step-name', JSON.stringify(step));" +
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

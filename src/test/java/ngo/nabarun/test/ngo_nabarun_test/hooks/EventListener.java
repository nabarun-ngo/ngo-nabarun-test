package ngo.nabarun.test.ngo_nabarun_test.hooks;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestStepStarted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class EventListener implements ConcurrentEventListener {
    private static final Logger logger = LogManager.getLogger(EventListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, testCaseStartedHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestCaseFinished.class, scenarioFinishedHandler);
    }

    private final EventHandler<TestCaseStarted> testCaseStartedHandler = event -> {
        String scenarioName = event.getTestCase().getName().replaceAll("[^a-zA-Z0-9-]", "_");
        ThreadContext.put("scenarioName", scenarioName);
    };

    private final EventHandler<TestStepStarted> stepStartedHandler = event -> {
        if (event.getTestStep() instanceof PickleStepTestStep pickleStep) {
            String stepName = pickleStep.getStep().getText();
            logger.info("Step: " + stepName);
        }
    };

    private final EventHandler<TestCaseFinished> scenarioFinishedHandler = event -> {
        if (event.getResult().getStatus().is(io.cucumber.plugin.event.Status.FAILED)) {
            Throwable error = event.getResult().getError();
            if (error != null) {
                logger.error("Exception: " + error.getMessage(), error);
            }
        }
    };
}

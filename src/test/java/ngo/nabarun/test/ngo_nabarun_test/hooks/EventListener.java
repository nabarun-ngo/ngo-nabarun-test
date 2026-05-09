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
        }
    };

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

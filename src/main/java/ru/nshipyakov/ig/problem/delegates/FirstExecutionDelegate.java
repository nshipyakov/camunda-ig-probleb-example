package ru.nshipyakov.ig.problem.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FirstExecutionDelegate implements JavaDelegate {

    private static final Logger LOGGER = Logger.getLogger(FirstExecutionDelegate.class.getName());

    @Override public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("FirstExecutionDelegate");
    }
}

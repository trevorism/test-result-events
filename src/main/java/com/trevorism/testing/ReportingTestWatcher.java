package com.trevorism.testing;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public class ReportingTestWatcher implements TestExecutionListener {

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        System.out.println("rtw Test " + testIdentifier.getDisplayName() + " finished with result " + testExecutionResult.getStatus());
    }
}

package com.trevorism.testing;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReportingTestWatcherTest {

    @Test
    public void testPlanExecutionStarted() {
        ReportingTestWatcher reportingTestWatcher = new ReportingTestWatcher();
        reportingTestWatcher.testPlanExecutionStarted(null);
        assertEquals(reportingTestWatcher.getTestResults().size(), 0);
        assertNull(reportingTestWatcher.getProjectName());

    }
}
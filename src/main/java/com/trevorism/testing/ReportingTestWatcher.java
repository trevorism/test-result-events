package com.trevorism.testing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ReportingTestWatcher implements TestExecutionListener {

    private List<TestResult> testResults = new LinkedList<>();
    private boolean eventEnabled;
    private Instant startInstant;
    private String projectName;

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        System.out.println("In test plan execution started");
        projectName = System.getProperty("projectName");
        if(projectName != null && !projectName.isEmpty()) {
            eventEnabled = true;
        }
        startInstant = Instant.now();
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        System.out.println("In execution finished");

        TestResult testResult = new TestResult();
        testResult.setName(testIdentifier.getDisplayName());
        testResult.setSuccess(testExecutionResult.getStatus().equals(TestExecutionResult.Status.SUCCESSFUL));
        testResults.add(testResult);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        System.out.println("In test plan execution finished");
        //if(!eventEnabled){
        //    return;
        //}
        TestEvent event = new TestEvent();
        event.setService(System.getProperty("projectName"));
        event.setDate(new Date());
        event.setNumberOfTests(testResults.size());
        event.setDurationMillis(Instant.now().toEpochMilli() - startInstant.toEpochMilli());
        event.setSuccess(testResults.stream().allMatch(TestResult::isSuccess));

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
        System.out.println("EVENT ENABLED: " + eventEnabled + " " + projectName);
        System.out.println(gson.toJson(event));
    }
}

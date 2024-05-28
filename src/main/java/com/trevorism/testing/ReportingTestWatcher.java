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

import com.trevorism.http.HttpClient;
import com.trevorism.http.JsonHttpClient;

public class ReportingTestWatcher implements TestExecutionListener {

    private List<TestResult> testResults = new LinkedList<>();
    private boolean eventEnabled;
    private Instant startInstant;
    private String projectName;

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        projectName = System.getProperty("trevorism.test.event");
        if(projectName != null && !projectName.isEmpty()) {
            eventEnabled = true;
        }
        startInstant = Instant.now();
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        TestResult testResult = new TestResult();
        testResult.setName(testIdentifier.getDisplayName());
        testResult.setSuccess(testExecutionResult.getStatus().equals(TestExecutionResult.Status.SUCCESSFUL));
        testResults.add(testResult);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if(!eventEnabled){
            return;
        }
        TestEvent event = new TestEvent();
        event.setKind("unit");
        event.setService(System.getProperty("projectName"));
        event.setDate(new Date());
        event.setNumberOfTests(testResults.size());
        event.setDurationMillis(Instant.now().toEpochMilli() - startInstant.toEpochMilli());
        event.setSuccess(testResults.stream().allMatch(TestResult::isSuccess));

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
        HttpClient client = new JsonHttpClient();
        client.post("https://event.data.trevorism.com/event/testResult", gson.toJson(event));
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public boolean isEventEnabled() {
        return eventEnabled;
    }

    public Instant getStartInstant() {
        return startInstant;
    }

    public String getProjectName() {
        return projectName;
    }
}

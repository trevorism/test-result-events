package com.trevorism.testing;

import java.time.Instant;

public class TestResult {

    private String name;
    private int durationMillis;
    private boolean success;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}

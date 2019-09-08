package hudson.plugins.jira.extension;

import java.util.List;

/**
 * @author Merav Yaakov
 */

public class TestExecution {

    private List<String> tests;
    private List<String> summaries;

    public TestExecution(List<String> tests, List<String> summaries) {
        this.tests = tests;
        this.summaries = summaries;
    }

    public List<String> getTests() {
        return tests;
    }

    public List<String> getSummaries() {
        return summaries;
    }
}

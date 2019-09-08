package hudson.plugins.jira.extension;

import java.util.List;

/**
 * @author Merav Yaakov
 */

public class TestRun {

    private List<TestExecution> testExecutions;

    public TestRun(List<TestExecution> testExecutions) {
        this.testExecutions = testExecutions;
    }

    public List<TestExecution> getTestExecutions() {
        return testExecutions;
    }


    public static class TestExecution {
        private final String key;
        private final Long id;
        private final ExecutionStatus status;

        public TestExecution(String key, Long id, ExecutionStatus status) {
            this.key = key;
            this.id = id;
            this.status = status;
        }

        public String getKey() {
            return key;
        }

        public Long getId() {
            return id;
        }

        public ExecutionStatus getStatus() {
            return status;
        }
    }

    public enum ExecutionStatus {
        PASS("0", "PASS"), TODO("1", "TODO"),
        EXECUTING("2", "EXECUTING"), FAIL("3", "FAIL"),
        ABORTED("4", "ABORTED");

        private String id;
        private String name;

        ExecutionStatus(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}

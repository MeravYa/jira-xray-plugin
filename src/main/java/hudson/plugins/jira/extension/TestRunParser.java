package hudson.plugins.jira.extension;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import hudson.plugins.jira.extension.TestRun.ExecutionStatus;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Merav Yaakov
 */

public class TestRunParser implements JsonObjectParser<TestRun> {

    public TestRunParser() {}


    @Override
    public TestRun parse(JSONObject jsonObject) throws JSONException {
        JSONArray testExecutions = jsonObject.getJSONArray("entries");
        List<TestRun.TestExecution> testExecutionsList = new ArrayList<>();
        for(int i=0; i<testExecutions.length(); i++) {
            JSONObject testExecutionJson = testExecutions.getJSONObject(i);
            String key = testExecutionJson.getString("key");
            Long id = testExecutionJson.getLong("id");
            ExecutionStatus status = ExecutionStatus.valueOf(testExecutionJson.getJSONObject("status").getString("name"));
            TestRun.TestExecution testExecution = new TestRun.TestExecution(key, id, status);
            testExecutionsList.add(testExecution);
        }

        return new TestRun(testExecutionsList);

    }
}

package hudson.plugins.jira.extension;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.google.gson.Gson;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * @author Merav Yaakov
 */

public class TestExecutionParser implements JsonObjectParser<TestExecution> {

    public TestExecutionParser() {
    }

    @Override
    public TestExecution parse(JSONObject jsonObject) throws JSONException {
        JSONArray tests = jsonObject.getJSONArray("tests");
        Gson gson = new Gson();
        List<String> testsList = Arrays.asList(gson.fromJson(tests.toString(), String[].class));

        JSONArray summaries = jsonObject.getJSONArray("summaries");
        List<String> summariesList = Arrays.asList(gson.fromJson(summaries.toString(), String[].class));

        return new TestExecution(testsList, summariesList);
    }
}

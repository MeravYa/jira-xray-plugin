package hudson.plugins.jira.extension;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.ResponsePromise;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.util.concurrent.Promise;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author Merav Yaakov
 */

public class XrayJiraTestExecutionRestClient extends AbstractAsynchronousRestClient {

    private final URI baseUri;
    private JiraRestClient jiraRestClient;

    protected XrayJiraTestExecutionRestClient(URI baseUri, HttpClient client,
                                              JiraRestClient jiraRestClient) {
        super(client);
        this.baseUri = baseUri;
        this.jiraRestClient = jiraRestClient;
    }

    public Promise<TestExecution> createTestExecution(String testKey, String summary) throws ExecutionException, InterruptedException {
        UriBuilder uriBuilder = UriBuilder.fromUri(this.baseUri).path("test").path(testKey).path("createtestexec");
        Issue test = jiraRestClient.getIssueClient().getIssue(testKey).get();
        uriBuilder.queryParam("projectKey", test.getProject().getKey())
            .queryParam("priority", Objects.requireNonNull(test.getPriority()).getId())
            //.queryParam("assignee", Objects.requireNonNull(test.getAssignee()).getName())
            .queryParam("summary", summary);
        ResponsePromise responsePromise = this.client().newRequest(uriBuilder.build()).post();

        return this.callAndParse(responsePromise, new TestExecutionParser());
    }

    public Promise<Void> updateExecutionStatus(String testKey, String executionKey, String executionStatusId) {
        UriBuilder uriBuilder = UriBuilder.fromUri(this.baseUri).path("test").path(testKey).path("execute").path(executionKey);
        uriBuilder.queryParam("status", executionStatusId);
        return this.post(uriBuilder.build());
    }

}

package hudson.plugins.jira.extension;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.util.concurrent.Promise;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * @author Merav Yaakov
 */

public class XrayJiraTestRunRestClient extends AbstractAsynchronousRestClient {

    private final URI baseUri;
    private JiraRestClient jiraRestClient;

    protected XrayJiraTestRunRestClient(URI baseUri, HttpClient client,
                                        JiraRestClient jiraRestClient) {
        super(client);
        this.baseUri = baseUri;
        this.jiraRestClient = jiraRestClient;
    }

    public Promise<TestRun> getTestRun(String testKey) {
        UriBuilder uriBuilder = UriBuilder.fromUri(this.baseUri).path("test").path(testKey).path("testrun");

        return this.getAndParse(uriBuilder.build(), new TestRunParser());
    }

}

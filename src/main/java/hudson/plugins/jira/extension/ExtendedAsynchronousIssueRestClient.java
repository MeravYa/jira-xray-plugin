package hudson.plugins.jira.extension;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.api.MetadataRestClient;
import com.atlassian.jira.rest.client.api.SessionRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousIssueRestClient;
import com.atlassian.util.concurrent.Promise;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * @author Merav Yaakov
 */

public class ExtendedAsynchronousIssueRestClient extends AsynchronousIssueRestClient {

    private URI baseUri;

    public ExtendedAsynchronousIssueRestClient(URI baseUri, HttpClient client,
                                               SessionRestClient sessionRestClient,
                                               MetadataRestClient metadataRestClient) {
        super(baseUri, client, sessionRestClient, metadataRestClient);
        this.baseUri = baseUri;
    }

    public Promise<Issue> createIssue(JSONObject issueJson) {
        IssueTestParser parser = new IssueTestParser();
        UriBuilder uriBuilder = UriBuilder.fromUri(this.baseUri).path("issue");
        return this.postAndParse(uriBuilder.build(new Object[0]), issueJson, parser);
    }
}

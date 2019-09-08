package hudson.plugins.jira.extension;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;

import javax.ws.rs.core.UriBuilder;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

/**
 * @author Merav Yaakov
 */

public class XrayJiraRestClient implements Closeable {

    private final DisposableHttpClient httpClient;
    private XrayJiraTestExecutionRestClient xrayJiraTestExecutionRestClient;
    private XrayJiraTestRunRestClient xrayJiraTestRunRestClient;

    public XrayJiraRestClient (URI serverUri, DisposableHttpClient httpClient, JiraRestClient jiraRestClient) {
        URI baseUri = UriBuilder.fromUri(serverUri).path("/rest/raven/1.0").build();
        this.httpClient = httpClient;
        this.xrayJiraTestExecutionRestClient = new XrayJiraTestExecutionRestClient(baseUri, httpClient, jiraRestClient);
        this.xrayJiraTestRunRestClient = new XrayJiraTestRunRestClient(baseUri, httpClient, jiraRestClient);
    }

    public XrayJiraTestExecutionRestClient getXrayJiraTestExecutionRestClient() {
        return xrayJiraTestExecutionRestClient;
    }

    public XrayJiraTestRunRestClient getXrayJiraTestRunRestClient() {
        return xrayJiraTestRunRestClient;
    }

    @Override
    public void close() throws IOException {
        try {
            this.httpClient.destroy();
        } catch (Exception e) {
            throw e instanceof IOException ? (IOException)e : new IOException(e);
        }

    }
}

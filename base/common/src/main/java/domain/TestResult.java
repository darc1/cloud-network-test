package domain;

/**
 * Created by cdoar on 1/10/17.
 */
public class TestResult {

    private VMInstance client;
    private ServerData server;
    private String testResults;

    public TestResult() {
    }

    public VMInstance getClient() {
        return client;
    }

    public void setClient(VMInstance client) {
        this.client = client;
    }

    public ServerData getServer() {
        return server;
    }

    public void setServer(ServerData server) {
        this.server = server;
    }

    public String getTestResults() {
        return testResults;
    }

    public void setTestResults(String testResults) {
        this.testResults = testResults;
    }
}

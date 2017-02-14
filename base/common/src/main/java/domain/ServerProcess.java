package domain;

/**
 * Created by cdoar on 1/9/17.
 */
public class ServerProcess {

    private Process process;
    private int port;

    public ServerProcess(Process process, int port) {
        this.process = process;
        this.port = port;
    }

    public Process getProcess() {
        return process;
    }

    public int getPort() {
        return port;
    }
}

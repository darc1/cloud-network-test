package domain;

/**
 * Created by cdoar on 1/11/17.
 */
public class IcmpLatencySample {

    private boolean successFlag;
    private boolean timeoutFlag;
    private String errorMessage;
    private String targetIp;
    private int size;
    private int rtt;
    private int ttl;

    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public boolean isTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(boolean timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRtt() {
        return rtt;
    }

    public void setRtt(int rtt) {
        this.rtt = rtt;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    private long duration;


    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

}

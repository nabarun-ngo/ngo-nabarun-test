package ngo.nabarun.test.ngo_nabarun_test.models.api;


public class ApiResponse<D> {
    public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public D getResponsePayload() {
		return responsePayload;
	}
	public void setResponsePayload(D responsePayload) {
		this.responsePayload = responsePayload;
	}
	private String info;
    private long timestamp;
    private int status;
    private String version;
    private D responsePayload;

}
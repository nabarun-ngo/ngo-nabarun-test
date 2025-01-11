package ngo.nabarun.test.ngo_nabarun_test.models;

import lombok.Data;

@Data
public class ApiResponse<D> {
    private String info;
    private long timestamp;
    private int status;
    private String version;
    private D responsePayload;

}
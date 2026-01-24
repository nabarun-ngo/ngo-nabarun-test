package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("api_keys")
public class ApiKeyDBModel {
    private String id;
    private String name;
    private String apiKey;
    private String apiKeyId;
    private String permissions;
    private Integer rateLimit;
    private Date expiresAt;
    private Date lastUsedAt;
    private Date createdAt;
    private Date updatedAt;
}

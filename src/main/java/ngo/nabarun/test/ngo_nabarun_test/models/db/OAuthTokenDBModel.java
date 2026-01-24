package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("oauth_tokens")
public class OAuthTokenDBModel {
    private String id;
    private String provider;
    private String clientId;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Date expiresAt;
    private String scope;
    private Date createdAt;
    private Date updatedAt;
}

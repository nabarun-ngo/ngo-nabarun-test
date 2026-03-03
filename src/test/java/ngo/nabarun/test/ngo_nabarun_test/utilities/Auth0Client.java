package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.util.Date;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementApi;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.TokenRequest;

import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;

public class Auth0Client {
    private static ManagementApi managementAPI;
    private static TokenHolder tokenHolder;

    public static ManagementApi managementAPI() throws Auth0Exception {
        if (tokenHolder == null || (tokenHolder != null && new Date().after(tokenHolder.getExpiresAt()))) {
            AuthAPI authAPI = authAPI();
            String domain = Configs.AUTH0_DOMAIN;
            String audience = Configs.AUTH0_AUDIENCE;
            TokenRequest tokenRequest = authAPI.requestToken(audience);
            tokenHolder = tokenRequest.execute().getBody();
            managementAPI = ManagementApi.builder().domain(domain).token(tokenHolder.getAccessToken()).build();
        }
        return managementAPI;

    }

    public static AuthAPI authAPI() {
        String domain = Configs.AUTH0_DOMAIN;
        String clientId = Configs.AUTH0_CLIENT_ID;
        String clientSecret = Configs.AUTH0_CLIENT_SECRET;
        return AuthAPI.newBuilder(domain, clientId, clientSecret).build();
    }

}

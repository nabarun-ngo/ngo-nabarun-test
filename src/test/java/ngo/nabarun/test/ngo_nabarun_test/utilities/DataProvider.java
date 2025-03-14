package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ngo.nabarun.test.ngo_nabarun_test.config.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.CommonHelpers;
import ngo.nabarun.test.ngo_nabarun_test.models.ApiResponse;
import ngo.nabarun.test.ngo_nabarun_test.models.ApiPagination;
import ngo.nabarun.test.ngo_nabarun_test.models.User;

public class DataProvider {

	public List<User> getUsersByRole(String role) {
		String rootUrl=Configs.ROOT_URL;
		String apiKey=Configs.TEST_APIKEY;
		String requestUrl=rootUrl+"/api/user/list?roles="+role+"&userByRole=true";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet(requestUrl);
		request.addHeader("X-Api-Key", apiKey);
		request.addHeader("accept","application/json");
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity);
				System.out.println(result);
				ObjectMapper mapper = CommonHelpers.objectMapper;
				ApiResponse<ApiPagination<User>> apiResponse = mapper.readValue(result, new TypeReference<ApiResponse<ApiPagination<User>>>() {
				});

				//System.out.println(apiResponse);
				return apiResponse.getResponsePayload().getContent();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return List.of();
	}

}

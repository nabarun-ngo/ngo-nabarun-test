package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import ngo.nabarun.test.ngo_nabarun_test.config.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.CommonHelpers;
import ngo.nabarun.test.ngo_nabarun_test.models.ApiResponse;
import ngo.nabarun.test.ngo_nabarun_test.models.ApiPagination;
import ngo.nabarun.test.ngo_nabarun_test.models.User;

public class DataProvider {
	private static final Faker faker = new Faker();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
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
	


    public String replacePlaceholders(String placeholder) {
        if (!placeholder.startsWith("{") || !placeholder.endsWith("}")) {
            return placeholder; // Return the actual value if there's no placeholder
        }
        
        switch (placeholder) {
            case "{RandomName}":
                return faker.name().fullName();
            case "{RandomEmail}":
                return faker.internet().emailAddress();
            case "{SystemDate}":
                return dateFormat.format(new Date());
            default:
                Pattern datePattern = Pattern.compile("\\{SystemDate([+-]\\d+)\\}");
                Matcher dateMatcher = datePattern.matcher(placeholder);
                if (dateMatcher.matches()) {
                    int offset = Integer.parseInt(dateMatcher.group(1));
                    return getDateWithOffset(offset);
                }
                
                Pattern phonePattern = Pattern.compile("\\{RandomNumber:(\\d+)\\}");
                Matcher phoneMatcher = phonePattern.matcher(placeholder);
                if (phoneMatcher.matches()) {
                    int digits = Integer.parseInt(phoneMatcher.group(1));
                    return generateRandomPhoneNumber(digits);
                }
                
                return placeholder; // Return as is if no match
        }
    }

    private static String generateRandomPhoneNumber(int digits) {
        StringBuilder phoneNumber = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            phoneNumber.append(faker.number().randomDigit());
        }
        return phoneNumber.toString();
    }

    private static String getDateWithOffset(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(calendar.getTime());
    }


}

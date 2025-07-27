package ngo.nabarun.test.ngo_nabarun_test.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.client.MongoCollection;

import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.models.api.ApiPagination;
import ngo.nabarun.test.ngo_nabarun_test.models.api.ApiResponse;
import ngo.nabarun.test.ngo_nabarun_test.models.api.User;
import ngo.nabarun.test.ngo_nabarun_test.utils.APIUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DBUtils;

import static com.mongodb.client.model.Filters.*;


public class DataProvider {
	private static final Logger logger = LogManager.getLogger(DataProvider.class);
	

	public List<User> getUsersByRoleViaAPI(String role) {
		String rootUrl = Configs.ROOT_URL;
		String apiKey = Configs.TEST_APIKEY;
		String requestUrl = rootUrl + "/api/user/list?roles=" + role + "&userByRole=true";
		TypeReference<ApiResponse<ApiPagination<User>>> typeRef = new TypeReference<ApiResponse<ApiPagination<User>>>() {};
		ApiResponse<ApiPagination<User>> apiResponse = APIUtils.httpGet(requestUrl, Map.of("X-API-KEY", apiKey),typeRef);
		return apiResponse.getResponsePayload().getContent();
	}

	
	public Document findUserByName(String firstName, String lastName) {
		return DBUtils.executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("user_profiles");
			Document user = collection.find(and(eq("firstName", firstName), eq("lastName", lastName))).first();
			return user;
		});
	}

	public List<Document> findDonationsBetweenDates(Date startDate, Date endDate, String profileId,
			String type) {
		return DBUtils.executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("donations");
			List<Document> donations = new ArrayList<>();
			try {
				collection.find(and(gte("raisedOn", startDate), lte("raisedOn", endDate), eq("profile", profileId),
					eq("type", type))).into(donations);
			} catch (Exception e) {
				logger.error("Error finding donations between dates", e);
				throw new RuntimeException("Error finding donations between dates", e);
			}
			return donations;
		});
	}

	public boolean deleteDonationById(String donationId) {
		return DBUtils.executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("donations");
			try {
				collection.deleteOne(eq("_id", donationId));
				return true;
			} catch (Exception e) {
				logger.error("Error deleting donation by id", e);
				return false;
			}
		});
	}

	public Document findOTPDetails(String email) {
		return DBUtils.executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("tickets");
			Document otpDetails = collection.find(eq("email", email)).first();
			return otpDetails;
		});
	}
}
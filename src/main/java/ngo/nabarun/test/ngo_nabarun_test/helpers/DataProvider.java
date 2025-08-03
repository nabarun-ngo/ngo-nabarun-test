package ngo.nabarun.test.ngo_nabarun_test.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.models.api.ApiPagination;
import ngo.nabarun.test.ngo_nabarun_test.models.api.ApiResponse;
import ngo.nabarun.test.ngo_nabarun_test.models.api.User;
import ngo.nabarun.test.ngo_nabarun_test.models.db.DonationDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.TicketDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;
import ngo.nabarun.test.ngo_nabarun_test.utils.APIUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DBUtils;

import static dev.morphia.query.filters.Filters.*;
public class DataProvider {
	private static final Logger logger = LogManager.getLogger(DataProvider.class);

	public List<User> getUsersByRoleViaAPI(String role) {
		String rootUrl = Configs.ROOT_URL;
		String apiKey = Configs.TEST_APIKEY;
		String requestUrl = rootUrl + "/api/user/list?roles=" + role + "&userByRole=true";
		TypeReference<ApiResponse<ApiPagination<User>>> typeRef = new TypeReference<ApiResponse<ApiPagination<User>>>() {
		};
		ApiResponse<ApiPagination<User>> apiResponse = APIUtils.httpGet(requestUrl, Map.of("X-API-KEY", apiKey),
				typeRef);
		return apiResponse.getResponsePayload().getContent();
	}

	public UserDBModel findUserByName(String firstName, String lastName) {
		return DBUtils.executeMongoOperation(database -> {
			Query<UserDBModel> collection = database.find(UserDBModel.class);
			UserDBModel user = collection
					.filter(Filters.and(Filters.eq("firstName", firstName), Filters.eq("lastName", lastName))).first();
			return user;
		});
	}

	public List<DonationDBModel> findDonationsBetweenDates(Date startDate, Date endDate, String profileId,
			String type) {
		return DBUtils.executeMongoOperation(database -> {
			Query<DonationDBModel> collection = database.find(DonationDBModel.class);
			List<DonationDBModel> donations = new ArrayList<>();
			try {
				collection.filter(and(gte("raisedOn", startDate), lte("raisedOn", endDate), eq("profile", profileId),
						eq("type", type))).iterator().forEachRemaining(donations::add);
			} catch (Exception e) {
				logger.error("Error finding donations between dates", e);
				throw new RuntimeException("Error finding donations between dates", e);
			}
			return donations;
		});
	}

	public boolean deleteDonationById(String donationId) {
		return DBUtils.executeMongoOperation(database -> {
			Query<DonationDBModel> collection = database.find(DonationDBModel.class);
			try {
				return collection.filter(eq("id", donationId)).delete().getDeletedCount() > 0 ;
			} catch (Exception e) {
				logger.error("Error deleting donation by id", e);
				return false;
			}
		});
	}

	public TicketDBModel findOTPDetails(String email) {
		return DBUtils.executeMongoOperation(database -> {
			Query<TicketDBModel> collection = database.find(TicketDBModel.class);
			TicketDBModel otpDetails = collection.filter(eq("email", email)).first();
			return otpDetails;
		});
	}

}
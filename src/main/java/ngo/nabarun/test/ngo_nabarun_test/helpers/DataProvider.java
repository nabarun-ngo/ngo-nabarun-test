package ngo.nabarun.test.ngo_nabarun_test.helpers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.models.api.ApiPagination;
import ngo.nabarun.test.ngo_nabarun_test.models.api.ApiResponse;
import ngo.nabarun.test.ngo_nabarun_test.models.api.User;
import ngo.nabarun.test.ngo_nabarun_test.models.db.DonationDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.TicketDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;
import ngo.nabarun.test.ngo_nabarun_test.utils.APIUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DBFilter;
import ngo.nabarun.test.ngo_nabarun_test.utils.DBUtils;

public class DataProvider {
	private static final Logger logger = LogManager.getLogger(DataProvider.class);

	public List<User> getUsersByRoleViaAPI(String role) {
		String rootUrl = Configs.ROOT_URL;
		String apiKey = "";
		String requestUrl = rootUrl + "/api/user/list?roles=" + role + "&userByRole=true";
		TypeReference<ApiResponse<ApiPagination<User>>> typeRef = new TypeReference<ApiResponse<ApiPagination<User>>>() {
		};
		ApiResponse<ApiPagination<User>> apiResponse = APIUtils.httpGet(requestUrl, Map.of("X-API-KEY", apiKey),
				typeRef);
		return apiResponse.getResponsePayload().getContent();
	}

	public UserDBModel findUserByName(String firstName, String lastName) {
		try {
			return DBUtils.getClient().findFirst(UserDBModel.class, Arrays.asList(
					DBFilter.eq("firstName", firstName),
					DBFilter.eq("lastName", lastName)
			));
		} catch (Exception e) {
			logger.error("Error finding user by name", e);
			return null;
		}
	}

	public List<DonationDBModel> findDonationsBetweenDates(Date startDate, Date endDate, String profileId,
			String type) {
		try {
			return DBUtils.getClient().findMany(DonationDBModel.class, Arrays.asList(
					DBFilter.gte("raisedOn", startDate),
					DBFilter.lte("raisedOn", endDate),
					DBFilter.eq("donorId", profileId),
					DBFilter.eq("type", type)
			));
		} catch (Exception e) {
			logger.error("Error finding donations between dates", e);
			throw new RuntimeException("Error finding donations between dates", e);
		}
	}

	public boolean deleteDonationById(String donationId) {
		try {
			return DBUtils.getClient().delete(DonationDBModel.class, Arrays.asList(
					DBFilter.eq("id", donationId)
			));
		} catch (Exception e) {
			logger.error("Error deleting donation by id", e);
			return false;
		}
	}

	public TicketDBModel findOTPDetails(String email) {
		try {
			return DBUtils.getClient().findFirst(TicketDBModel.class, Arrays.asList(
					DBFilter.eq("email", email)
			));
		} catch (Exception e) {
			logger.error("Error finding OTP details", e);
			return null;
		}
	}

}
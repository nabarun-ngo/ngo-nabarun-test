package ngo.nabarun.test.ngo_nabarun_test.utilities;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import ngo.nabarun.test.ngo_nabarun_test.config.Configs;

public class DBUtility {
	private static final Logger logger = LogManager.getLogger(DBUtility.class);

	private static <T> T executeMongoOperation(Function<MongoDatabase, T> operation) {
		String connectionString = Configs.MONGODB_CONNECTION_STRING;

		ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
		ConnectionString connString = new ConnectionString(connectionString);
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(connString)
				.serverApi(serverApi)
				.build();

		try (MongoClient mongoClient = MongoClients.create(settings)) {
			MongoDatabase database = mongoClient.getDatabase(connString.getDatabase());
			return operation.apply(database);
		} catch (Exception e) {
			logger.error("Error executing MongoDB operation", e);
			throw new RuntimeException("Error executing MongoDB operation", e);
		}
	}

	public static Document findUserByName(String firstName, String lastName) {
		return executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("user_profiles");
			Document user = collection.find(and(eq("firstName", firstName), eq("lastName", lastName))).first();
			return user;
		});
	}

	public static List<Document> findDonationsBetweenDates(Date startDate, Date endDate, String profileId,
			String type) {
		return executeMongoOperation(database -> {
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

	public static boolean deleteDonationById(String donationId) {
		return executeMongoOperation(database -> {
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

	public static Document findOTPDetails(String email) {
		return executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("tickets");
			Document otpDetails = collection.find(eq("email", email)).first();
			return otpDetails;
		});
	}
}
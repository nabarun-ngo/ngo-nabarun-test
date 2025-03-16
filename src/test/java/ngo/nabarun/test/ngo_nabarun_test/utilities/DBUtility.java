package ngo.nabarun.test.ngo_nabarun_test.utilities;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import ngo.nabarun.test.ngo_nabarun_test.config.Configs;

public class DBUtility {
	private static final Logger logger = LogManager.getLogger(DevToolsUtility.class);

	private static <T> T executeMongoOperation(Function<MongoDatabase, T> operation) {
		String mongodb_uri = Configs.MONGODB_CONNECTION_STRING;
		ConnectionString connectionString = new ConnectionString(mongodb_uri);
		try (MongoClient mongoClient = MongoClients.create(connectionString)) { // Auto-close connection
			MongoDatabase database = mongoClient.getDatabase(connectionString.getDatabase());
			return operation.apply(database); // Execute and return result
		} catch (Exception e) {
			logger.error("failed to establish database connection", e);
			return null;
		}
	}

	public static Document findUserByName(String firstName, String lastName) {
		return executeMongoOperation(database -> {
			Document filter = new Document("firstName",
					new Document("$regex", "^" + firstName + "$").append("$options", "i"))
					.append("lastName", new Document("$regex", "^" + lastName + "$").append("$options", "i"));
			MongoCollection<Document> collection = database.getCollection("user_profiles");
			return collection.find(filter).first();
		});
	}

	public static List<Document> findDonationsBetweenDates(Date startDate, Date endDate, String profileId,String type) {
		return executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("donations");
			Bson dateFilter = and(gte("raisedOn", startDate), lte("raisedOn", endDate), eq("profile", profileId),eq("type", type));
			List<Document> donations = new ArrayList<>();
			collection.find(dateFilter).into(donations);
			return donations;
		});
	}

	public static boolean deleteDonationById(String donationId) {
		return executeMongoOperation(database -> {
			MongoCollection<Document> collection = database.getCollection("donations");
			long deletedCount = collection.deleteOne(eq("_id", donationId)).getDeletedCount();
			return deletedCount > 0;
		});
	}
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public static void main(String[] args) throws ParseException {
		String firstName ="Member";
		String lastName = "TestUser";
		Document user =DBUtility.findUserByName(firstName, lastName);
		Date startDate = dateFormat.parse(DataProvider.firstDayOfCurrentMonth());
		Date endDate = dateFormat.parse(DataProvider.lastDayOfCurrentMonth());
		String id = user.getString("_id");
		System.out.println(id);
		System.out.println();
		List<Document> donations=DBUtility.findDonationsBetweenDates(startDate,endDate,id,"REGULAR");
		for(Document donation:donations) {
			System.out.println(donation.getString("_id"));
			DBUtility.deleteDonationById(donation.getString("_id"));
		}
	}
}

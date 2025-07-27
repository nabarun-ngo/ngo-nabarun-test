package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;



public class DBUtils {
	private static final Logger logger = LogManager.getLogger(DBUtils.class);

	public static <T> T executeMongoOperation(Function<MongoDatabase, T> operation) {
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
	
}
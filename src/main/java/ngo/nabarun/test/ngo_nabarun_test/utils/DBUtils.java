package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import static org.bson.codecs.configuration.CodecRegistries.*;

public class DBUtils {
	private static final Logger logger = LogManager.getLogger(DBUtils.class);

	@Deprecated
	public static <T> T executeMongoOperation_Legacy(Function<MongoDatabase, T> operation) {
		String connectionString = Configs.MONGODB_CONNECTION_STRING;
		ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
		ConnectionString connString = new ConnectionString(connectionString);
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(pojoCodecProvider));
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
				.codecRegistry(codecRegistry).serverApi(serverApi).build();
		try (MongoClient mongoClient = MongoClients.create(settings)) {
			MongoDatabase database = mongoClient.getDatabase(connString.getDatabase());
			return operation.apply(database);
		} catch (Exception e) {
			logger.error("Error executing MongoDB operation", e);
			throw new RuntimeException("Error executing MongoDB operation", e);
		}
	}

	public static <T> T executeMongoOperation(Function<Datastore, T> operation) {
		String connectionString = Configs.MONGODB_CONNECTION_STRING;
		ConnectionString connString = new ConnectionString(connectionString);

		try (MongoClient mongoClient = MongoClients.create(connectionString)) {
			final Datastore datastore = Morphia.createDatastore(mongoClient, connString.getDatabase());
			return operation.apply(datastore);
		} catch (Exception e) {
			logger.error("Error executing MongoDB operation", e);
			throw new RuntimeException("Error executing MongoDB operation", e);
		}
	}

}
package ngo.nabarun.test.ngo_nabarun_test.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBUtils {
	private static final Logger logger = LogManager.getLogger(DBUtils.class);
	private static IDatabaseClient databaseClient;

	static {
		try {
			// Initialize with Postgres client
			databaseClient = new PostgresDatabaseClient();
		} catch (Exception e) {
			logger.error("Failed to initialize database client", e);
		}
	}

	public static IDatabaseClient getClient() {
		if (databaseClient == null) {
			throw new RuntimeException("Database client not initialized");
		}
		return databaseClient;
	}

}
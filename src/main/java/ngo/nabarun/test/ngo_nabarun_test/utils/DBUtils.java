package ngo.nabarun.test.ngo_nabarun_test.utils;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBUtils {
	private static final Logger logger = LogManager.getLogger(DBUtils.class);
	private static Jdbi jdbi;
	private static HikariDataSource dataSource;

	static {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(Configs.DB_URL);
		config.setDriverClassName("org.postgresql.Driver");

		// HikariCP pool settings to ensure connections are managed efficiently
		config.setMaximumPoolSize(3); // Stay well within the 5 concurrent connection limit
		config.setMinimumIdle(0); // Do not keep any idle connections; close all when not in use
		config.setIdleTimeout(30000); // 30 seconds
		config.setConnectionTimeout(30000); // 30 seconds
		config.setLeakDetectionThreshold(2000); // 2 seconds log warning if a connection is not closed

		logger.info("Initializing HikariCP with URL: {}", Configs.DB_URL);
		logger.info("Database Pool Settings: MaxPoolSize={}, MinIdle={}, ConnectionTimeout={}ms", 
				config.getMaximumPoolSize(), config.getMinimumIdle(), config.getConnectionTimeout());

		dataSource = new HikariDataSource(config);

		jdbi = Jdbi.create(dataSource)
				.installPlugin(new SqlObjectPlugin())
				.installPlugin(new PostgresPlugin());
		
		logger.info("JDBI instance created and plugins installed.");

		// Adding a shutdown hook to close the datasource when the JVM exits
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (dataSource != null && !dataSource.isClosed()) {
				dataSource.close();
				logger.info("Database connection pool closed via shutdown hook.");
			}
		}));
	}

	public static Jdbi getJdbi() {
		return jdbi;
	}

	public static void close() {
		if (dataSource != null && !dataSource.isClosed()) {
			dataSource.close();
			logger.info("Database connection pool closed manually.");
		}
	}

}
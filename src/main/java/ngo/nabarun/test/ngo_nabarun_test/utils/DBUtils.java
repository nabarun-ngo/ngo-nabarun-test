package ngo.nabarun.test.ngo_nabarun_test.utils;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;

public class DBUtils {
	private static Jdbi jdbi;

	static {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(Configs.DB_URL);
		config.setDriverClassName("org.postgresql.Driver");
		HikariDataSource dataSource = new HikariDataSource(config);

		jdbi = Jdbi.create(dataSource)
				.installPlugin(new SqlObjectPlugin())
				.installPlugin(new PostgresPlugin());
	}

	public static Jdbi getJdbi() {
		return jdbi;
	}

}
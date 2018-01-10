package org.briarproject.bramble.db;

import org.briarproject.bramble.api.db.DatabaseConfig;
import org.briarproject.bramble.api.system.Clock;

public class H2DatabaseTest extends JdbcDatabaseTest {

	public H2DatabaseTest() throws Exception {
		super();
	}

	@Override
	protected JdbcDatabase createDatabase(DatabaseConfig config, Clock clock) {
		return new H2Database(config, clock);
	}
}

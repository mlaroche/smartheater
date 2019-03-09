/**
 * Copyright 2018 Matthieu Laroche - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package com.mlaroche.smartheater.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.inject.Inject;

import io.vertigo.core.component.ComponentInitializer;
import io.vertigo.core.resource.ResourceManager;
import io.vertigo.database.sql.SqlDataBaseManager;
import io.vertigo.database.sql.connection.SqlConnection;
import io.vertigo.database.sql.statement.SqlStatement;
import io.vertigo.lang.WrappedException;

/**
 * Init masterdata list.
 * @author mlaroche
 */
public class DataBaseInitializer implements ComponentInitializer {

	@Inject
	private ResourceManager resourceManager;
	@Inject
	private SqlDataBaseManager sqlDataBaseManager;

	/** {@inheritDoc} */
	@Override
	public void init() {
		createDataBase();
		addData();
	}

	private void addData() {
		// TODO Auto-generated method stub

	}

	private void createDataBase() {
		final SqlConnection connection = sqlDataBaseManager.getConnectionProvider(SqlDataBaseManager.MAIN_CONNECTION_PROVIDER_NAME).obtainConnection();
		execCallableStatement(connection, sqlDataBaseManager, "drop all objects;");
		execSqlScript(connection, "sqlgen/crebas.sql");
		execSqlScript(connection, "sqlgen/init_masterdata_heater_mode.sql");
		execSqlScript(connection, "sqlgen/init_masterdata_protocol.sql");
		execSqlScript(connection, "sql/init_data.sql");
		try {
			connection.commit();
			connection.release();
		} catch (final SQLException e) {
			throw WrappedException.wrap(e, "Can't release connection");
		}
	}

	private void execSqlScript(final SqlConnection connection, final String scriptPath) {
		final StringBuilder crebaseSql = new StringBuilder();
		try (final BufferedReader in = new BufferedReader(new InputStreamReader(resourceManager.resolve(scriptPath).openStream()))) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				final String adaptedInputLine = inputLine.replaceAll("-- .*", "");//removed comments
				if (!"".equals(adaptedInputLine)) {
					crebaseSql.append(adaptedInputLine).append('\n');
				}
				if (inputLine.trim().endsWith(";")) {
					execCallableStatement(connection, sqlDataBaseManager, crebaseSql.toString());
					crebaseSql.setLength(0);
				}
			}
		} catch (final IOException e) {
			throw WrappedException.wrap(e, "Can't exec script {0}", scriptPath);
		}
	}

	private static void execCallableStatement(final SqlConnection connection, final SqlDataBaseManager sqlDataBaseManager, final String sql) {
		try {
			sqlDataBaseManager.executeUpdate(SqlStatement.builder(sql).build(), connection);
		} catch (final SQLException e) {
			throw WrappedException.wrap(e, "Can't exec command {0}", sql);
		}
	}

}

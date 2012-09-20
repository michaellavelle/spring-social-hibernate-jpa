/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.connect.jpa.hibernate;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * @author Michael Lavelle
 */
@Configuration
public class DataSourceTestConfig {

	private boolean testMySqlCompatiblity = true;

	@Bean
	public ResourceDatabasePopulator resourceDatabasePopulator() {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource(
				getSchemaSql(), getClass()));
		return resourceDatabasePopulator;
	}

	@Bean
	EmbeddedDatabase dataSource() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		if (testMySqlCompatiblity) {
			factory.setDatabaseConfigurer(new MySqlCompatibleH2DatabaseConfigurer());
		} else {
			factory.setDatabaseType(EmbeddedDatabaseType.H2);
		}

		factory.setDatabasePopulator(resourceDatabasePopulator());
		return factory.getDatabase();
	}

	protected String getSchemaSql() {
		return "HibernateHQLDBUsersConnectionRepository.sql";
	}

	public static class MySqlCompatibleH2DatabaseConfigurer implements
			EmbeddedDatabaseConfigurer {

		public void shutdown(DataSource dataSource, String databaseName) {
			try {
				java.sql.Connection connection = dataSource.getConnection();
				Statement stmt = connection.createStatement();
				stmt.execute("SHUTDOWN");
			} catch (SQLException ex) {
			}
		}

		public void configureConnectionProperties(
				ConnectionProperties properties, String databaseName) {
			properties.setDriverClass(Driver.class);
			properties.setUrl(String
					.format("jdbc:h2:mem:%s;MODE=MYSQL;DB_CLOSE_DELAY=-1",
							databaseName));
			properties.setUsername("sa");
			properties.setPassword("");
		}

	}

}

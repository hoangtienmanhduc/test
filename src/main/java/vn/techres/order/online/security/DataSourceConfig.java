package vn.techres.order.online.security;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vn.techres.order.online.configuration.ApplicationProperties;
import vn.techres.order.online.configuration.SocketClientSingleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

@Configuration
@ComponentScan()
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DataSourceConfig {

	@Autowired
	private ApplicationProperties config;

	@Autowired
	private Environment environment;

	@Autowired
	private SocketClientSingleton socketClientSingleton;

	@Bean
	DataSource dataSource() {
		HikariConfig hikariConfig = this.initHikariPoolingConfig("hikari-pool");
		hikariConfig.setJdbcUrl(this.config.getDatasourceUrl());
		hikariConfig.setUsername(this.config.getDatasourceUsername());
		hikariConfig.setPassword(this.config.getDatasourcePassword());
		this.socketClientSingleton.setRealtimeUrl(config.getSocketIoRealtimeOrderUrl());

		System.out.println("Kết nối reamtime thành công");
		return new HikariDataSource(hikariConfig);
	}

	@Bean(name = { "sessionFactory", "entityManagerFactory" })
	LocalSessionFactoryBean entityManagerFactory(@Autowired final DataSource dataSource) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("vn.techres.order");
		sessionFactory.setHibernateProperties(this.hibernateProperties());
		return sessionFactory;
	}

	@Bean
	PlatformTransactionManager transactionManager(LocalSessionFactoryBean factoryBean) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(factoryBean.getObject());
		return transactionManager;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put(AvailableSettings.DIALECT, environment.getRequiredProperty("hibernate.dialect"));
		properties.put(AvailableSettings.SHOW_SQL, environment.getRequiredProperty("hibernate.show_sql"));
		properties.put(AvailableSettings.FORMAT_SQL, environment.getRequiredProperty("hibernate.format_sql"));
		return properties;
	}

	private HikariConfig initHikariPoolingConfig(String poolName) {
		HikariConfig hikariConfig = new HikariConfig();

		hikariConfig.setPoolName(poolName);

		hikariConfig.setDriverClassName(config.getDriverClassname());
		hikariConfig.setConnectionTimeout(config.getHikariCP_ConnectionTimeout());
		hikariConfig.setIdleTimeout(config.getHikariCP_IdleTimeout());
		hikariConfig.setMaxLifetime(config.getHikariCP_MaxLifetime());
		hikariConfig.setTransactionIsolation(String.valueOf(Connection.TRANSACTION_READ_COMMITTED));

		hikariConfig.addDataSourceProperty("cachePrepStmts", config.getHikariCP_CachePrepStmts());
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", config.getHikariCP_PrepStmtCacheSize());
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", config.getHikariCP_PrepStmtCacheSqlLimit());

		return hikariConfig;
	}
}

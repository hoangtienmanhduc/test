package vn.techres.order.online.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationProperties {

	@Value("${project.service.port}")
	private int projectServicePort;

	@Value("${project.service.grpc.port}")
	private int projectServiceGrpcPort;

	@Value("${socket.io.realtime.order.url}")
	private String socketIoRealtimeOrderUrl;

	@Value("${grpc.client.java_order_lv3.address}")
	private String pepcUrl;

	@Value("${spring.datasource.driver.classname}")
	private String driverClassname;

	@Value("${spring.datasource.url}")
	private String datasourceUrl;

	@Value("${spring.datasource.username}")
	private String datasourceUsername;

	@Value("${spring.datasource.password}")
	private String datasourcePassword;

	@Value("${hikaricp.connectionTimeout}")
	private long hikariCP_ConnectionTimeout;

	@Value("${hikaricp.idleTimeout}")
	private long hikariCP_IdleTimeout;

	@Value("${hikaricp.maxLifetime}")
	private long hikariCP_MaxLifetime;

	@Value("${hikaricp.cachePrepStmts}")
	private String hikariCP_CachePrepStmts;

	@Value("${hikaricp.cacheResultSetMetadata}")
	private String hikariCP_CacheResultSetMetadata;

	@Value("${hikaricp.cacheServerConfiguration}")
	private String hikariCP_CacheServerConfiguration;

	@Value("${hikaricp.prepStmtCacheSize}")
	private int hikariCP_PrepStmtCacheSize;

	@Value("${hikaricp.prepStmtCacheSqlLimit}")
	private int hikariCP_PrepStmtCacheSqlLimit;

	@Value("${hikaricp.useServerPrepStmts}")
	private String hikariCP_UseServerPrepStmts;

	@Value("${hikaricp.useLocalSessionState}")
	private String hikariCP_UseLocalSessionState;

	@Value("${hikaricp.rewriteBatchedStatements}")
	private String hikariCP_RewriteBatchedStatements;

	@Value("${hikaricp.elideSetAutoCommit}")
	private String hikariCP_ElideSetAutoCommit;

	@Value("${hikaricp.maintainTimeStats}")
	private String hikariCP_MaintainTimeStats;

	@Value("${config.build_number}")
	private String configBuildNumber;

	@Value("${config.build_time}")
	private String configBuildTime;


}
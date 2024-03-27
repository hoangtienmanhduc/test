package vn.techres.order.online.configuration;

import net.devh.boot.grpc.server.config.GrpcServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		factory.setPort(applicationProperties.getProjectServicePort());
	}

	@Bean
	public GrpcServerProperties grpcServerProperties() {
		GrpcServerProperties properties = new GrpcServerProperties();
		properties.setPort(applicationProperties.getProjectServiceGrpcPort());
		System.out.println("--------------- GRPC PORT ------------------------:  "+ applicationProperties.getProjectServiceGrpcPort());
		System.out.println("--------------- GRPC URL ------------------------:  "+ applicationProperties.getPepcUrl());
		return properties;
	}

}

package vn.techres.order.online.configuration;

import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import net.devh.boot.grpc.client.inject.StubTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcClientConfiguration {

	@Bean
	GrpcChannelConfigurer keepAliveClientConfigurer() {
		return (channelBuilder, name) -> {
			if (channelBuilder instanceof NettyChannelBuilder) {
				((NettyChannelBuilder) channelBuilder)
						.usePlaintext()
						.keepAliveTime(30, TimeUnit.SECONDS)
						.keepAliveWithoutCalls(true)
						.enableRetry()
						.intercept(new GrpcRetry(3))
						.maxRetryAttempts(5)
						.keepAliveTimeout(5, TimeUnit.SECONDS);
			}
		};
	}

	@Bean
	StubTransformer call() {
		return (name, stub) -> {
			return stub;
		};
	}
}
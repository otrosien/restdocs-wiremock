package com.epages.wiremock.starter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

@Configuration
@ConditionalOnClass({WireMockServer.class})
@ConditionalOnProperty(name="wiremock.enabled", havingValue="true")
public class WireMockAutoConfiguration {

	private static final Log log = LogFactory.getLog(WireMockAutoConfiguration.class);

	@Value("${wiremock.port}")
	int wiremockPort = 0;

	@Bean
	@ConditionalOnMissingBean
	public WireMockConfiguration wireMockConfiguration() {
		WireMockConfiguration config = WireMockConfiguration.options();
		if(wiremockPort > 0) {
			log.info("Starting WireMock on port " + wiremockPort);
			config.port(wiremockPort);
		} else {
			log.info("Starting WireMock on dynamic port");
			config.dynamicPort();
		}
		return config;
	}

	@Bean
	public WireMockServer wireMockServer(WireMockConfiguration config) {
		return new WireMockServer(config);
	}

}

/*
 * Copyright 2014-2020 the original author or authors.
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

package de.codecentric.boot.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.LoggingNotifier;

@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
@EnableAdminServer
public class SpringBootAdminReactiveApplication {

	private final AdminServerProperties adminServer;

	public SpringBootAdminReactiveApplication(AdminServerProperties adminServer) {
		this.adminServer = adminServer;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdminReactiveApplication.class, args);
	}

	@Bean
	@Profile("insecure")
	public SecurityWebFilterChain securityWebFilterChainPermitAll(ServerHttpSecurity http) {
		return http.authorizeExchange((authorizeExchange) -> authorizeExchange.anyExchange().permitAll())
				.csrf(ServerHttpSecurity.CsrfSpec::disable).build();
	}

	@Bean
	@Profile("secure")
	public SecurityWebFilterChain securityWebFilterChainSecure(ServerHttpSecurity http) {
		return http
				.authorizeExchange((authorizeExchange) -> authorizeExchange
						.pathMatchers(this.adminServer.path("/assets/**")).permitAll()
						.pathMatchers("/actuator/health/**").permitAll().pathMatchers(this.adminServer.path("/login"))
						.permitAll().anyExchange().authenticated())
				.formLogin((formLogin) -> formLogin.loginPage(this.adminServer.path("/login")))
				.logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout")))
				.httpBasic(Customizer.withDefaults()).csrf(ServerHttpSecurity.CsrfSpec::disable).build();
	}

	@Bean
	public LoggingNotifier loggerNotifier(InstanceRepository repository) {
		return new LoggingNotifier(repository);
	}

}

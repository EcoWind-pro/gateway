package ecowind.ru.gateway.configs

import ecowind.ru.gateway.configs.properties.RestServiceProps
import ecowind.ru.gateway.filters.AuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val restServiceProps: RestServiceProps
) {
    /**
     * Override bean of security filter chain where acceptable URLs are being determined.
     */
    @Bean
    fun securityFilterChain(
        http: ServerHttpSecurity,
        authorizationFilter: AuthorizationFilter,
    ): SecurityWebFilterChain = http
        .csrf { it.disable() }
        .cors { it.disable() }
        .authorizeExchange {
            it
                .pathMatchers("${restServiceProps.msAuth.path}/**")
                .permitAll()
                .pathMatchers("${restServiceProps.msClient.path}/**")
                .permitAll()
                .anyExchange()
                .denyAll()
        }
        .addFilterAt(authorizationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build()
}
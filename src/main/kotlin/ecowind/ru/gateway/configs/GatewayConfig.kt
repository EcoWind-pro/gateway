package ecowind.ru.gateway.configs

import ecowind.ru.gateway.models.MsData
import ecowind.ru.gateway.configs.properties.RestServiceProps
import ecowind.ru.utils.ActionUtils.generateUri
import java.util.function.Function
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfig(private val restServiceProps: RestServiceProps) {

    /**
     * Method to build all routes by configuration properties.
     */
    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
        .route(restServiceProps.msAuth.name, buildRoute(restServiceProps.msAuth))
        .route(restServiceProps.msClient.name, buildRoute(restServiceProps.msClient))
        .build()

    /**
     * Additional function to create a route rewrite.
     *
     * @param data data from configuration properties about certain microservice
     */
    private fun buildRoute(data: MsData) = Function<PredicateSpec, Buildable<Route>> {
        it.path("${data.path}/**")
            .filters { f -> f.rewritePath("${data.path}/(?<path>.*)", "${data.rewritePath}/$\\{path}") }
            .uri(
                generateUri(
                    scheme = data.scheme,
                    host = data.host,
                    port = data.port
                )
            )
    }
}
package ecowind.ru.gateway.filters

import ecowind.ru.ClientAPI
import ecowind.ru.authapi.TokenAPI
import ecowind.ru.exceptionhandler.excepions.ActionException
import ecowind.ru.gateway.actions.AuthorizationAction
import ecowind.ru.gateway.configs.properties.RestServiceProps
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class AuthorizationFilter(
    private val authorizationAction: AuthorizationAction,
    restServiceProps: RestServiceProps
) : WebFilter {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)
    private val excludedApis: List<String> = listOf(
        restServiceProps.msAuth.path.plus(TokenAPI.PREFIX).plus(TokenAPI.CREATE),
        restServiceProps.msClient.path.plus(ClientAPI.PREFIX).plus(ClientAPI.REGISTER)
    )

    /**
     * Override filter method that wil be executed on every request. In filter requesting to another microservice to validate token of user.
     */
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> = runBlocking {
        val request: ServerHttpRequest = exchange.request

        if (request.path.value() !in excludedApis) {
            try {
                authorizationAction.checkAuthorization(request.headers.getOrEmpty(HttpHeaders.AUTHORIZATION)[0])
            } catch (ex: ActionException) {
                log.error(ex.message)
                return@runBlocking handleError(exchange, ex)
            }
        }
        return@runBlocking chain.filter(exchange)
    }

    /**
     * Custom exceptionHandler that allow to customize WebFlux errors and get normal view of exceptions.
     */
    private fun handleError(exchange: ServerWebExchange, ex: ActionException): Mono<Void> {
        val response = exchange.response

        response.statusCode = ex.status
        response.headers.contentType = MediaType.APPLICATION_JSON

        return response.writeWith(
            Mono.just(
                response.bufferFactory().wrap(Json.encodeToString(ex.errorDetails).toByteArray())
            )
        )
    }
}

package ecowind.ru.gateway.actions

import ecowind.ru.authapi.TokenAPI
import ecowind.ru.exceptionhandler.WebClientExceptionsHandler.exchangeCatchingErrors
import ecowind.ru.gateway.configs.properties.RestServiceProps
import ecowind.ru.gateway.models.MsData
import ecowind.ru.utils.ActionUtils.generateUri
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient

@Component
class AuthorizationAction(
    private val webClient: WebClient,
    private val restServiceProps: RestServiceProps
) {
    /**
     * Request to another microservice to check if token of user is correct
     *
     * @param token token that need to be validated
     */
    suspend fun checkAuthorization(token: String): Any {
        val msAuthData: MsData = restServiceProps.msAuth
        return webClient
            .post()
            .uri(
                generateUri(
                    scheme = msAuthData.scheme,
                    host = msAuthData.host,
                    port = msAuthData.port,
                    url = msAuthData.rewritePath.plus(TokenAPI.PREFIX).plus(TokenAPI.VALIDATE)
                )
            )
            .header(HttpHeaders.AUTHORIZATION, token)
            .exchangeToMono { exchangeCatchingErrors<ClientResponse>(it) }
            .awaitSingle()
    }
}
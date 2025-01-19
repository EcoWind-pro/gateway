package ecowind.ru.gateway.models

import java.time.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class GatewayErrorDetails(
    val timestamp: String = LocalDateTime.now().toString(),
    val message: String,
    val shortMessage: String,
)

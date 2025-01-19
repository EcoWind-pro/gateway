package ecowind.ru.gateway.models

data class MsData(
    val name: String,
    val path: String,
    val rewritePath: String,
    val scheme: String,
    val host: String,
    val port: Number
)

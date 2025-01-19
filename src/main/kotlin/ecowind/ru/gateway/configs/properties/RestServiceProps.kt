package ecowind.ru.gateway.configs.properties

import ecowind.ru.gateway.models.MsData
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rest")
data class RestServiceProps (
    val msAuth: MsData,
    val msClient: MsData,
)
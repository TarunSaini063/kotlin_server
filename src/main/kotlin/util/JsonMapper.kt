package util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object JsonMapper {

    @OptIn(ExperimentalSerializationApi::class)
    val defaultMapper = Json {
        prettyPrint = true
        explicitNulls = false
    }

}
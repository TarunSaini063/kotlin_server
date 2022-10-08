package user.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserModel(
    val name: String,
    val email: String,
    val password: String,
    val avatar: String,
)
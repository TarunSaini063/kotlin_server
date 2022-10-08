package user.repository

import base.BaseResponse
import io.ktor.http.*
import security.JwtConfig
import user.model.CreateUserModel
import user.model.User
import user.service.UserService

class UserRepositoryImpl(private val userService: UserService) : UserRepository {
    override suspend fun registerUser(params: CreateUserModel): BaseResponse<User> {
        return if (isEmailExit(params.email)) {
            BaseResponse.Error(message = "Email already exist", statusCode = HttpStatusCode.BadRequest)
        } else {
            val user = userService.registerUser(params)
            if (user == null) {
                BaseResponse.Error(message = "Email already exist", statusCode = HttpStatusCode.BadRequest)
            } else {
                val token = JwtConfig.instance.createAccessToken(user.id)
                user.authToken = token
                BaseResponse.Success(user)
            }
        }
    }

    override suspend fun findUserByEmail(email: String): BaseResponse<User?> {
        val response = userService.findUserByEmail(email)
        return if (response != null) BaseResponse.Success(response)
        else BaseResponse.Error(message = "User not found")
    }

    override suspend fun getAllUsers(): BaseResponse<List<User>> {
        return BaseResponse.Success(userService.getAllUsers())
    }

    private suspend fun isEmailExit(email: String): Boolean {
        return userService.findUserByEmail(email) != null
    }
}
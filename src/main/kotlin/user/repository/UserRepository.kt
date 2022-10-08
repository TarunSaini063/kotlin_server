package user.repository

import base.BaseResponse
import user.model.CreateUserModel
import user.model.User

interface UserRepository {
    suspend fun registerUser(params: CreateUserModel): BaseResponse<User>
    suspend fun findUserByEmail(email: String): BaseResponse<User?>
    suspend fun getAllUsers(): BaseResponse<List<User>>
}
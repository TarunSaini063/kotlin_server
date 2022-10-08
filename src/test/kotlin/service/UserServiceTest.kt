package service

import base.BaseResponse
import common.ServerTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import user.model.CreateUserModel
import user.model.User
import user.repository.UserRepositoryImpl
import user.service.UserService

class UserServiceTest : ServerTest() {
    private val service = UserService()
    private val repository = UserRepositoryImpl(service)

    @Test
    fun registerUser() {
        runBlocking {
            val createUserModel = CreateUserModel(
                name = "test1",
                email = "test@email.com",
                password = "123",
                avatar = "testAvatar",
            )
            val res: BaseResponse<User?> = repository.registerUser(createUserModel)
            println(res)
//            assertThat(res is BaseResponse.Success)
//            assertThat(createUserModel.name).isEqualTo((res as BaseResponse.Success).data?.name)
//            assertThat(createUserModel.avatar).isEqualTo((res).data?.avatar)
        }
    }

}
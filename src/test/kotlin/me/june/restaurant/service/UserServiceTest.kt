package me.june.restaurant.service

import me.june.restaurant.dto.UserDto
import me.june.restaurant.entity.User
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class UserServiceTest(
        private val userService: UserService,
        private val userRepository: UserRepository
) {

    @Test
    @DisplayName("유저 조회")
    fun `존재하는 유저 조회시 성공한다`() {
        // given
        val user = User(password = Password("asdf"),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )
        val savedUser = userRepository.save(user)

        // when
        val findUser = userService.findUser(savedUser.id)

        // then
        assertThat(findUser.id).isEqualTo(savedUser.id)
        assertThat(findUser.username).isEqualTo(savedUser.username)
        assertThat(findUser.email).isEqualTo(savedUser.email)
        assertThat(findUser.birth).isEqualTo(savedUser.birth)
        assertThat(findUser.gender).isEqualTo(savedUser.gender)
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회")
    fun `존재하지 않는 유저 조회시 실패한다`() {
        // when
        val id = -1L
        val ex = Assertions.assertThrows(UserNotFoundException::class.java) { userService.findUser(id) }

        // then
        assertThat(ex.message).isEqualTo("$id 에 해당하는 유저를 찾을 수 없습니다.")
    }

    @Test
    @DisplayName("유저 생성 성공")
    fun `유저 생성에 성공한다`() {
        // given
        val request = UserDto.CreateRequest(password = Password("asdf"),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )

        // when
        val savedUserId = userService.createdUser(request)

        // then
        val ( password, username, email, birth, gender ) = request
        val findUser = userRepository.findById(savedUserId).orElse(null)

        assertThat(findUser).isNotNull
        assertThat(findUser.password).isEqualTo(password)
        assertThat(findUser.username).isEqualTo(username)
        assertThat(findUser.email).isEqualTo(email)
        assertThat(findUser.birth).isEqualTo(birth)
        assertThat(findUser.gender).isEqualTo(gender)
    }

    @Test
    @DisplayName("유저 정보 수정")
    fun `유저 정보 수정에 성공한다`() {
        // given
        val user = User(password = Password("asdf"),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )
        val savedUser = userRepository.save(user)

        val request = UserDto.UpdateRequest(
                password = Password("asdf1234"),
                username = "ncucudas",
                email = "ncucudas.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )

        // when
        userService.updateUser(savedUser.id, request)

        // then
        val ( password, username, email, birth, gender ) = request
        val findUser = userRepository.findById(savedUser.id).orElse(null)

        assertThat(findUser).isNotNull
        assertThat(findUser.password).isEqualTo(password)
        assertThat(findUser.username).isEqualTo(username)
        assertThat(findUser.email).isEqualTo(email)
        assertThat(findUser.birth).isEqualTo(birth)
        assertThat(findUser.gender).isEqualTo(gender)
    }

    @Test
    @DisplayName("존재하지 않는 유저 수정")
    fun `존재하지 않는 유저 수정시 실패한다`() {
        // given
        val request = UserDto.UpdateRequest(
                password = Password("asdf1234"),
                username = "ncucudas",
                email = "ncucudas.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )

        // when
        val id = -1L
        val ex = Assertions.assertThrows(UserNotFoundException::class.java) { userService.updateUser(id, request) }
        assertThat(ex.message).isEqualTo("$id 에 해당하는 유저를 찾을 수 없습니다.")
    }

    @Test
    @DisplayName("유저 삭제")
    fun `유저 삭제에 성공한다`() {
        // given
        val user = User(password = Password("asdf"),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )
        val savedUser = userRepository.save(user)

        // when
        userService.deleteUser(savedUser.id)

        // then
        val findUser = userRepository.findById(savedUser.id).orElse(null)
        assertThat(findUser).isNull()
    }

    @Test
    @DisplayName("유저 삭제 실패")
    fun `존재하지 않는 유저 삭제시 실패한다`() {
        // when
        val id = -1L
        val ex = Assertions.assertThrows(UserNotFoundException::class.java) { userService.deleteUser(id) }

        // then
        assertThat(ex.message).isEqualTo("$id 에 해당하는 유저를 찾을 수 없습니다.")
    }
}
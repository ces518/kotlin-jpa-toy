package me.june.restaurant.service

import me.june.restaurant.config.EmbeddedRedisConfig
import me.june.restaurant.config.MethodSecurityConfig
import me.june.restaurant.config.SecurityConfig
import me.june.restaurant.dto.UserDto
import me.june.restaurant.entity.User
import me.june.restaurant.mapper.UserDtoMapper
import me.june.restaurant.mapper.UserDtoMapperImpl
import me.june.restaurant.repository.UserRepository
import me.june.restaurant.vo.Gender
import me.june.restaurant.vo.Password
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.TestConstructor
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.EnumType
import javax.persistence.Enumerated

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = [UserService::class, UserDtoMapperImpl::class])
@Import(
        SecurityConfig::class,
        MethodSecurityConfig::class,
        EmbeddedRedisConfig::class
)
@EnableAutoConfiguration
internal class UserServiceTest(
        private val userService: UserService,
        private val passwordEncoder: PasswordEncoder,
) {

    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    @DisplayName("유저 조회")
    fun `존재하는 유저 조회시 성공한다`() {
        // given
        val mockUser = User(password = Password("asdf"),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )

        given(userRepository.save(any(User::class.java))).willReturn(mockUser)
        given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser))

        val savedUser = userRepository.save(mockUser)

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
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty())

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
        val request = UserDto.CreateRequest(password = "asdf",
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )

        val mockUser = User(
                password = Password(request.password),
                username = request.username,
                email = request.email,
                birth = request.birth,
                gender = request.gender
        )
        given(userRepository.save(any(User::class.java))).willReturn(mockUser)
        given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser))

        // when
        val savedUserId = userService.createdUser(request)

        // then
        val ( password, username, email, birth, gender ) = request
        val findUser = userRepository.findByIdOrNull(savedUserId)

        assertThat(findUser).isNotNull
        findUser!!
        assertThat(findUser.password.password).isEqualTo(password)
        assertThat(findUser.username).isEqualTo(username)
        assertThat(findUser.email).isEqualTo(email)
        assertThat(findUser.birth).isEqualTo(birth)
        assertThat(findUser.gender).isEqualTo(gender)
    }

    @Test
    @DisplayName("유저 정보 수정")
    fun `유저 정보 수정에 성공한다`() {
        // given
        val mockUser = User(password = Password(passwordEncoder.encode("asdf")),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )
        given(userRepository.save(any(User::class.java))).willReturn(mockUser)
        given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser))

        val savedUser = userRepository.save(mockUser)

        val request = UserDto.UpdateRequest(
                password = "asdf1234",
                username = "ncucudas",
                email = "ncucudas.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )

        // when
        userService.updateUser(savedUser.id, request)

        // then
        val ( password, username, email, birth, gender ) = request
        val findUser = userRepository.findByIdOrNull(savedUser.id)

        assertThat(findUser).isNotNull
        findUser!!

        assertThat(findUser.password.matchPassword(password, passwordEncoder)).isTrue
        assertThat(findUser.username).isEqualTo(username)
        assertThat(findUser.email).isEqualTo(email)
        assertThat(findUser.birth).isEqualTo(birth)
        assertThat(findUser.gender).isEqualTo(gender)
    }

    @Test
    @DisplayName("존재하지 않는 유저 수정")
    fun `존재하지 않는 유저 수정시 실패한다`() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty())

        val request = UserDto.UpdateRequest(
                password = "asdf1234",
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
        val mockUser = User(password = Password("asdf"),
                username = "ncucu",
                email = "ncucu.me@kakaocommerce.com",
                birth = LocalDate.of(1994, 4, 13),
                gender = Gender.MAN,
        )

        given(userRepository.save(any(User::class.java))).willReturn(mockUser)
        given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser))

        val savedUser = userRepository.save(mockUser)

        // when
        userService.deleteUser(savedUser.id)
        given(userRepository.findById(anyLong())).willReturn(Optional.empty())

        // then
        val findUser = userRepository.findByIdOrNull(savedUser.id)
        assertThat(findUser).isNull()
    }

    @Test
    @DisplayName("유저 삭제 실패")
    fun `존재하지 않는 유저 삭제시 실패한다`() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty())

        // when
        val id = -1L
        val ex = Assertions.assertThrows(UserNotFoundException::class.java) { userService.deleteUser(id) }

        // then
        assertThat(ex.message).isEqualTo("$id 에 해당하는 유저를 찾을 수 없습니다.")
    }
}
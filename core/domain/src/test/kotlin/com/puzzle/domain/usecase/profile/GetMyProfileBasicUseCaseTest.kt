package com.puzzle.domain.usecase.profile

import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.domain.model.profile.MyProfileBasic
import com.puzzle.domain.spy.repository.SpyProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMyProfileBasicUseCaseTest {
    private lateinit var spyProfileRepository: SpyProfileRepository
    private lateinit var getMyProfileBasicUseCase: GetMyProfileBasicUseCase

    @BeforeEach
    fun setup() {
        spyProfileRepository = SpyProfileRepository()
        getMyProfileBasicUseCase = GetMyProfileBasicUseCase(spyProfileRepository)
    }

    @Test
    fun `로컬에서 데이터를 불러오다가 실패했을 경우 서버 데이터를 불러온다`() = runTest {
        // Given
        val remoteProfile = dummyMyProfileBasic.copy(description = "Remote Profile")
        spyProfileRepository.setRemoteMyProfileBasic(remoteProfile)

        // When
        val result = getMyProfileBasicUseCase()

        // Then
        assertEquals(remoteProfile, result.getOrNull())
        assertEquals(1, spyProfileRepository.loadMyProfileBasicCallCount)
    }

    @Test
    fun `로컬에서 데이터를 성공적으로 불러올 경우 서버 요청을 하지 않는다`() = runTest {
        // Given
        val localProfile = dummyMyProfileBasic
        spyProfileRepository.setLocalMyProfileBasic(localProfile)

        // When
        val result = getMyProfileBasicUseCase()

        // Then
        assertEquals(localProfile, result.getOrNull())
        assertEquals(0, spyProfileRepository.loadMyProfileBasicCallCount)
    }

    private val dummyMyProfileBasic = MyProfileBasic(
        description = "새로운 인연을 만나고 싶은 26살 개발자입니다. 여행과 커피, 그리고 좋은 대화를 좋아해요.",
        nickname = "코딩하는개발자",
        age = 26,
        birthdate = "1997-09-12",
        height = 178,
        weight = 72,
        location = "서울 강남구",
        job = "소프트웨어 개발자",
        smokingStatus = "비흡연",
        imageUrl = "https://example.com/profile/dev_profile.jpg",
        contacts = listOf(
            Contact(ContactType.KAKAO_TALK_ID, "dev_coder"),
            Contact(ContactType.INSTAGRAM_ID, "seoul_dev_life"),
            Contact(ContactType.PHONE_NUMBER, "010-1234-5678")
        ),
        snsActivityLevel = "은둔",
    )
}

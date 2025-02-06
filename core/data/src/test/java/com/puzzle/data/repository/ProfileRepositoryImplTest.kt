package com.puzzle.data.repository

import com.puzzle.data.fake.source.profile.FakeLocalProfileDataSource
import com.puzzle.data.fake.source.profile.FakeProfileDataSource
import com.puzzle.data.fake.source.token.FakeLocalTokenDataSource
import com.puzzle.data.fake.source.user.FakeLocalUserDataSource
import com.puzzle.network.model.matching.ValueTalkResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProfileRepositoryImplTest {
    private lateinit var profileDataSource: FakeProfileDataSource
    private lateinit var localProfileDataSource: FakeLocalProfileDataSource
    private lateinit var localTokenDataSource: FakeLocalTokenDataSource
    private lateinit var localUserDataSource: FakeLocalUserDataSource
    private lateinit var matchingRepository: ProfileRepositoryImpl

    @BeforeEach
    fun setUp() {
        profileDataSource = FakeProfileDataSource()
        localProfileDataSource = FakeLocalProfileDataSource()
        localTokenDataSource = FakeLocalTokenDataSource()
        localUserDataSource = FakeLocalUserDataSource()
        matchingRepository = ProfileRepositoryImpl(
            profileDataSource = profileDataSource,
            localProfileDataSource = localProfileDataSource,
            localUserDataSource = localUserDataSource,
            localTokenDataSource = localTokenDataSource,
        )
    }

    @Test
    fun `가치관Talk을 새로 갱신할 경우 id값이 올바르게 내려오지 않은 가치관Talk은 무시한다`() = runTest {
        // given
        profileDataSource.setValueTalks(
            listOf(
                ValueTalkResponse(
                    id = null,
                    category = null,
                    title = null,
                    guide = null
                ),
                ValueTalkResponse(
                    id = 1,
                    category = "음주",
                    title = "술자리에 대한 대화",
                    guide = null
                )
            )
        )

        // when
        matchingRepository.loadValueTalks()

        // then
        val storedTalks = localProfileDataSource.retrieveValueTalks()
        assertTrue(storedTalks.all { it.id != null })
        assertTrue(storedTalks.size == 1)
    }

    @Test
    fun `갱신한 가치관Talk 데이터는 로컬 데이터베이스에 저장한다`() = runTest {
        // given
        val validValueTalks = listOf(
            ValueTalkResponse(
                id = 1,
                title = "술자리에 대한 대화",
                category = "음주",
                guide = null
            ),
            ValueTalkResponse(
                id = 2,
                title = "취미 공유하기",
                category = "취미",
                guide = null
            )
        )
        profileDataSource.setValueTalks(validValueTalks)

        // when
        matchingRepository.loadValueTalks()

        // then
        val storedTalks = localProfileDataSource.retrieveValueTalks()
        assertTrue(storedTalks.size == validValueTalks.size)
        assertTrue(
            storedTalks.all { entity ->
                validValueTalks.any { talk -> talk.id == entity.id && talk.title == entity.title }
            }
        )
    }

    @Test
    fun `유저가 프로필 생성에 성공했을 경우 토큰과 유저 상태를 저장한다`() = runTest {
        // when
        val result = matchingRepository.uploadProfile(
            birthdate = "2000-06-14",
            description = "안녕하세요 반갑습니다.",
            height = 250,
            weight = 123,
            imageUrl = "image_url",
            job = "개발자",
            location = "서울",
            nickname = "태태",
            phoneNumber = "010-3911-1842",
            smokingStatus = "비흡연",
            snsActivityLevel = "활발",
            contacts = emptyList(),
            valuePicks = emptyList(),
            valueTalks = emptyList()
        )

        // then
        assertTrue(localTokenDataSource.accessToken.first().isNotEmpty())
        assertTrue(localTokenDataSource.refreshToken.first().isNotEmpty())
        assertEquals("PENDING", localUserDataSource.userRole.first())
    }
}

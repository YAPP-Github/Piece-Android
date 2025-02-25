package com.puzzle.data.repository

import com.puzzle.data.fake.FakeSseClient
import com.puzzle.data.fake.source.profile.FakeLocalProfileDataSource
import com.puzzle.data.fake.source.profile.FakeProfileDataSource
import com.puzzle.data.fake.source.token.FakeLocalTokenDataSource
import com.puzzle.data.fake.source.user.FakeLocalUserDataSource
import com.puzzle.data.spy.image.SpyImageResizer
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.network.api.sse.SseClient
import com.puzzle.network.model.profile.ValueTalkResponse
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
    private lateinit var profileRepository: ProfileRepositoryImpl
    private lateinit var imageResizer: SpyImageResizer
    private lateinit var sseClient: SseClient

    @BeforeEach
    fun setUp() {
        profileDataSource = FakeProfileDataSource()
        localProfileDataSource = FakeLocalProfileDataSource()
        localTokenDataSource = FakeLocalTokenDataSource()
        localUserDataSource = FakeLocalUserDataSource()
        imageResizer = SpyImageResizer()
        sseClient = FakeSseClient()
        profileRepository = ProfileRepositoryImpl(
            profileDataSource = profileDataSource,
            localProfileDataSource = localProfileDataSource,
            localUserDataSource = localUserDataSource,
            localTokenDataSource = localTokenDataSource,
            imageResizer = imageResizer,
            sseClient = sseClient,
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
                    placeholder = "",
                    guides = null,
                ),
                ValueTalkResponse(
                    id = 1,
                    category = "음주",
                    title = "술자리에 대한 대화",
                    placeholder = "",
                    guides = null,
                )
            )
        )

        // when
        profileRepository.loadValueTalkQuestions()

        // then
        val storedTalks = localProfileDataSource.valueTalkQuestions.first()
        assertTrue(storedTalks.all { it.id != null })
        assertTrue(storedTalks.size == 1)
    }

    @Test
    fun `갱신한 가치관Talk 데이터는 로컬에 저장한다`() = runTest {
        // given
        val validValueTalks = listOf(
            ValueTalkResponse(
                id = 1,
                title = "술자리에 대한 대화",
                category = "음주",
                placeholder = "",
                guides = null
            ),
            ValueTalkResponse(
                id = 2,
                title = "취미 공유하기",
                category = "취미",
                placeholder = "",
                guides = null
            )
        )
        profileDataSource.setValueTalks(validValueTalks)

        // when
        profileRepository.loadValueTalkQuestions()

        // then
        val storedTalks = localProfileDataSource.valueTalkQuestions.first()
        assertTrue(storedTalks.size == validValueTalks.size)
        assertTrue(storedTalks.zip(validValueTalks).all { (stored, response) ->
            stored.id == response.id &&
                    stored.title == response.title &&
                    stored.category == response.category
        })
    }

    @Test
    fun `유저가 프로필 생성에 성공했을 경우 토큰과 유저 상태를 저장한다`() = runTest {
        // when
        profileRepository.uploadProfile(
            birthdate = "2000-06-14",
            description = "안녕하세요 반갑습니다.",
            height = 250,
            weight = 123,
            imageUrl = "image_url",
            job = "개발자",
            location = "서울",
            nickname = "태태",
            smokingStatus = "비흡연",
            snsActivityLevel = "활발",
            contacts = emptyList(),
            valuePicks = emptyList(),
            valueTalks = emptyList(),
        )

        // then
        assertTrue(localTokenDataSource.accessToken.first().isNotEmpty())
        assertTrue(localTokenDataSource.refreshToken.first().isNotEmpty())
        assertEquals("PENDING", localUserDataSource.userRole.first())
    }

    @Test
    fun `유저가 프로필 생성시 이미지를 리사이징한 후 업로드한다`() = runTest {
        // when
        profileRepository.uploadProfile(
            birthdate = "2000-06-14",
            description = "안녕하세요 반갑습니다.",
            height = 250,
            weight = 123,
            imageUrl = "image_url",
            job = "개발자",
            location = "서울",
            nickname = "태태",
            smokingStatus = "비흡연",
            snsActivityLevel = "활발",
            contacts = emptyList(),
            valuePicks = emptyList(),
            valueTalks = emptyList(),
        )

        // then
        assertEquals(1, imageResizer.resizeImageCallCount)
    }

    @Test
    fun `가치관Talk 업데이트 시 로컬에 저장된다`() = runTest {
        // given
        val updatedValueTalks = listOf(
            MyValueTalk(
                id = 1,
                category = "음주",
                title = "술자리에 대한 대화",
                answer = "가끔 즐깁니다.",
                summary = "술자리 즐기기",
                placeholder = "",
                guides = emptyList(),
            )
        )

        // when
        val result = profileRepository.updateMyValueTalks(updatedValueTalks)

        // then
        val storedValueTalks = localProfileDataSource.myValueTalks.first()
        assertEquals(updatedValueTalks, storedValueTalks)
        assertEquals(updatedValueTalks, result.getOrNull())
    }

    @Test
    fun `가치관Pick 업데이트 시 로컬에 저장된다`() = runTest {
        // given
        val updatedValuePicks = listOf(
            MyValuePick(
                id = 1,
                category = "취미",
                question = "주말에 주로 무엇을 하나요?",
                answerOptions = listOf(),
                selectedAnswer = 1
            )
        )

        // when
        val result = profileRepository.updateMyValuePicks(updatedValuePicks)

        // then
        val storedValuePicks = localProfileDataSource.myValuePicks.first()
        assertEquals(updatedValuePicks, storedValuePicks)
        assertEquals(updatedValuePicks, result.getOrNull())
    }

    @Test
    fun `프로필 기본 정보 업데이트 시 로컬에 저장된다`() = runTest {
        // given
        val contacts = listOf(
            Contact(ContactType.KAKAO_TALK_ID, "user123"),
            Contact(ContactType.PHONE_NUMBER, "010-1234-5678")
        )

        // when
        val result = profileRepository.updateMyProfileBasic(
            description = "새로운 자기소개",
            nickname = "업데이트된닉네임",
            birthdate = "1995-01-01",
            height = 180,
            weight = 75,
            location = "서울 강남구",
            job = "소프트웨어 엔지니어",
            smokingStatus = "비흡연",
            snsActivityLevel = "보통",
            imageUrl = "updated_profile_image.jpg",
            contacts = contacts
        )

        // then
        val storedProfileBasic = localProfileDataSource.myProfileBasic.first()

        // 반환된 결과와 로컬에 저장된 프로필이 일치하는지 확인
        val updatedProfileBasic = result.getOrNull()
        assertEquals(updatedProfileBasic, storedProfileBasic)
    }

    @Test
    fun `가치관Talk 요약 업데이트 시 로컬에 저장된다`() = runTest {
        // given
        val originValueTalks = listOf(
            MyValueTalk(
                id = 1,
                category = "음주",
                title = "술자리에 대한 대화",
                answer = "가끔 즐깁니다.",
                summary = "술자리 즐기기",
                placeholder = "",
                guides = emptyList(),
            )
        )
        profileRepository.updateMyValueTalks(originValueTalks)

        // when
        profileRepository.updateAiSummary(profileTalkId = 1, summary = "변경된 요약")

        // then
        val storedValueTalks = localProfileDataSource.myValueTalks.first()
        assertEquals(originValueTalks.map { it.id }, storedValueTalks.map { it.id })
        assertEquals(storedValueTalks.first { it.id == 1 }.summary, "변경된 요약")
    }
}

import com.puzzle.domain.model.profile.AnswerOption
import com.puzzle.domain.model.profile.Contact
import com.puzzle.domain.model.profile.ContactType
import com.puzzle.domain.model.profile.MyProfile
import com.puzzle.domain.model.profile.MyValuePick
import com.puzzle.domain.model.profile.MyValueTalk
import com.puzzle.domain.spy.repository.SpyProfileRepository
import com.puzzle.domain.usecase.profile.GetMyProfileUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMyProfileUseCaseTest {
    private lateinit var spyProfileRepository: SpyProfileRepository
    private lateinit var getMyProfileUseCase: GetMyProfileUseCase

    @BeforeEach
    fun setup() {
        spyProfileRepository = SpyProfileRepository()
        getMyProfileUseCase = GetMyProfileUseCase(spyProfileRepository)
    }

    @Test
    fun `로컬에서 데이터를 불러오다가 실패했을 경우 서버 데이터를 불러온다`() = runTest {
        // Given
        val localProfile = dummyMyProfile
        val remoteProfile = localProfile.copy(description = "Remote Profile")

        spyProfileRepository.setShouldFailLocalRetrieval(true)
        spyProfileRepository.setRemoteOpponentProfile(remoteProfile)

        // When
        val result = getMyProfileUseCase()

        // Then
        assertEquals(remoteProfile, result.getOrNull())
        assertEquals(1, spyProfileRepository.loadMyProfileCallCount)
    }

    @Test
    fun `로컬에서 데이터를 성공적으로 불러올 경우 서버 요청을 하지 않는다`() = runTest {
        // Given
        val localProfile = dummyMyProfile
        spyProfileRepository.setLocalMyProfile(localProfile)

        // When
        val result = getMyProfileUseCase()

        // Then
        assertEquals(localProfile, result.getOrNull())
        assertEquals(0, spyProfileRepository.loadMyProfileCallCount)
    }

    private val dummyMyProfile = MyProfile(
        description = "새로운 인연을 만나고 싶은 26살 개발자입니다. 여행과 커피, 그리고 좋은 대화를 좋아해요.",
        nickname = "코딩하는개발자",
        age = 26,
        birthDate = "1997-09-12",
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
        valuePicks = listOf(
            MyValuePick(
                id = 1,
                category = "인생의 가치",
                question = "당신에게 가장 중요한 것은 무엇인가요?",
                answerOptions = listOf(
                    AnswerOption(1, "성장과 배움"),
                    AnswerOption(2, "가족과 관계"),
                    AnswerOption(3, "자아실현"),
                    AnswerOption(4, "안정과 평화")
                ),
                selectedAnswer = 1
            ),
            MyValuePick(
                id = 2,
                category = "연애관",
                question = "좋은 파트너에게 가장 중요하게 생각하는 것은?",
                answerOptions = listOf(
                    AnswerOption(1, "진실성"),
                    AnswerOption(2, "공감능력"),
                    AnswerOption(3, "지적 호기심"),
                    AnswerOption(4, "유머감각")
                ),
                selectedAnswer = 2
            )
        ),
        valueTalks = listOf(
            MyValueTalk(
                id = 1,
                category = "커리어",
                title = "나의 개발 여정",
                answer = "대학에서 컴퓨터공학을 전공하고 스타트업에서 junior 개발자로 시작했어요. 꾸준한 학습과 도전을 통해 성장하고 있습니다.",
                summary = "기술에 대한 열정과 성장 마인드셋"
            ),
            MyValueTalk(
                id = 2,
                category = "라이프스타일",
                title = "일과 삶의 균형",
                answer = "개발 공부와 개인적인 성장, 취미 활동 사이의 균형을 중요하게 생각해요. 주말에는 주로 카페에서 책을 읽거나 새로운 기술을 공부합니다.",
                summary = "균형 잡힌 삶을 추구하는 개발자"
            )
        )
    )
}

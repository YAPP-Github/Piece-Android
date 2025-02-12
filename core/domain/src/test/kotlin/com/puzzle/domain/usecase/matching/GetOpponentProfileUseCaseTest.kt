import com.puzzle.domain.model.profile.OpponentProfile
import com.puzzle.domain.spy.repository.SpyMatchingRepository
import com.puzzle.domain.usecase.matching.GetOpponentProfileUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetOpponentProfileUseCaseTest {
    private lateinit var spyMatchingRepository: SpyMatchingRepository
    private lateinit var getOpponentProfileUseCase: GetOpponentProfileUseCase

    @BeforeEach
    fun setup() {
        spyMatchingRepository = SpyMatchingRepository()
        getOpponentProfileUseCase = GetOpponentProfileUseCase(spyMatchingRepository)
    }

    @Test
    fun `로컬에서 데이터를 불러오다가 실패했을 경우 서버 데이터를 불러온다`() = runTest {
        // Given
        val localProfile = dummyOpponentProfile
        val remoteProfile = localProfile.copy(description = "Remote Profile")

        spyMatchingRepository.setShouldFailLocalRetrieval(true)
        spyMatchingRepository.setRemoteOpponentProfile(remoteProfile)

        // When
        val result = getOpponentProfileUseCase()

        // Then
        assertEquals(remoteProfile, result.getOrNull())
        assertEquals(1, spyMatchingRepository.loadOpponentProfileCallCount)
    }

    @Test
    fun `로컬에서 데이터를 성공적으로 불러올 경우 서버 요청을 하지 않는다`() = runTest {
        // Given
        val localProfile = dummyOpponentProfile
        spyMatchingRepository.setLocalOpponentProfile(localProfile)

        // When
        val result = getOpponentProfileUseCase()

        // Then
        assertEquals(localProfile, result.getOrNull())
        assertEquals(0, spyMatchingRepository.loadOpponentProfileCallCount)
    }

    private val dummyOpponentProfile = OpponentProfile(
        description = "Local Profile",
        nickname = "LocalUser",
        age = 25,
        birthYear = "1998",
        height = 170,
        weight = 65,
        location = "Seoul",
        job = "Developer",
        smokingStatus = "Non-smoker",
        valuePicks = emptyList(),
        valueTalks = emptyList(),
        imageUrl = "local_image_url"
    )
}

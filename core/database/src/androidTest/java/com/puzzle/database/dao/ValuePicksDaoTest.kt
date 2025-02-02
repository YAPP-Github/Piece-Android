package com.puzzle.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.puzzle.database.PieceDatabase
import com.puzzle.database.model.matching.ValuePickAnswer
import com.puzzle.database.model.matching.ValuePickEntity
import com.puzzle.database.model.matching.ValuePickQuestion
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValuePicksDaoTest {
    private lateinit var valuePicksDao: ValuePicksDao
    private lateinit var db: PieceDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = PieceDatabase::class.java
        ).build()
        valuePicksDao = db.valuePicksDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun 가치관Pick을_삽입하고_조회할_수_있다() = runTest {
        // given
        val valuePickQuestion = ValuePickQuestion(
            id = 1,
            category = "음주",
            question = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?"
        )

        val valuePickAnswers = listOf(
            ValuePickAnswer(
                id = 1,
                questionsId = 1,
                number = 1,
                content = "함께 술을 즐기고 싶어요"
            ),
            ValuePickAnswer(
                id = 2,
                questionsId = 1,
                number = 2,
                content = "같이 술을 즐길 수 없어도 괜찮아요"
            )
        )

        val expected = ValuePickEntity(
            valuePickQuestion = valuePickQuestion,
            answers = valuePickAnswers
        )

        // when
        valuePicksDao.insertValuePickQuestion(expected.valuePickQuestion)
        valuePicksDao.insertValuePickAnswers(expected.answers)
        val actual = valuePicksDao.getValuePicks()

        // then
        assertEquals(listOf(expected), actual)
    }

    @Test
    fun 가치관Pick을_모두_삭제할_수_있다() = runTest {
        // given
        val valuePickQuestion = ValuePickQuestion(
            id = 1,
            category = "음주",
            question = "사귀는 사람과 함께 술을 마시는 것을 좋아하나요?"
        )

        val valuePickAnswers = listOf(
            ValuePickAnswer(
                id = 1,
                questionsId = 1,
                number = 1,
                content = "함께 술을 즐기고 싶어요"
            ),
            ValuePickAnswer(
                id = 2,
                questionsId = 1,
                number = 2,
                content = "같이 술을 즐길 수 없어도 괜찮아요"
            )
        )

        val valuePick = ValuePickEntity(
            valuePickQuestion = valuePickQuestion,
            answers = valuePickAnswers
        )

        valuePicksDao.insertValuePickQuestion(valuePick.valuePickQuestion)
        valuePicksDao.insertValuePickAnswers(valuePick.answers)

        // when
        valuePicksDao.clearValuePicks()
        val actual = valuePicksDao.getValuePicks()

        // then
        val expected = emptyList<ValuePickEntity>()
        assertEquals(expected, actual)
    }

    @Test
    fun 이전_가치관Pick을_삭제하고_새로운_가치관Pick을_삽입할_수_있다() = runTest {
        // given
        val oldValuePickQuestion = ValuePickQuestion(
            id = 1,
            category = "이전 카테고리",
            question = "이전 질문"
        )

        val oldValuePickAnswers = listOf(
            ValuePickAnswer(
                id = 1,
                questionsId = 1,
                number = 1,
                content = "이전 답변 1"
            ),
            ValuePickAnswer(
                id = 2,
                questionsId = 1,
                number = 2,
                content = "이전 답변 2"
            )
        )

        val oldValuePick = ValuePickEntity(
            valuePickQuestion = oldValuePickQuestion,
            answers = oldValuePickAnswers
        )

        valuePicksDao.insertValuePickQuestion(oldValuePick.valuePickQuestion)
        valuePicksDao.insertValuePickAnswers(oldValuePick.answers)

        // when
        val newValuePickQuestion = ValuePickQuestion(
            id = 2,
            category = "새로운 카테고리",
            question = "새로운 질문"
        )

        val newValuePickAnswers = listOf(
            ValuePickAnswer(
                id = 3,
                questionsId = 2,
                number = 1,
                content = "새로운 답변 1"
            ),
            ValuePickAnswer(
                id = 4,
                questionsId = 2,
                number = 2,
                content = "새로운 답변 2"
            )
        )

        val expected = ValuePickEntity(
            valuePickQuestion = newValuePickQuestion,
            answers = newValuePickAnswers
        )

        valuePicksDao.replaceValuePicks(expected)
        val actual = valuePicksDao.getValuePicks()

        // then
        assertEquals(listOf(expected), actual)
    }
}

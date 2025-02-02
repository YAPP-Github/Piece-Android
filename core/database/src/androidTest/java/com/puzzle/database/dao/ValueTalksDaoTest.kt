package com.puzzle.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.puzzle.database.PieceDatabase
import com.puzzle.database.model.matching.ValueTalkEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValueTalksDaoTest {
    private lateinit var valueTalksDao: ValueTalksDao
    private lateinit var db: PieceDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = PieceDatabase::class.java
        ).build()
        valueTalksDao = db.valueTalksDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun 가치관Talk을_삽입하고_조회할_수_있다() = runTest {
        // given
        val valueTalk = ValueTalkEntity(
            id = 1,
            category = "음주",
            title = "술자리에 대한 생각",
            helpMessages = listOf(
                "술을 즐기는 방식이 다를 수 있어요",
                "서로의 취향을 존중하는 게 중요해요"
            )
        )

        // when
        valueTalksDao.insertValueTalks(valueTalk)
        val actual = valueTalksDao.getValueTalks()

        // then
        assertEquals(listOf(valueTalk), actual)
    }

    @Test
    fun 가치관Talk을_모두_삭제할_수_있다() = runTest {
        // given
        val valueTalk = ValueTalkEntity(
            id = 1,
            category = "음주",
            title = "술자리에 대한 생각",
            helpMessages = listOf(
                "술을 즐기는 방식이 다를 수 있어요",
                "서로의 취향을 존중하는 게 중요해요"
            )
        )

        valueTalksDao.insertValueTalks(valueTalk)

        // when
        valueTalksDao.clearValueTalks()
        val actual = valueTalksDao.getValueTalks()

        // then
        val expected = emptyList<ValueTalkEntity>()
        assertEquals(expected, actual)
    }

    @Test
    fun 이전_가치관Talk을_삭제하고_새로운_가치관Talk을_삽입할_수_있다() = runTest {
        // given
        val oldValueTalk = ValueTalkEntity(
            id = 1,
            category = "이전 카테고리",
            title = "이전 제목",
            helpMessages = listOf("이전 메시지 1", "이전 메시지 2")
        )

        valueTalksDao.insertValueTalks(oldValueTalk)

        // when
        val newValueTalk = ValueTalkEntity(
            id = 2,
            category = "새로운 카테고리",
            title = "새로운 제목",
            helpMessages = listOf("새로운 메시지 1", "새로운 메시지 2")
        )

        valueTalksDao.replaceValueTalks(newValueTalk)
        val actual = valueTalksDao.getValueTalks()

        // then
        assertEquals(listOf(newValueTalk), actual)
    }
}

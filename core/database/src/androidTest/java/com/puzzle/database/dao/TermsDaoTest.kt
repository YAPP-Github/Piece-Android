package com.puzzle.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.puzzle.common.parseDateTime
import com.puzzle.database.PieceDatabase
import com.puzzle.database.model.terms.TermEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TermsDaoTest {
    private lateinit var termsDao: TermsDao
    private lateinit var db: PieceDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = PieceDatabase::class.java
        ).build()
        termsDao = db.termsDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun 약관을_삽입하고_조회할_수_있다() = runTest {
        // given
        val expected = listOf(
            TermEntity(1, "이용약관", "내용1", true, "2024-06-01T00:00:00".parseDateTime()),
            TermEntity(2, "개인정보처리방침", "내용2", false, "2024-06-01T00:00:00".parseDateTime())
        )

        // when
        termsDao.insertTerms(*expected.toTypedArray())
        val actual = termsDao.getTerms()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun 약관을_모두_삭제할_수_있다() = runTest {
        // given
        val terms = listOf(
            TermEntity(1, "이용약관", "내용1", true, "2024-06-01T00:00:00".parseDateTime()),
            TermEntity(2, "개인정보처리방침", "내용2", false, "2024-06-01T00:00:00".parseDateTime())
        )
        termsDao.insertTerms(*terms.toTypedArray())

        // when
        termsDao.clearTerms()
        val actual = termsDao.getTerms()

        // then
        val expected = emptyList<TermEntity>()
        assertEquals(expected, actual)
    }

    @Test
    fun 이전_약관을_삭제하고_새로운_약관을_삽입할_수_있다() = runTest {
        // given
        val oldTerms = listOf(
            TermEntity(1, "이전 약관", "이전 내용", true, "2024-06-01T00:00:00".parseDateTime())
        )
        termsDao.insertTerms(*oldTerms.toTypedArray())

        // when
        val expected = listOf(
            TermEntity(2, "새로운 약관", "새로운 내용", false, "2024-06-01T00:00:00".parseDateTime())
        )
        termsDao.replaceTerms(*expected.toTypedArray())
        val actual = termsDao.getTerms()

        // then
        assertEquals(expected, actual)
    }
}

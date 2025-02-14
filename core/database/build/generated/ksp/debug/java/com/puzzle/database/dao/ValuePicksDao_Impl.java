package com.puzzle.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.puzzle.database.model.matching.ValuePickAnswerEntity;
import com.puzzle.database.model.matching.ValuePickEntity;
import com.puzzle.database.model.matching.ValuePickQuestionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ValuePicksDao_Impl implements ValuePicksDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ValuePickQuestionEntity> __insertionAdapterOfValuePickQuestionEntity;

  private final EntityInsertionAdapter<ValuePickAnswerEntity> __insertionAdapterOfValuePickAnswerEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearValuePicks;

  public ValuePicksDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfValuePickQuestionEntity = new EntityInsertionAdapter<ValuePickQuestionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `value_pick_question` (`id`,`category`,`question`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ValuePickQuestionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCategory());
        statement.bindString(3, entity.getQuestion());
      }
    };
    this.__insertionAdapterOfValuePickAnswerEntity = new EntityInsertionAdapter<ValuePickAnswerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `value_pick_answer` (`id`,`questionsId`,`number`,`content`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ValuePickAnswerEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        statement.bindLong(2, entity.getQuestionsId());
        statement.bindLong(3, entity.getNumber());
        statement.bindString(4, entity.getContent());
      }
    };
    this.__preparedStmtOfClearValuePicks = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM value_pick_question";
        return _query;
      }
    };
  }

  @Override
  public Object insertValuePickQuestion(final ValuePickQuestionEntity question,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfValuePickQuestionEntity.insert(question);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertValuePickAnswers(final List<ValuePickAnswerEntity> answers,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfValuePickAnswerEntity.insert(answers);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceValuePicks(final ValuePickEntity[] valuePicks,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ValuePicksDao.DefaultImpls.replaceValuePicks(ValuePicksDao_Impl.this, valuePicks, __cont), $completion);
  }

  @Override
  public Object clearValuePicks(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearValuePicks.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearValuePicks.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getValuePicks(final Continuation<? super List<ValuePickEntity>> $completion) {
    final String _sql = "SELECT * FROM value_pick_question";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<ValuePickEntity>>() {
      @Override
      @NonNull
      public List<ValuePickEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
            final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
            final LongSparseArray<ArrayList<ValuePickAnswerEntity>> _collectionAnswers = new LongSparseArray<ArrayList<ValuePickAnswerEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionAnswers.containsKey(_tmpKey)) {
                _collectionAnswers.put(_tmpKey, new ArrayList<ValuePickAnswerEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipvaluePickAnswerAscomPuzzleDatabaseModelMatchingValuePickAnswerEntity(_collectionAnswers);
            final List<ValuePickEntity> _result = new ArrayList<ValuePickEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ValuePickEntity _item;
              final ValuePickQuestionEntity _tmpValuePickQuestion;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final String _tmpCategory;
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
              final String _tmpQuestion;
              _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
              _tmpValuePickQuestion = new ValuePickQuestionEntity(_tmpId,_tmpCategory,_tmpQuestion);
              final ArrayList<ValuePickAnswerEntity> _tmpAnswersCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpAnswersCollection = _collectionAnswers.get(_tmpKey_1);
              _item = new ValuePickEntity(_tmpValuePickQuestion,_tmpAnswersCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipvaluePickAnswerAscomPuzzleDatabaseModelMatchingValuePickAnswerEntity(
      @NonNull final LongSparseArray<ArrayList<ValuePickAnswerEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipvaluePickAnswerAscomPuzzleDatabaseModelMatchingValuePickAnswerEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`questionsId`,`number`,`content` FROM `value_pick_answer` WHERE `questionsId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "questionsId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfQuestionsId = 1;
      final int _cursorIndexOfNumber = 2;
      final int _cursorIndexOfContent = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<ValuePickAnswerEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ValuePickAnswerEntity _item_1;
          final Integer _tmpId;
          if (_cursor.isNull(_cursorIndexOfId)) {
            _tmpId = null;
          } else {
            _tmpId = _cursor.getInt(_cursorIndexOfId);
          }
          final int _tmpQuestionsId;
          _tmpQuestionsId = _cursor.getInt(_cursorIndexOfQuestionsId);
          final int _tmpNumber;
          _tmpNumber = _cursor.getInt(_cursorIndexOfNumber);
          final String _tmpContent;
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
          _item_1 = new ValuePickAnswerEntity(_tmpId,_tmpQuestionsId,_tmpNumber,_tmpContent);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}

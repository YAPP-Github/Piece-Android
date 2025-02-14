package com.puzzle.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.puzzle.database.converter.PieceConverters;
import com.puzzle.database.model.matching.ValueTalkEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class ValueTalksDao_Impl implements ValueTalksDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ValueTalkEntity> __insertionAdapterOfValueTalkEntity;

  private final PieceConverters __pieceConverters = new PieceConverters();

  private final SharedSQLiteStatement __preparedStmtOfClearValueTalks;

  public ValueTalksDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfValueTalkEntity = new EntityInsertionAdapter<ValueTalkEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `value_talk` (`id`,`category`,`title`,`helpMessages`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ValueTalkEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCategory());
        statement.bindString(3, entity.getTitle());
        final String _tmp = __pieceConverters.fromStringList(entity.getHelpMessages());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
      }
    };
    this.__preparedStmtOfClearValueTalks = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM value_talk";
        return _query;
      }
    };
  }

  @Override
  public Object insertValueTalks(final ValueTalkEntity[] valueTalks,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfValueTalkEntity.insert(valueTalks);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceValueTalks(final ValueTalkEntity[] valueTalks,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ValueTalksDao.DefaultImpls.replaceValueTalks(ValueTalksDao_Impl.this, valueTalks, __cont), $completion);
  }

  @Override
  public Object clearValueTalks(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearValueTalks.acquire();
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
          __preparedStmtOfClearValueTalks.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getValueTalks(final Continuation<? super List<ValueTalkEntity>> $completion) {
    final String _sql = "SELECT * FROM value_talk";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ValueTalkEntity>>() {
      @Override
      @NonNull
      public List<ValueTalkEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfHelpMessages = CursorUtil.getColumnIndexOrThrow(_cursor, "helpMessages");
          final List<ValueTalkEntity> _result = new ArrayList<ValueTalkEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ValueTalkEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final List<String> _tmpHelpMessages;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfHelpMessages)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfHelpMessages);
            }
            final List<String> _tmp_1 = __pieceConverters.toStringList(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.util.List<java.lang.String>', but it was NULL.");
            } else {
              _tmpHelpMessages = _tmp_1;
            }
            _item = new ValueTalkEntity(_tmpId,_tmpCategory,_tmpTitle,_tmpHelpMessages);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

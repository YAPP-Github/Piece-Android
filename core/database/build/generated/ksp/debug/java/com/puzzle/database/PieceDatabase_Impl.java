package com.puzzle.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.puzzle.database.dao.TermsDao;
import com.puzzle.database.dao.TermsDao_Impl;
import com.puzzle.database.dao.ValuePicksDao;
import com.puzzle.database.dao.ValuePicksDao_Impl;
import com.puzzle.database.dao.ValueTalksDao;
import com.puzzle.database.dao.ValueTalksDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PieceDatabase_Impl extends PieceDatabase {
  private volatile TermsDao _termsDao;

  private volatile ValuePicksDao _valuePicksDao;

  private volatile ValueTalksDao _valueTalksDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `term` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `required` INTEGER NOT NULL, `start_date` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `value_pick_question` (`id` INTEGER NOT NULL, `category` TEXT NOT NULL, `question` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `value_pick_answer` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `questionsId` INTEGER NOT NULL, `number` INTEGER NOT NULL, `content` TEXT NOT NULL, FOREIGN KEY(`questionsId`) REFERENCES `value_pick_question`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_value_pick_answer_questionsId` ON `value_pick_answer` (`questionsId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `value_talk` (`id` INTEGER NOT NULL, `category` TEXT NOT NULL, `title` TEXT NOT NULL, `helpMessages` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3d8bc1ef506e74a78947154dfa100bba')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `term`");
        db.execSQL("DROP TABLE IF EXISTS `value_pick_question`");
        db.execSQL("DROP TABLE IF EXISTS `value_pick_answer`");
        db.execSQL("DROP TABLE IF EXISTS `value_talk`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTerm = new HashMap<String, TableInfo.Column>(5);
        _columnsTerm.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerm.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerm.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerm.put("required", new TableInfo.Column("required", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerm.put("start_date", new TableInfo.Column("start_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTerm = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTerm = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTerm = new TableInfo("term", _columnsTerm, _foreignKeysTerm, _indicesTerm);
        final TableInfo _existingTerm = TableInfo.read(db, "term");
        if (!_infoTerm.equals(_existingTerm)) {
          return new RoomOpenHelper.ValidationResult(false, "term(com.puzzle.database.model.terms.TermEntity).\n"
                  + " Expected:\n" + _infoTerm + "\n"
                  + " Found:\n" + _existingTerm);
        }
        final HashMap<String, TableInfo.Column> _columnsValuePickQuestion = new HashMap<String, TableInfo.Column>(3);
        _columnsValuePickQuestion.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValuePickQuestion.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValuePickQuestion.put("question", new TableInfo.Column("question", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysValuePickQuestion = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesValuePickQuestion = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoValuePickQuestion = new TableInfo("value_pick_question", _columnsValuePickQuestion, _foreignKeysValuePickQuestion, _indicesValuePickQuestion);
        final TableInfo _existingValuePickQuestion = TableInfo.read(db, "value_pick_question");
        if (!_infoValuePickQuestion.equals(_existingValuePickQuestion)) {
          return new RoomOpenHelper.ValidationResult(false, "value_pick_question(com.puzzle.database.model.matching.ValuePickQuestionEntity).\n"
                  + " Expected:\n" + _infoValuePickQuestion + "\n"
                  + " Found:\n" + _existingValuePickQuestion);
        }
        final HashMap<String, TableInfo.Column> _columnsValuePickAnswer = new HashMap<String, TableInfo.Column>(4);
        _columnsValuePickAnswer.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValuePickAnswer.put("questionsId", new TableInfo.Column("questionsId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValuePickAnswer.put("number", new TableInfo.Column("number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValuePickAnswer.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysValuePickAnswer = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysValuePickAnswer.add(new TableInfo.ForeignKey("value_pick_question", "CASCADE", "NO ACTION", Arrays.asList("questionsId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesValuePickAnswer = new HashSet<TableInfo.Index>(1);
        _indicesValuePickAnswer.add(new TableInfo.Index("index_value_pick_answer_questionsId", false, Arrays.asList("questionsId"), Arrays.asList("ASC")));
        final TableInfo _infoValuePickAnswer = new TableInfo("value_pick_answer", _columnsValuePickAnswer, _foreignKeysValuePickAnswer, _indicesValuePickAnswer);
        final TableInfo _existingValuePickAnswer = TableInfo.read(db, "value_pick_answer");
        if (!_infoValuePickAnswer.equals(_existingValuePickAnswer)) {
          return new RoomOpenHelper.ValidationResult(false, "value_pick_answer(com.puzzle.database.model.matching.ValuePickAnswerEntity).\n"
                  + " Expected:\n" + _infoValuePickAnswer + "\n"
                  + " Found:\n" + _existingValuePickAnswer);
        }
        final HashMap<String, TableInfo.Column> _columnsValueTalk = new HashMap<String, TableInfo.Column>(4);
        _columnsValueTalk.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValueTalk.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValueTalk.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsValueTalk.put("helpMessages", new TableInfo.Column("helpMessages", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysValueTalk = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesValueTalk = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoValueTalk = new TableInfo("value_talk", _columnsValueTalk, _foreignKeysValueTalk, _indicesValueTalk);
        final TableInfo _existingValueTalk = TableInfo.read(db, "value_talk");
        if (!_infoValueTalk.equals(_existingValueTalk)) {
          return new RoomOpenHelper.ValidationResult(false, "value_talk(com.puzzle.database.model.matching.ValueTalkEntity).\n"
                  + " Expected:\n" + _infoValueTalk + "\n"
                  + " Found:\n" + _existingValueTalk);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3d8bc1ef506e74a78947154dfa100bba", "dcaaf455ecddf0074efe45e9f5fc2c9c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "term","value_pick_question","value_pick_answer","value_talk");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `term`");
      _db.execSQL("DELETE FROM `value_pick_question`");
      _db.execSQL("DELETE FROM `value_pick_answer`");
      _db.execSQL("DELETE FROM `value_talk`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TermsDao.class, TermsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ValuePicksDao.class, ValuePicksDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ValueTalksDao.class, ValueTalksDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TermsDao termsDao() {
    if (_termsDao != null) {
      return _termsDao;
    } else {
      synchronized(this) {
        if(_termsDao == null) {
          _termsDao = new TermsDao_Impl(this);
        }
        return _termsDao;
      }
    }
  }

  @Override
  public ValuePicksDao valuePicksDao() {
    if (_valuePicksDao != null) {
      return _valuePicksDao;
    } else {
      synchronized(this) {
        if(_valuePicksDao == null) {
          _valuePicksDao = new ValuePicksDao_Impl(this);
        }
        return _valuePicksDao;
      }
    }
  }

  @Override
  public ValueTalksDao valueTalksDao() {
    if (_valueTalksDao != null) {
      return _valueTalksDao;
    } else {
      synchronized(this) {
        if(_valueTalksDao == null) {
          _valueTalksDao = new ValueTalksDao_Impl(this);
        }
        return _valueTalksDao;
      }
    }
  }
}

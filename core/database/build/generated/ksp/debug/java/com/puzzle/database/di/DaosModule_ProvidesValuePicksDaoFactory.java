package com.puzzle.database.di;

import com.puzzle.database.PieceDatabase;
import com.puzzle.database.dao.ValuePicksDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DaosModule_ProvidesValuePicksDaoFactory implements Factory<ValuePicksDao> {
  private final Provider<PieceDatabase> databaseProvider;

  public DaosModule_ProvidesValuePicksDaoFactory(Provider<PieceDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ValuePicksDao get() {
    return providesValuePicksDao(databaseProvider.get());
  }

  public static DaosModule_ProvidesValuePicksDaoFactory create(
      javax.inject.Provider<PieceDatabase> databaseProvider) {
    return new DaosModule_ProvidesValuePicksDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DaosModule_ProvidesValuePicksDaoFactory create(
      Provider<PieceDatabase> databaseProvider) {
    return new DaosModule_ProvidesValuePicksDaoFactory(databaseProvider);
  }

  public static ValuePicksDao providesValuePicksDao(PieceDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaosModule.INSTANCE.providesValuePicksDao(database));
  }
}

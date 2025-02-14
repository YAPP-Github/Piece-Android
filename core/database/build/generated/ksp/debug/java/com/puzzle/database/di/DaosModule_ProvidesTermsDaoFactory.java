package com.puzzle.database.di;

import com.puzzle.database.PieceDatabase;
import com.puzzle.database.dao.TermsDao;
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
public final class DaosModule_ProvidesTermsDaoFactory implements Factory<TermsDao> {
  private final Provider<PieceDatabase> databaseProvider;

  public DaosModule_ProvidesTermsDaoFactory(Provider<PieceDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TermsDao get() {
    return providesTermsDao(databaseProvider.get());
  }

  public static DaosModule_ProvidesTermsDaoFactory create(
      javax.inject.Provider<PieceDatabase> databaseProvider) {
    return new DaosModule_ProvidesTermsDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DaosModule_ProvidesTermsDaoFactory create(
      Provider<PieceDatabase> databaseProvider) {
    return new DaosModule_ProvidesTermsDaoFactory(databaseProvider);
  }

  public static TermsDao providesTermsDao(PieceDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaosModule.INSTANCE.providesTermsDao(database));
  }
}

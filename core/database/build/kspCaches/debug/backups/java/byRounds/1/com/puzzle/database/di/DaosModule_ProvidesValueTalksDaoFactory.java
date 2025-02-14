package com.puzzle.database.di;

import com.puzzle.database.PieceDatabase;
import com.puzzle.database.dao.ValueTalksDao;
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
public final class DaosModule_ProvidesValueTalksDaoFactory implements Factory<ValueTalksDao> {
  private final Provider<PieceDatabase> databaseProvider;

  public DaosModule_ProvidesValueTalksDaoFactory(Provider<PieceDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ValueTalksDao get() {
    return providesValueTalksDao(databaseProvider.get());
  }

  public static DaosModule_ProvidesValueTalksDaoFactory create(
      javax.inject.Provider<PieceDatabase> databaseProvider) {
    return new DaosModule_ProvidesValueTalksDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DaosModule_ProvidesValueTalksDaoFactory create(
      Provider<PieceDatabase> databaseProvider) {
    return new DaosModule_ProvidesValueTalksDaoFactory(databaseProvider);
  }

  public static ValueTalksDao providesValueTalksDao(PieceDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaosModule.INSTANCE.providesValueTalksDao(database));
  }
}

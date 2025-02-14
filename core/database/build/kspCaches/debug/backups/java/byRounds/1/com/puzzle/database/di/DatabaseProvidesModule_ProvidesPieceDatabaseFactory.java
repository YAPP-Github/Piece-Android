package com.puzzle.database.di;

import android.content.Context;
import com.puzzle.database.PieceDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseProvidesModule_ProvidesPieceDatabaseFactory implements Factory<PieceDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseProvidesModule_ProvidesPieceDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PieceDatabase get() {
    return providesPieceDatabase(contextProvider.get());
  }

  public static DatabaseProvidesModule_ProvidesPieceDatabaseFactory create(
      javax.inject.Provider<Context> contextProvider) {
    return new DatabaseProvidesModule_ProvidesPieceDatabaseFactory(Providers.asDaggerProvider(contextProvider));
  }

  public static DatabaseProvidesModule_ProvidesPieceDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseProvidesModule_ProvidesPieceDatabaseFactory(contextProvider);
  }

  public static PieceDatabase providesPieceDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseProvidesModule.INSTANCE.providesPieceDatabase(context));
  }
}

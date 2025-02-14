package com.puzzle.database.source.profile;

import com.puzzle.database.dao.ValuePicksDao;
import com.puzzle.database.dao.ValueTalksDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class LocalProfileDataSourceImpl_Factory implements Factory<LocalProfileDataSourceImpl> {
  private final Provider<ValuePicksDao> valuePicksDaoProvider;

  private final Provider<ValueTalksDao> valueTalksDaoProvider;

  public LocalProfileDataSourceImpl_Factory(Provider<ValuePicksDao> valuePicksDaoProvider,
      Provider<ValueTalksDao> valueTalksDaoProvider) {
    this.valuePicksDaoProvider = valuePicksDaoProvider;
    this.valueTalksDaoProvider = valueTalksDaoProvider;
  }

  @Override
  public LocalProfileDataSourceImpl get() {
    return newInstance(valuePicksDaoProvider.get(), valueTalksDaoProvider.get());
  }

  public static LocalProfileDataSourceImpl_Factory create(
      javax.inject.Provider<ValuePicksDao> valuePicksDaoProvider,
      javax.inject.Provider<ValueTalksDao> valueTalksDaoProvider) {
    return new LocalProfileDataSourceImpl_Factory(Providers.asDaggerProvider(valuePicksDaoProvider), Providers.asDaggerProvider(valueTalksDaoProvider));
  }

  public static LocalProfileDataSourceImpl_Factory create(
      Provider<ValuePicksDao> valuePicksDaoProvider,
      Provider<ValueTalksDao> valueTalksDaoProvider) {
    return new LocalProfileDataSourceImpl_Factory(valuePicksDaoProvider, valueTalksDaoProvider);
  }

  public static LocalProfileDataSourceImpl newInstance(ValuePicksDao valuePicksDao,
      ValueTalksDao valueTalksDao) {
    return new LocalProfileDataSourceImpl(valuePicksDao, valueTalksDao);
  }
}

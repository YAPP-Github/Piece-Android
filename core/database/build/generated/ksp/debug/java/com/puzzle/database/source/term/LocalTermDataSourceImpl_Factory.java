package com.puzzle.database.source.term;

import com.puzzle.database.dao.TermsDao;
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
public final class LocalTermDataSourceImpl_Factory implements Factory<LocalTermDataSourceImpl> {
  private final Provider<TermsDao> termsDaoProvider;

  public LocalTermDataSourceImpl_Factory(Provider<TermsDao> termsDaoProvider) {
    this.termsDaoProvider = termsDaoProvider;
  }

  @Override
  public LocalTermDataSourceImpl get() {
    return newInstance(termsDaoProvider.get());
  }

  public static LocalTermDataSourceImpl_Factory create(
      javax.inject.Provider<TermsDao> termsDaoProvider) {
    return new LocalTermDataSourceImpl_Factory(Providers.asDaggerProvider(termsDaoProvider));
  }

  public static LocalTermDataSourceImpl_Factory create(Provider<TermsDao> termsDaoProvider) {
    return new LocalTermDataSourceImpl_Factory(termsDaoProvider);
  }

  public static LocalTermDataSourceImpl newInstance(TermsDao termsDao) {
    return new LocalTermDataSourceImpl(termsDao);
  }
}

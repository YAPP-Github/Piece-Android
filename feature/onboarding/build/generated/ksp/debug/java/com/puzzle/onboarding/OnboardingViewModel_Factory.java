package com.puzzle.onboarding;

import com.puzzle.navigation.NavigationHelper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "cast"
})
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<NavigationHelper> navigationHelperProvider;

  public OnboardingViewModel_Factory(Provider<NavigationHelper> navigationHelperProvider) {
    this.navigationHelperProvider = navigationHelperProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(navigationHelperProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<NavigationHelper> navigationHelperProvider) {
    return new OnboardingViewModel_Factory(navigationHelperProvider);
  }

  public static OnboardingViewModel newInstance(NavigationHelper navigationHelper) {
    return new OnboardingViewModel(navigationHelper);
  }
}

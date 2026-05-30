package com.ramsha.spendwise.viewmodel;

import com.ramsha.spendwise.data.repository.ExpenseRepository;
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
public final class ExpenseViewModel_Factory implements Factory<ExpenseViewModel> {
  private final Provider<ExpenseRepository> repositoryProvider;

  public ExpenseViewModel_Factory(Provider<ExpenseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ExpenseViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ExpenseViewModel_Factory create(Provider<ExpenseRepository> repositoryProvider) {
    return new ExpenseViewModel_Factory(repositoryProvider);
  }

  public static ExpenseViewModel newInstance(ExpenseRepository repository) {
    return new ExpenseViewModel(repository);
  }
}

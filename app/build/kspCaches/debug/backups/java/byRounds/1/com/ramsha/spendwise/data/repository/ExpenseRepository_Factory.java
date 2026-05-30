package com.ramsha.spendwise.data.repository;

import com.ramsha.spendwise.data.local.dao.ExpenseDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "cast"
})
public final class ExpenseRepository_Factory implements Factory<ExpenseRepository> {
  private final Provider<ExpenseDao> daoProvider;

  public ExpenseRepository_Factory(Provider<ExpenseDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ExpenseRepository get() {
    return newInstance(daoProvider.get());
  }

  public static ExpenseRepository_Factory create(Provider<ExpenseDao> daoProvider) {
    return new ExpenseRepository_Factory(daoProvider);
  }

  public static ExpenseRepository newInstance(ExpenseDao dao) {
    return new ExpenseRepository(dao);
  }
}

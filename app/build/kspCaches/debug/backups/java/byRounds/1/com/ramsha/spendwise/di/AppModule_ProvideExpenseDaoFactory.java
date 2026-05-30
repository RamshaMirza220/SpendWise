package com.ramsha.spendwise.di;

import com.ramsha.spendwise.data.local.SpendWiseDatabase;
import com.ramsha.spendwise.data.local.dao.ExpenseDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideExpenseDaoFactory implements Factory<ExpenseDao> {
  private final Provider<SpendWiseDatabase> dbProvider;

  public AppModule_ProvideExpenseDaoFactory(Provider<SpendWiseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ExpenseDao get() {
    return provideExpenseDao(dbProvider.get());
  }

  public static AppModule_ProvideExpenseDaoFactory create(Provider<SpendWiseDatabase> dbProvider) {
    return new AppModule_ProvideExpenseDaoFactory(dbProvider);
  }

  public static ExpenseDao provideExpenseDao(SpendWiseDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExpenseDao(db));
  }
}

package jp.co.yumemi.android.codecheck.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.codecheck.repository.GithubRepository
import jp.co.yumemi.android.codecheck.usecase.SearchRepositoriesUseCase

@Module
@InstallIn(SingletonComponent::class)
/** リポジトリのモジュール */
object RepositoryModule {
    /** 検索リポジトリのUseCaseを提供する */
    @Provides
    fun provideSearchRepositoriesUseCase(repository: GithubRepository): SearchRepositoriesUseCase {
        return repository
    }
}
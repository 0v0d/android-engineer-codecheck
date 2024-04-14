package jp.co.yumemi.android.codecheck.usecase

import jp.co.yumemi.android.codecheck.model.api.APIGitHubResponse
import retrofit2.Response

/** リポジトリの検索を行うUseCase */
interface SearchRepositoriesUseCase {
    suspend fun execute(query: String): Response<APIGitHubResponse>?
}
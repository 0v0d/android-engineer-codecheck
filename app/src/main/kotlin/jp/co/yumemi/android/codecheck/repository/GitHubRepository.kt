package jp.co.yumemi.android.codecheck.repository

import android.util.LruCache
import jp.co.yumemi.android.codecheck.source.GithubNetworkDataSource
import jp.co.yumemi.android.codecheck.model.api.APIGitHubResponse
import jp.co.yumemi.android.codecheck.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * GitHubのAPIを叩くためのRepository
 * @param service GitHubのAPIを叩くためのInterface
 */
@Singleton
class GithubRepository @Inject constructor(
    private val service: GithubNetworkDataSource
) : SearchRepositoriesUseCase {
    /** キャッシュサイズ */
    private val cacheSize = 5

    /** キャッシュ */
    private val cache = LruCache<String, Response<APIGitHubResponse>?>(cacheSize)

    /** リポジトリ情報を取得
     * @param query 検索条件
     * @return Response<GitHubResponse>リポジトリ情報 or null
     */
    override suspend fun execute(query: String): Response<APIGitHubResponse>? {
        return searchRepositoriesData(query)
    }

    /**
     * GitHubのAPIを利用して、リポジトリ情報を取得
     * @param query 検索条件
     * @return Response<GitHubResponse>リポジトリ情報 or null
     */
    private suspend fun searchRepositoriesData(query: String): Response<APIGitHubResponse>? =
        withContext(Dispatchers.IO) {
            // キャッシュから結果を取得
            cache[query]?.let { return@withContext it }

            // キャッシュになければ新たに検索
            return@withContext try {
                val response = service.getRepositoriesData(query)
                cache.put(query, response)
                response
            } catch (e: IOException) {
                null
            }
        }
}
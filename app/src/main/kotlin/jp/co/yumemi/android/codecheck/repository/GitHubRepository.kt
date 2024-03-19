package jp.co.yumemi.android.codecheck.repository

import android.util.LruCache
import jp.co.yumemi.android.codecheck.source.GithubNetworkDataSource
import jp.co.yumemi.android.codecheck.model.GitHubResponse
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
) {
    /** キャッシュ */
    private val cache = LruCache<String, Response<GitHubResponse>?>(5)

    /**
     * GitHubのAPIを利用して、リポジトリ情報を取得
     * @param query 検索条件
     * @return Response<GitHubResponse>リポジトリ情報 or null
     */
    fun searchRepositoriesData(query: String): Response<GitHubResponse>? {
        // キャッシュから結果を取得
        cache[query]?.let { return it }

        // キャッシュになければ新たに検索
        return try {
            val response = service.getRepositoriesData(query).execute()
            cache.put(query, response)
            response
        } catch (e: IOException) {
            null
        }
    }
}

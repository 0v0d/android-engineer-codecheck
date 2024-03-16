package jp.co.yumemi.android.codecheck.repository

import jp.co.yumemi.android.codecheck.source.GithubNetworkDataSource
import jp.co.yumemi.android.codecheck.model.GitHubResponse
import retrofit2.Response

import java.io.IOException
import javax.inject.Inject

/** GitHubのAPIを叩くためのRepository
 * @param service GitHubのAPIを叩くためのInterface
 */
class GithubRepository @Inject constructor(
    private val service: GithubNetworkDataSource
) {
    /**
     * GitHubのAPIを利用して、リポジトリ情報を取得
     * @param query 検索条件
     * @return Response<GitHubResponse>リポジトリ情報 or null
     */
    fun searchRepositoriesData(query: String): Response<GitHubResponse>? {
        return try {
            service.getRepositoriesData(query).execute()
        } catch (e: IOException) {
            null
        }
    }
}
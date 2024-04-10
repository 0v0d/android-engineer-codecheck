package jp.co.yumemi.android.codecheck.source

import jp.co.yumemi.android.codecheck.model.GitHubResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/** GitHubのAPIを叩くためのInterface */
interface GithubNetworkDataSource {
    /** リポジトリ情報を取得
     * @param query 検索条件
     * @return Call<GitHubResponse>リポジトリ情報
     */
    @GET("search/repositories")
    suspend fun getRepositoriesData(@Query("q") query: String): Response<GitHubResponse>
}
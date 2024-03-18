package jp.co.yumemi.android.codecheck.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * リポジトリ情報を保持するデータクラス
 * @param id リポジトリID
 * @param name リポジトリ名
 * @param fullName リポジトリのフルネーム
 * @param owner オーナー情報
 * @param htmlUrl GitHubのURL
 * @param description 説明
 * @param language 言語
 * @param stargazersCount スター数
 * @param watchersCount Watch数
 * @param forksCount フォーク数
 * @param openIssuesCount オープンイシュー数
 */
@Parcelize
data class RepositoryItem(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "full_name")
    val fullName: String,
    @Json(name = "owner")
    val owner: OwnerItem,
    @Json(name = "html_url")
    val htmlUrl: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "language")
    val language: String?,
    @Json(name = "stargazers_count")
    val stargazersCount: String,
    @Json(name = "watchers_count")
    val watchersCount: String,
    @Json(name = "forks_count")
    val forksCount: String,
    @Json(name = "open_issues_count")
    val openIssuesCount: String
) : Parcelable
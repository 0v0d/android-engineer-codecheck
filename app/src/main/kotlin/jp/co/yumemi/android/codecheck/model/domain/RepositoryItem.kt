package jp.co.yumemi.android.codecheck.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * リポジトリのドメインモデル
 * @param id リポジトリID
 * @param name リポジトリ名
 * @param fullName リポジトリのフルネーム
 * @param owner リポジトリのオーナー情報
 * @param htmlUrl リポジトリのURL
 * @param description リポジトリの説明
 * @param language リポジトリの言語
 * @param stargazersCount スターの数
 * @param watchersCount ウォッチの数
 * @param forksCount フォークの数
 * @param openIssuesCount オープンなIssueの数
 */
@Parcelize
data class RepositoryItem(
    val id: Long,
    val name: String,
    val fullName: String,
    val owner: OwnerItem,
    val htmlUrl: String,
    val description: String?,
    val language: String?,
    val stargazersCount: String,
    val watchersCount: String,
    val forksCount: String,
    val openIssuesCount: String
) : Parcelable
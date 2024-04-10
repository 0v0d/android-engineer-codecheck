package jp.co.yumemi.android.codecheck.model.domain

import java.io.Serializable

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
) : Serializable
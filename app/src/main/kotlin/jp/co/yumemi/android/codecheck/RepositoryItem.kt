package jp.co.yumemi.android.codecheck

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * リポジトリーアイテム
 * @param name リポジトリー名
 * @param ownerIconUrl オーナーアイコンURL
 * @param language 言語
 * @param stargazersCount スターガザー数
 * @param watchersCount ウォッチャー数
 * @param forksCount フォーク数
 * @param openIssuesCount オープンイシュー数
 */
@Parcelize
data class RepositoryItem(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable
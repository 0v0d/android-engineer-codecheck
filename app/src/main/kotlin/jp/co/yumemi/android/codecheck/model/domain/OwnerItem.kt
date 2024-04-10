package jp.co.yumemi.android.codecheck.model.domain

/**
 * オーナーのドメインモデル
 * @param login ログイン名
 * @param avatarUrl アバターのURL
 * @param htmlUrl GitHubのURL
 */
data class OwnerItem(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String
)